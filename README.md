# Twitter Image Downloader
Parses the image URLs from the tweets.csv file from a Twitter user's tweet archive and saves the images to a folder.

# Usage
First request your Twitter archive from Twitter. 
After receving your archive, copy the tweets.csv file into the directory containing this program. 
Run this program using the commands below. The number of images to save and file path are optional. 
The file path should only be used if tweets.csv is not in the same directory as the program.

## Compile:  
javac *.java  

## Run:
java TwitterImageDownloader \<username\> [number of images to save] [file path]