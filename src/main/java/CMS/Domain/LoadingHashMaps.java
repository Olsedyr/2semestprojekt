package CMS.Domain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LoadingHashMaps {
    private HashMap<String, String> products = new HashMap<>();
    private HashMap<String, String> articles = new HashMap<>();
    private HashMap<String, String> thumbnails = new HashMap<>();

    //Makes LoadingHashMaps a Singleton.
    private final static LoadingHashMaps instance = new LoadingHashMaps();

    //Gets the Singleton instance of LoadingHashMaps.
    public static LoadingHashMaps getInstance() {return instance;}

    public HashMap<String, String> getProducts(){return products;}
    public HashMap<String, String> getArticles() {return articles;}
    public HashMap<String, String> getThumbnails() {return thumbnails;}

    public void hashMapIntoTextFiles(String filename, HashMap<String, String> hashMap) {
        Path filePath = Paths.get("src/main/data/Files for ListViews/" + filename + ".txt");

        try {
            FileWriter myWriter = new FileWriter(filePath.toString());

            for(Map.Entry<String, String> entry : hashMap.entrySet()) {
                myWriter.write(entry.getValue() + ";;;");
            }

            myWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //This function retrieves the previous contents of the HashMaps articles, products and thumbnails from the files they're respectively written in.
    public void textFileIntoHashMaps(String filename, HashMap<String, String> dataMap, HashMap<String, String> thumbnailsCheck) {

        //This part gets the file paths for the .txt-files which hold the saved information used by the HashMaps products and thumbnails.

        Path filePath = Paths.get("src/main/data/Files for ListViews/" + filename + ".txt");

        File hashmapFile = new File(String.valueOf(filePath));

        Path thumbnailsFilePath = Paths.get("src/main/data/Files for ListViews/thumbnailsFile.txt");

        File thumbnailFile = new File(String.valueOf(thumbnailsFilePath));

        try {
            //This part checks if there is any information in the productFile.

            if(hashmapFile.length() != 0){
                //This part reads the information from the productsFile and splits it into the different products' information.

                String fileContent = Files.readString(filePath);

                String[] lines = fileContent.split(";;;");

                //This part makes some necessary variables for the future parts.

                Path hashmapFilePath;

                HashMap<String, String> newHashMap = new HashMap<>();



                Path thumbnailFilePath;

                HashMap<String, String> newThumbnails = new HashMap<>();

                //This part iterates over every product in the productsFile and checks the information against existing products.

                for(int i = 0; i < lines.length; i++){

                    //This part makes a new file based on the product information.

                    hashmapFilePath = Paths.get("src/main/data/CMS/" + lines[i].substring(0, lines[i].indexOf(";;")) + ".txt");
                    File file = new File(String.valueOf(hashmapFilePath));

                    //If the file already exists, the information is put into the previously made HashMaps.
                    //If not, the products and its thumbnail is deleted.

                    if(!file.createNewFile()){
                        String[] array = lines[i].split(";;");

                        newHashMap.put(array[0], lines[i]);

                        System.out.println(lines[i]);

                        System.out.println(thumbnailsCheck);

                        if(thumbnailsCheck != null){
                            newThumbnails.put(array[0].substring(0, array[0].indexOf("---")) + "_thumbnail",
                                    array[0].substring(0, array[0].indexOf("---")) + "_thumbnail" + ";;" + array[1]
                                            + ";;" + array[2] + ";;" + array[3] + ";;" + array[5]);
                        }
                    } else {
                        file.delete();

                        if(thumbnailsCheck != null){
                            thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + lines[i].substring(0, lines[i].indexOf("---")) + "_thumbnail.txt");
                            File thumbnail = new File(String.valueOf(thumbnailFilePath));
                            thumbnail.delete();
                        }
                    }

                }

                //The collective information on existing products and thumbnails are written into the HashMaps products and thumbnails.

                //Afterwards the previous files holding the information about the existing products and thumbnails are deleted.

                products = newHashMap;

                hashmapFile.delete();

                thumbnails = newThumbnails;

                thumbnailFile.delete();

                //New files holding the updated information about the existing products and thumbnails are created.

                CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles(filename, dataMap);

                CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("thumbnailsFile",  CMS.Domain.LoadingHashMaps.getInstance().getThumbnails());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //This function retrieves the previous contents of the HashMaps articles, products and thumbnails from the files they're respectively written in.
    public void textFilesIntoHashMaps() {

        textFileIntoHashMaps("productsFile", CMS.Domain.LoadingHashMaps.getInstance().getProducts(), CMS.Domain.LoadingHashMaps.getInstance().getThumbnails());

        textFileIntoHashMaps("articlesFile", CMS.Domain.LoadingHashMaps.getInstance().getArticles(), null);
    }
}
