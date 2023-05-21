package CMS.Domain;

import CMS.ICMS;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShopAccess implements ICMS {
    private HashMap<String, String> articles = new HashMap<>();

    private HashMap<String, String> thumbnails = new HashMap<>();

    private final static ShopAccess instance = new ShopAccess();
    public static ShopAccess getInstance() {
        return instance;
    }

    @Override
    public String getProductPage(String id, String template_id){
        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        try {

            //This part uses the given id and template id to find the right file, read it and return its information as a String.

            Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + "-" + template_id + ".txt");

            return Files.readString(htmlFilePath);

            //This part throws an Exception if the file doesn't actually exist.

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getProductPage(String name, String description, double price, int stock, String id, File imageFile,
                          int template_id){


        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        //This part makes the not-so-usable given information into usable Strings.

        String price_String = price + "";

        String stock_String = stock + "";

        String filepath = imageFile.getAbsolutePath();

        if (CMS.Domain.LoadingHashMaps.getInstance().getProducts().containsKey(id + "-" + template_id)){
            CMS.Domain.LoadingHashMaps.getInstance().getProducts().replace(id + "-" + template_id, id + "-" + template_id + ";" + name + ";" + description + ";" + price_String + ";" + stock_String + ";" + filepath);

            CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().replace(id + "_thumbnail", id + "_thumbnail" + ";" + name + ";" + price_String + ";" + filepath);
        } else {
            CMS.Domain.LoadingHashMaps.getInstance().getProducts().put(id + "-" + template_id, id + "-" + template_id + ";" + name + ";" + description + ";" + price_String + ";" + stock_String + ";" + filepath);

            CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().put(id + "_thumbnail", id + "_thumbnail" + ";" + name + ";" + price_String + ";" + filepath);
        }
        CMS.Domain.LoadingHashMaps.getInstance().hashMapProductsIntoTextFiles();
        CMS.Domain.LoadingHashMaps.getInstance().hashMapThumbnailsIntoTextFiles();


        try {
            //This part makes the information into HTML through a specific template.

            String html = Create.create(name, description, price_String, stock_String, filepath, template_id);

            //This part makes a new file using the id and template id.

            Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + "-" + template_id + ".txt");

            File htmlFile = new File(String.valueOf(htmlFilePath));

            //This part overwrites any old file with the same id and template id.

            if (!htmlFile.createNewFile()) {
                htmlFile.delete();
            }

            FileWriter myWriter = new FileWriter(String.valueOf(htmlFilePath));
            myWriter.write(html);
            myWriter.close();

            return html;

            //This part throws an Exception if the file doesn't actually exist.

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Read all article files into this HashMap and get a specific Entry based on its id.
    @Override
    public Map.Entry<String, String> getArticlePage(String title){

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        for (Map.Entry<String, String> entry : CMS.Domain.LoadingHashMaps.getInstance().getArticles().entrySet()) {
            if (entry.getKey().equals(title)) {
                return entry;
            }
        }
        return null; // Key not found in the HashMap
    }

    //Read all article files into this HashMap and get them all.
    @Override
    public HashMap<String, String> getArticlePages(){


        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        for(Map.Entry<String, String> entry : CMS.Domain.LoadingHashMaps.getInstance().getArticles().entrySet()) {

            String[] array = entry.getValue().split(";");
            try {
                articles.put(entry.getKey(), Create.create(array[0].substring(0, array[0].indexOf("-")), array[1], array[2], array[3], Integer.parseInt(array[0].substring(array[0].indexOf("-") + 1))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return articles;
    }

    //Read all thumbnail files into this HashMap and get one based on its id.
    @Override
    public Map.Entry<String, String> getThumbnail(String title){

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        for (Map.Entry<String, String> entry : CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().entrySet()) {
            if (entry.getKey().equals(title)) {
                return entry;
            }
        }
        return null; // Key not found in the HashMap
    }

    //Read all thumbnail files into this HashMap and get them all.
    @Override
    public HashMap<String, String> getThumbnails(){

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        for(Map.Entry<String, String> entry : CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().entrySet()) {

            try {

                Path htmlFilePath = Paths.get("src/main/data/Thumbnails/" + entry.getKey() + "_thumbnail.txt");

                String htmlContent = Files.readString(htmlFilePath);

                thumbnails.put(entry.getKey(), htmlContent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return thumbnails;
    }

}
