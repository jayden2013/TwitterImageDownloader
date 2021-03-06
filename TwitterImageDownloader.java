import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Saves images from a specified Twitter account.
 * @author Jayden Weaver
 *
 */
public class TwitterImageDownloader {

	public static void main(String[] args){
		if (args.length == 0 || args.length > 3){
			System.err.println(usage());
			return;
		}
		String username = args[0];
		String fileName = "tweets.csv";
		int desiredAmount = 99999; //99,999 image default
		if (args.length == 2){
			desiredAmount = Integer.parseInt(args[1]);
		}
		else if (args.length == 3){
			desiredAmount = Integer.parseInt(args[1]);
			fileName = args[2];
		}

		try{
			File file = new File(fileName);
			BufferedReader bufRead = new BufferedReader(new FileReader(file));
			String str = "", error = "https://twitter.com/iwazsleep_/status/950134915726680065"; //Error is just the image that will be used if the original photo was deleted, so program will not crash.
			str = bufRead.readLine();
			ArrayList<URL> photoURLs = new ArrayList<URL>();
			ArrayList<String> descriptions = new ArrayList<String>();
			InputStream inStream;
			BufferedReader bufReadImage;
			String pageString = "";
			int descriptionCounter = 0; //Track how many descriptions need to be added to the arraylist.
			int count = 0;

			while ((str = bufRead.readLine()) != null && count < desiredAmount){
				if (str.contains(username) && str.contains("/photo/1")){
					str = str.substring(str.length() - 67, str.length() - 1);
					if (str.contains(username) && str.contains("/photo/1")){
						str = str.substring(4); //Get rid of HTTP
						str = "https" + str;  //Add HTTPS
						URL directurl;
						try{
							directurl = new URL(str);
							inStream = directurl.openStream();
						}
						catch(FileNotFoundException e){
							directurl = new URL(error);
							inStream = directurl.openStream();
						}
						bufReadImage = new BufferedReader(new InputStreamReader(inStream));
						while ((pageString = bufReadImage.readLine()) != null){
							if (pageString.contains(":large")){ //Check if direct link
								photoURLs.add(new URL((pageString.substring(40, 87)))); //Add URL to ArrayList
								count++;
								descriptionCounter++; //count how many times a photo is found in one tweet.
								System.out.println("Found " + count + " image(s)...");
							}
							if (pageString.contains("og:description")){ //Get photo description for output
								for (int i = 0; i < descriptionCounter; i++){ //Add description to arraylist so that each photo has a description and no exception is thrown later.
									descriptions.add(pageString.substring(49, pageString.length() - 5)); //add description to description arraylist
								}
								break; //TURBOCHARGER
							}
						}
						descriptionCounter = 0; //Reset description counter
					}
				}
			}
			bufRead.close();

			System.out.println("Saving " + count + " images...");
			BufferedImage photo;
			File directory = new File(username + "\\");
			if (!directory.exists()){
				directory.mkdir();
			}

			File outputFile;
			String photoName;
			count = 0; //Reset count to use in loop.
			for (URL url : photoURLs){
				photo = ImageIO.read(url);
				photoName = url.toString().substring(28);
				outputFile = new File(username + "\\" + photoName);
				ImageIO.write(photo, "png", outputFile);
				count++;
				System.out.println(count + " Saved: " + photoName + " | " + descriptions.get(count - 1));
			}
			System.out.println("Saved " + count + " images...");
		}
		catch(Exception e){
			System.err.println(e);
		}
	}

	public static String usage(){
		return "java TwitterImageDownloader <username>\n or\njava TwitterImageDownloader <username> <number of images to save>\n or\njava TwitterImageDownloader <username> <number of images to save>  <filepath>";
	}
}
