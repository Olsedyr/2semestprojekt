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

    //This function writes the contents of the HashMap products into a file for later retrieval.
    public void hashMapProductsIntoTextFiles() {
        Path productsfilePath = Paths.get("src/main/data/Files for ListViews/productsFile.txt");

        try {
            FileWriter myWriter = new FileWriter(productsfilePath.toString());

            for(Map.Entry<String, String> product : products.entrySet()) {
                myWriter.write(product.getValue() + ";;;");
            }

            myWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //This function writes the contents of the HashMap articles into a file for later retrieval.
    public void hashMapArticlesIntoTextFiles() {

        Path articlesfilePath = Paths.get("src/main/data/Files for ListViews/articlesFile.txt");

        try {

            FileWriter myWriter = new FileWriter(articlesfilePath.toString());

            for(Map.Entry<String, String> article : articles.entrySet()) {
                myWriter.write(article.getValue() + ";;;");
            }

            myWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //This function writes the contents of the HashMap thumbnails into a file for later retrieval.
    public void hashMapThumbnailsIntoTextFiles() {
        Path thumbnailsfilePath = Paths.get("src/main/data/Files for ListViews/thumbnailsFile.txt");

        try {
            FileWriter myWriter = new FileWriter(thumbnailsfilePath.toString());

            for(Map.Entry<String, String> thumbnail : thumbnails.entrySet()) {
                myWriter.write(thumbnail.getValue() + ";;;");
            }

            myWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //This function retrieves the previous contents of the HashMaps products and thumbnails from the files they're respectively written in.
    public void textProductsIntoHashMaps() {
        //This part gets the file paths for the .txt-files which hold the saved information used by the HashMaps products and thumbnails.

        Path productsFilePath = Paths.get("src/main/data/Files for ListViews/productsFile.txt");

        File productFile = new File(String.valueOf(productsFilePath));

        Path thumbnailsFilePath = Paths.get("src/main/data/Files for ListViews/thumbnailsFile.txt");

        File thumbnailFile = new File(String.valueOf(thumbnailsFilePath));

        try {
            //This part checks if there is any information in the productFile.

            if(productFile.length() != 0){
                //This part reads the information from the productsFile and splits it into the different products' information.

                String productsFileContent = Files.readString(productsFilePath);

                String[] lines = productsFileContent.split(";;;");

                //This part makes some necessary variables for the future parts.

                Path productFilePath;

                Path thumbnailFilePath;

                HashMap<String, String> newProducts = new HashMap<>();

                HashMap<String, String> newThumbnails = new HashMap<>();

                //This part iterates over every product in the productsFile and checks the information against existing products.

                for(int i = 0; i < lines.length; i++){

                    //This part makes a new file based on the product information.

                    productFilePath = Paths.get("src/main/data/CMS/" + lines[i].substring(0, lines[i].indexOf(";;")) + ".txt");
                    File product = new File(String.valueOf(productFilePath));

                    //If the file already exists, the information is put into the previously made HashMaps.
                    //If not, the products and its thumbnail is deleted.

                    if(!product.createNewFile()){
                        String[] array = lines[i].split(";;");

                        newProducts.put(array[0], lines[i]);

                        newThumbnails.put(array[0].substring(0, array[0].indexOf("---")) + "_thumbnail",
                                array[0].substring(0, array[0].indexOf("---")) + "_thumbnail" + ";;" + array[1]
                                        + ";;" + array[2] + ";;" + array[3] + ";;" + array[5]);
                    } else {
                        product.delete();

                        thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + lines[i].substring(0, lines[i].indexOf("---")) + "_thumbnail.txt");
                        File thumbnail = new File(String.valueOf(thumbnailFilePath));
                        thumbnail.delete();
                    }

                }

                //The collective information on existing products and thumbnails are written into the HashMaps products and thumbnails.

                //Afterwards the previous files holding the information about the existing products and thumbnails are deleted.

                products = newProducts;

                productFile.delete();

                thumbnails = newThumbnails;

                thumbnailFile.delete();

                //New files holding the updated information about the existing products and thumbnails are created.

                hashMapProductsIntoTextFiles();

                hashMapThumbnailsIntoTextFiles();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //This function retrieves the previous contents of the HashMap articles from the file it is written in.
    public void textArticlesIntoHashMaps() {
        ////This part gets the file paths for the .txt-files which hold the saved information used by the HashMaps articles

        Path filePath = Paths.get("src/main/data/Files for ListViews/articlesFile.txt");

        File articleFile = new File(String.valueOf(filePath));

        try {
            //This part checks if there is any information in the articlesFile.

            if(articleFile.length() != 0){
                //This part reads the information from the articlesFile and splits it into the different articles' information.

                String articlesFileContent = Files.readString(filePath);

                String[] lines = articlesFileContent.split(";;;");

                //This part makes some necessary variables for the future parts.

                Path articleFilePath;

                HashMap<String, String> newArticles = new HashMap<>();

                //This part iterates over every product in the articlesFile and checks the information against existing articles.

                for(int i = 0; i < lines.length; i++){

                    //This part makes a new file based on the article information.

                    articleFilePath = Paths.get("src/main/data/ARTICLES/" + lines[i].substring(0, lines[i].indexOf(";;")) + ".txt");
                    File article = new File(String.valueOf(articleFilePath));

                    //If the file already exists, the information is put into the previously made HashMap.
                    //If not, the article is deleted.

                    if(!article.createNewFile()){
                        newArticles.put(lines[i].substring(0, lines[i].indexOf(";;")), lines[i]);
                    } else {
                        article.delete();
                    }

                }

                //The collective information on existing articles are written into the HashMap articles.

                //Afterwards the previous file holding the information about the existing articles is deleted.

                articles = newArticles;

                articleFile.delete();

                //A new file holding the updated information about the existing articles is created.

                hashMapArticlesIntoTextFiles();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    //This function retrieves the previous contents of the HashMaps articles, products and thumbnails from the files they're respectively written in.
    public void textFilesIntoHashMaps() {
        textProductsIntoHashMaps();

        textArticlesIntoHashMaps();
    }
}
