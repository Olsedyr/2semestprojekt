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

    private final static LoadingHashMaps instance = new LoadingHashMaps();
    public static LoadingHashMaps getInstance() {
        return instance;
    }

    public HashMap<String, String> getProducts(){
        return products;
    }

    public HashMap<String, String> getArticles() {
        return articles;
    }

    public HashMap<String, String> getThumbnails() {
        return thumbnails;
    }


    public void hashMapProductsIntoTextFiles() {
        Path productsfilePath = Paths.get("src/main/data/Files for ListViews/productsFile.txt");

        try {
            FileWriter myWriter = new FileWriter(productsfilePath.toString());

            for(Map.Entry<String, String> product : products.entrySet()) {
                myWriter.write(product.getValue() + ";;");
            }

            myWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void hashMapArticlesIntoTextFiles() {

        Path articlesfilePath = Paths.get("src/main/data/Files for ListViews/articlesFile.txt");

        try {

            FileWriter myWriter = new FileWriter(articlesfilePath.toString());

            for(Map.Entry<String, String> article : articles.entrySet()) {
                myWriter.write(article.getValue() + ";;");
            }

            myWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void hashMapThumbnailsIntoTextFiles() {
        Path thumbnailsfilePath = Paths.get("src/main/data/Files for ListViews/thumbnailsFile.txt");

        try {
            FileWriter myWriter = new FileWriter(thumbnailsfilePath.toString());

            for(Map.Entry<String, String> thumbnail : thumbnails.entrySet()) {
                myWriter.write(thumbnail.getValue() + ";;");
            }

            myWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void textProductsIntoHashMaps() {
        Path productsFilePath = Paths.get("src/main/data/Files for ListViews/productsFile.txt");

        File productFile = new File(String.valueOf(productsFilePath));

        Path thumbnailsFilePath = Paths.get("src/main/data/Files for ListViews/productsFile.txt");

        File thumbnailFile = new File(String.valueOf(thumbnailsFilePath));

        try {
            if(productFile.length() != 0){
                String productsFileContent = Files.readString(productsFilePath);

                String[] lines = productsFileContent.split(";;");

                Path productFilePath;

                Path thumbnailFilePath;

                HashMap<String, String> newProducts = new HashMap<>();

                HashMap<String, String> newThumbnails = new HashMap<>();

                for(int i = 0; i < lines.length; i++){
                    productFilePath = Paths.get("src/main/data/CMS/" + lines[i].substring(0, lines[i].indexOf(";")) + ".txt");
                    File product = new File(String.valueOf(productFilePath));
                    if(!product.createNewFile()){
                        String[] array = lines[i].split(";");
                        newProducts.put(array[0], lines[i]);
                        newThumbnails.put(lines[i].substring(0, lines[i].indexOf(";")) + "_thumbnail", array[0] + ";" + array[1] + ";" + array[2] + ";" + array[3] + ";" + array[5]);
                    } else {
                        product.delete();

                        thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + lines[i].substring(0, lines[i].indexOf("-")) + ".txt");
                        File thumbnail = new File(String.valueOf(thumbnailFilePath));
                        thumbnail.delete();
                    }

                }
                products = newProducts;

                productFile.delete();

                thumbnails = newThumbnails;

                thumbnailFile.delete();

                hashMapProductsIntoTextFiles();

                hashMapThumbnailsIntoTextFiles();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void textArticlesIntoHashMaps() {
        Path filePath = Paths.get("src/main/data/Files for ListViews/articlesFile.txt");

        File articleFile = new File(String.valueOf(filePath));

        try {

            if(articleFile.length() != 0){
                String articlesFileContent = Files.readString(filePath);

                String[] lines = articlesFileContent.split(";;");

                Path articleFilePath;

                HashMap<String, String> newArticles = new HashMap<>();

                for(int i = 0; i < lines.length; i++){

                    articleFilePath = Paths.get("src/main/data/ARTICLES/" + lines[i].substring(0, lines[i].indexOf(";")) + ".txt");
                    File article = new File(String.valueOf(articleFilePath));
                    if(!article.createNewFile()){
                        newArticles.put(lines[i].substring(0, lines[i].indexOf(";")), lines[i]);
                    } else {
                        article.delete();
                    }

                }
                articles = newArticles;

                articleFile.delete();

                hashMapArticlesIntoTextFiles();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void textFilesIntoHashMaps() {
        textProductsIntoHashMaps();

        textArticlesIntoHashMaps();
    }
}
