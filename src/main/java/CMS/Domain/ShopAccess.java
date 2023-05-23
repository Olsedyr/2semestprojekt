package CMS.Domain;

import CMS.ICMS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShopAccess implements ICMS {
    private HashMap<String, String> articles = new HashMap<>();

    private HashMap<String, String> thumbnails = new HashMap<>();

    private static ShopAccess instance = new ShopAccess();
    public static ShopAccess getInstance() {
        return instance;
    }

    //This function gets the HTML content of a file using the given id and template id.
    @Override
    public String getProductPage(String id, String template_id){
        //This part gets the current information about the existing articles, products and thumbnails and puts it into their
        //respective HashMaps located in the class LoadingHashMaps.

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        try {

            //This part uses the given id and template id to find the right file, read it and return its information as a String.

            Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + "---" + template_id + ".txt");

            return Files.readString(htmlFilePath);

            //This part throws an Exception if the file doesn't actually exist.

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getProductPage(String name, String description, double price, int stock, String id, File imageFile,
                          int template_id){
        //This part gets the current information about the existing articles, products and thumbnails and puts it into their
        //respective HashMaps  located in the class LoadingHashMaps.

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        //This part makes the not-so-usable given information into usable Strings through typecasting.

        String price_String = price + "";

        String stock_String = stock + "";

        String filepath = imageFile.getAbsolutePath();

        //This part puts the given information into the respective HashMap located in the class LoadingHashMaps,
        //overwriting when necessary.

        if (CMS.Domain.LoadingHashMaps.getInstance().getProducts().containsKey(id + "---" + template_id)){
            CMS.Domain.LoadingHashMaps.getInstance().getProducts().replace(id + "---" + template_id, id + "---" + template_id + ";;" + name + ";;" + description + ";;" + price_String + ";;" + stock_String + ";;" + filepath);

            CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().replace(id + "_thumbnail", id + "_thumbnail" + ";;" + name + ";;" + price_String + ";;" + filepath);
        } else {
            CMS.Domain.LoadingHashMaps.getInstance().getProducts().put(id + "---" + template_id, id + "---" + template_id + ";;" + name + ";;" + description + ";;" + price_String + ";;" + stock_String + ";;" + filepath);

            CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().put(id + "_thumbnail", id + "_thumbnail" + ";;" + name + ";;" + price_String + ";;" + filepath);
        }
        //This part writes the HashMaps' information into the respective text files.

        CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("productsFile",  CMS.Domain.LoadingHashMaps.getInstance().getProducts());
        CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("thumbnailsFile",  CMS.Domain.LoadingHashMaps.getInstance().getThumbnails());


        try {
            //This part makes the information into HTML through a specific template.

            String productHTML = Create.create(name, description, price_String, stock_String, filepath, template_id);

            //This part makes a new file using the id and template id.

            Path productFilePath = Paths.get("src/main/data/CMS/" + id + "---" + template_id + ".txt");

            File productFile = new File(String.valueOf(productFilePath));

            //This part overwrites any old file with the same id and template id.

            if (!productFile.createNewFile()) {
                productFile.delete();
            }

            FileWriter myWriter = new FileWriter(String.valueOf(productFilePath));
            myWriter.write(productHTML);
            myWriter.close();


            //This part makes a new file using the id and template id.

            Path thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + id + "_thumbnails.txt");

            File thumbnailFile = new File(String.valueOf(thumbnailFilePath));

            //This part overwrites any old file with the same id and template id.

            if (!thumbnailFile.createNewFile()) {
                thumbnailFile.delete();
            }

            //This part creates a thumbnail using the information from the product.

            Create.createThumbnail(id, name, price_String,
                    filepath);

            //This part returns the product's HTML content.

            return productHTML;

            //This part throws an Exception if the file doesn't actually exist.

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Read all article files into this HashMap and get a specific Entry based on its id.
    @Override
    public Map.Entry<String, String> getArticlePage(String title){
        //This part gets the current information about the existing articles, products and thumbnails and puts it into their
        //respective HashMaps located in the class LoadingHashMaps.

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        //This part iterates over every entry in the HashMap articles from the class LoadingHashMaps and converts the information in the entry's value into HTML
        //using the information from the entry's value.

        for(Map.Entry<String, String> entry : CMS.Domain.LoadingHashMaps.getInstance().getArticles().entrySet()) {

            //This part splits the article entry's information into parts.

            String[] array = entry.getValue().split(";;");

            String articleInHTML;

            //This part converts the information into HTML.

            try {
                articleInHTML = Create.create(array[0].substring(0, array[0].indexOf("---")), array[1], array[2], array[3], Integer.parseInt(array[0].substring(array[0].indexOf("---") + 3)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //This part puts the entry into this class' articles HashMap.

            articles.put(entry.getKey(), articleInHTML);

        }

        //This part tries to return an entry with the given title as the entry's key.

        for (Map.Entry<String, String> entry : articles.entrySet()) {
            if (entry.getKey().equals(title)) {
                return entry;
            }
        }

        //If the key is not found in the HashMap, null is returned.

        return null;
    }

    //Read all article files into this HashMap and get them all.
    @Override
    public HashMap<String, String> getArticlePages(){
        //This part gets the current information about the existing articles, products and thumbnails and puts it into their
        //respective HashMaps located in the class LoadingHashMaps.

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        //This part iterates over every entry in the HashMap articles from the class LoadingHashMaps
        // and converts the information in the entry's value into HTML
        //using the information from the entry's value.

        for(Map.Entry<String, String> entry : CMS.Domain.LoadingHashMaps.getInstance().getArticles().entrySet()) {

            //This part splits the article entry's information into parts.

            String[] array = entry.getValue().split(";;");

            String articleInHTML;

            //This part converts the information into HTML.

            try {
                articleInHTML = Create.create(array[0].substring(0, array[0].indexOf("---")), array[1], array[2], array[3],
                        Integer.parseInt(array[0].substring(array[0].indexOf("---") + 3)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //This part puts the entry into this class' articles HashMap.

            articles.put(entry.getKey(), articleInHTML);

        }

        //This part returns the HTML content of every existing article.

        return articles;
    }

    //Read all thumbnail files into this HashMap and get one based on its id.
    @Override
    public Map.Entry<String, String> getThumbnail(String title){
        //This part gets the current information about the existing articles, products and thumbnails and puts it into their
        //respective HashMaps located in the class LoadingHashMaps.

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        //This part iterates over every entry in the HashMap thumbnails from the class LoadingHashMaps,
        //gets the HTML content from the file with the same id and template and saves it in this class' thumbnails HashMap
        //with the same key.

        for (Map.Entry<String, String> entry : CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().entrySet()) {

            //This part reads the HTML content from any .txt file in the following folder containing the entry's key in its name.

            Path htmlFilePath = Paths.get("src/main/data/Thumbnails/" + entry.getKey() + ".txt");

            String htmlContent = null;
            try {
                htmlContent = Files.readString(htmlFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //This part puts the new information into the HashMap,
            // overwriting any existing entry that has the same key with the new information.

            if(thumbnails.containsKey(entry.getKey())){
                thumbnails.replace(entry.getKey(), htmlContent);
            } else {

                thumbnails.put(entry.getKey(), htmlContent);
            }
        }

        //This part tries to return an entry with the given title as the entry's key.

        for (Map.Entry<String, String> entry : thumbnails.entrySet()) {
            if (entry.getKey().equals(title)) {
                return entry;
            }
        }

        //If the key is not found in the HashMap, null is returned.

        return null;
    }

    //Read all thumbnail files into this HashMap and get them all.
    @Override
    public HashMap<String, String> getThumbnails(){


        //This part gets the current information about the existing articles, products and thumbnails and puts it into their
        //respective HashMaps located in the class LoadingHashMaps.

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        //This part iterates over every entry in the HashMap thumbnails from the class LoadingHashMaps,
        //gets the HTML content from the file with the same id and template and saves it in this class' thumbnails HashMap
        //with the same key.

        for(Map.Entry<String, String> entry : CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().entrySet()) {
            String htmlContent;
            try {
                Path htmlFilePath = Paths.get("src/main/data/Thumbnails/" + entry.getKey() + ".txt");

                htmlContent = Files.readString(htmlFilePath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //This part puts the new information into the HashMap,
            // overwriting any existing entry that has the same key with the new information.

            if(thumbnails.containsKey(entry.getKey())){
                thumbnails.replace(entry.getKey(), htmlContent);
            } else {

                thumbnails.put(entry.getKey(), htmlContent);
            }
        }
        
        //This part returns the HTML content of every existing thumbnail.

        return thumbnails;
    }

}
