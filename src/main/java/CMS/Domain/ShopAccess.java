package CMS.Domain;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ShopAccess {
    HashMap<String, String> products = new HashMap<>();

    private HashMap<String, String> articles = new HashMap<>();

    private final static ShopAccess instance = new ShopAccess();
    public static ShopAccess getInstance() {
        return instance;
    }

    String getProductPage(String id, String template_id){
        try {

            //This part uses the given id and template id to find the right file, read it and return its information as a String.

            Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + "-" + template_id + ".txt");

            return Files.readString(htmlFilePath);

            //This part throws an Exception if the file doesn't actually exist.

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String getProductPage(String name, String description, double price, int stock, String id, File imageFile,
                          int template_id){

        //This part makes the not-so-usable given information into usable Strings.

        String price_String = price + "";

        String stock_String = stock + "";

        String filepath = imageFile.getPath();

        if (products.containsKey(id + "-" + template_id)){
            products.replace(id + "-" + template_id, id + "-" + template_id + ";" + price_String + ";" + stock_String + ";" + filepath);
        } else {
            products.put(id + "-" + template_id, id + "-" + template_id + ";" + price_String + ";" + stock_String + ";" + filepath);
        }

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







    public void textFilesIntoHashMaps() {
        CMS.Domain.LoadingHashMaps.getInstance().textProductsIntoHashMaps();

        CMS.Domain.LoadingHashMaps.getInstance().textArticlesIntoHashMaps();
    }
}
