package CMS.Domain;

import CMS.Presentation.PopupWindow;
import CMS.Presentation.PopupWindowArticle;
import javafx.fxml.FXML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShopAccess {
    HashMap<String, String> products = new HashMap<>();

    private HashMap<String, String> articles = new HashMap<>();

    public HashMap<String, String> getProducts(){
        return products;
    }

    public HashMap<String, String> getArticles() {
        return articles;
    }

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
    @FXML
    protected void addItem() throws IOException {

        //This part makes a new pop-up window to write the information.

        PopupWindow popupWindow = new PopupWindow();
        String[] str = popupWindow.getResult();

        if (str != null) {

            //This part deletes any file with the same id and template.

            File htmlFile = new File(Paths.get("src/main/data/CMS/" + str[0]
                    + ".txt").toString());

            if (!htmlFile.createNewFile()) {
                htmlFile.delete();
            }

            products.put(str[0], str[1]);

            //This part makes a new file with the contents of the pop-up window.


            FileWriter myWriter = new FileWriter(String.valueOf(htmlFile));
            myWriter.write(str[2]);
            myWriter.close();

            //This part reloads the ListView.

            CMS.Presentation.CMSController.getInstance().loadProducts();
        }
    }


    @FXML
    protected void createArticle() throws IOException {

        //This part makes a new pop-up window to write the information.

        PopupWindowArticle popupWindow = new PopupWindowArticle();

        String[] str = popupWindow.getResult();

        if (str != null) {

            //This part deletes any file with the same id and template.

            File htmlFile = new File(Paths.get("src/main/data/ARTICLES/" + str[0]
                    + ".txt").toString());

            if (!htmlFile.createNewFile()) {
                htmlFile.delete();
            }

            articles.put(str[0], str[1]);



            //This part makes a new file with the contents of the pop-up window.


            FileWriter myWriter = new FileWriter(String.valueOf(htmlFile));
            myWriter.write(str[2]);
            myWriter.close();

            //This part reloads the ListView.

            CMS.Presentation.CMSController.getInstance().loadProducts();
        }
    }

    public void hashMapsIntoTextFiles() {
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

    public void textProductsIntoHashMaps() {
        Path filePath = Paths.get("src/main/data/Files for ListViews/productsFile.txt");

        File productFile = new File(String.valueOf(filePath));

        try {
            if(productFile.length() != 0){
                String productsFileContent = Files.readString(filePath);

                String[] lines = productsFileContent.split(";;");

                Path productFilePath;

                Path filePathThumbnail;

                HashMap<String, String> newProducts = new HashMap<>();

                for(int i = 0; i < lines.length; i++){
                    productFilePath = Paths.get("src/main/data/CMS/" + lines[i].substring(0, lines[i].indexOf(";")) + ".txt");
                    File product = new File(String.valueOf(productFilePath));
                    if(!product.createNewFile()){
                        newProducts.put(lines[i].substring(0, lines[i].indexOf(";")), lines[i]);
                    } else {
                        product.delete();

                        filePathThumbnail = Paths.get("src/main/data/Thumbnails/" + lines[i].substring(0, lines[i].indexOf("-")) + ".txt");
                        File thumbnail = new File(String.valueOf(filePathThumbnail));
                        thumbnail.delete();
                    }

                }
                products = newProducts;

                productFile.delete();

                hashMapsIntoTextFiles();
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
