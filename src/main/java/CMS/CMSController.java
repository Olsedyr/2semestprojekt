package CMS;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CMSController implements Initializable{

    @FXML
    private WebView webView;
    @FXML
    private WebView webView2;
    @FXML
    private TextField searchBar;
    private WebEngine engine;
    @FXML
    private ListView<String> productList;

    @FXML
    private ListView<String> articleList;

    HashMap<String, String> products = new HashMap<>();

    private HashMap<String, String> articles = new HashMap<>();

    protected HashMap<String, String> getProducts(){
        return products;
    }

    protected HashMap<String, String> getArticles() {
        return articles;
    }

    private final static CMSController instance = new CMSController();
    public static CMSController getCMSController() {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //This part loads our products when the application is started up.

        try {
            textFilesIntoHashMaps();
            loadProducts();
            loadArticles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //This part ensures that we can view our HTML-files.

        engine = webView.getEngine();



        productList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    webViewShowHtml(newValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //This part ensures that we can view our HTML-articles.

        engine = webView2.getEngine();



        articleList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    webViewShowHtmlArticle(newValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

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

            loadProducts();
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

            products.put(str[0], str[1]);

            //This part makes a new file with the contents of the pop-up window.


            FileWriter myWriter = new FileWriter(String.valueOf(htmlFile));
            myWriter.write(str[2]);
            myWriter.close();

            //This part reloads the ListView.

            loadArticles();
        }
    }


    @FXML
    protected void deleteArticle() throws IOException {

        //This part gets the index/indexes selected in our UI's ListView.

        ObservableList<Integer> selectedIndices = articleList.getSelectionModel().getSelectedIndices();

        //This part checks if there is only one index selected.

        if (selectedIndices.size() == 1) {

            //This part gets the information from the selected index and changes it to the correct Path.

            String product = articleList.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";")[0];

            Path filePath = Paths.get("src/main/data/ARTICLES/" + previousID + ".txt");

            //This part converts the Path into a File and deletes it.

            File fileToDelete = new File(filePath.toString());
            fileToDelete.delete();

            //This part deletes the entry in the corresponding HashMap.

            articles.remove(previousID);

            //This part loads the productList again and resets the search bar's text.

            loadArticles();
        }
    }

    public void loadArticles() throws IOException {

        hashMapsIntoTextFiles();

        //This part gets the file path and makes an ArrayList of Strings
        //from the information in every file therein.




        //This part clears the information in ListView containing the product list.

        if(articleList != null){
            articleList.getItems().clear();

            //This part copies the information in the text file containing the information of the different products
            //into the previously mentioned ListView.

            textFilesIntoHashMaps();

            //This part refreshes/updates the ListView.

            articleList.refresh();
        }
    }




    @FXML
    protected void deleteItem() throws IOException {

        //This part gets the index/indexes selected in our UI's ListView.

        ObservableList<Integer> selectedIndices = productList.getSelectionModel().getSelectedIndices();

        //This part checks if there is only one index selected.

        if (selectedIndices.size() == 1) {

            //This part gets the information from the selected index and changes it to the correct Path.

            String product = productList.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";")[0];

            Path filePath = Paths.get("src/main/data/CMS/" + previousID + ".txt");

            //This part converts the Path into a File and deletes it.

            File fileToDelete = new File(filePath.toString());
            fileToDelete.delete();

            //This part deletes the entry in the corresponding HashMap.

            products.remove(previousID);

            //This part loads the productList again and resets the search bar's text.

            loadProducts();
            searchBar.setText("");
        }
    }


    @FXML
    protected void editProduct() throws IOException {

        //This part gets the index/indexes selected in our UI's ListView.

        ObservableList<Integer> selectedIndices = productList.getSelectionModel().getSelectedIndices();

        //This part checks if there is only one index selected.

        if (selectedIndices.size() == 1) {

            //This part gets the id and template id from the selected index.

            String product = productList.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";")[0];

            //This part makes a new pop-up window to write the information.

            PopupWindow popupWindow = new PopupWindow();
            String[] str = popupWindow.getResult();

            if (str != null) {
                //This part gets a filepath using the previous id and previous template id.

                Path filepath = Paths.get("src/main/data/CMS/" + previousID + ".txt");

                //This part deletes the old file.

                File fileToDelete = new File(filepath.toString());
                fileToDelete.delete();

                //This part makes a new file with the information from the pop-up window.

                File newFile = new File(Paths.get("src/main/data/CMS/" + str[0]
                        + ".txt").toString());

                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str[2]);
                myWriter.close();



                products.replace(str[0], str[1]);

                //This part reloads our ListView.

                loadProducts();
            }
        }
    }


    @FXML
    protected void editArticle() throws IOException {

        //This part gets the index/indexes selected in our UI's ListView.

        ObservableList<Integer> selectedIndices = articleList.getSelectionModel().getSelectedIndices();

        //This part checks if there is only one index selected.

        if (selectedIndices.size() == 1) {

            //This part gets the id and template id from the selected index.

            String product = articleList.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";")[0];

            //This part makes a new pop-up window to write the information.

            PopupWindowArticle popupWindowArticle = new PopupWindowArticle();
            String[] str = popupWindowArticle.getResult();

            if (str != null) {
                //This part gets a filepath using the previous id and previous template id.

                Path filepath = Paths.get("src/main/data/ARTICLES/" + previousID + ".txt");

                //This part deletes the old file.

                File fileToDelete = new File(filepath.toString());
                fileToDelete.delete();

                //This part makes a new file with the information from the pop-up window.

                File newFile = new File(Paths.get("src/main/data/ARTICLES/" + str[0]
                        + ".txt").toString());

                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str[2]);
                myWriter.close();



                articles.replace(str[0], str[1]);

                //This part reloads our ListView.

                loadArticles();
            }
        }
    }

/*
    //Understand and comment this method. This method is necessary for the next method.
    public static ArrayList<String> productFilesInFolder(final File folder) throws IOException {

        //This part makes an ArrayList of String to put our products' info into.

        ArrayList<String> files_arrayList = new ArrayList<>();

        //This part throws Exceptions if the folder/files cannot be read.

        if (!folder.isDirectory()) {
            throw new IOException("Path is not a directory: " + folder.getPath());
        }

        //This part makes an Array of Files from the given folder.


        File[] files = folder.listFiles();

        if (files == null) {
            throw new IOException("Unable to list files in the directory: " + folder.getPath());
        }

        //This part reads the lines of all the files in folder.

        for(int i = 0; i < files.length; i++){
            Path filePath = Paths.get(files[i].getPath());
            List<String> list = Files.readAllLines(filePath);

            String product_String = files[i].getName() + ";";

            for(int n = 0; n < list.size(); n++){
                product_String += list.get(n);
            }

            //This part removes the HTML parts of the read files and puts the info of each file into the ArrayList.

            files_arrayList.add(htmlToString(product_String));
        }

        return files_arrayList;
    }

 */


    public void loadProducts() throws IOException {

        hashMapsIntoTextFiles();

        //This part gets the file path and makes an ArrayList of Strings
        //from the information in every file therein.




        //This part clears the information in ListView containing the product list.

        if(productList != null){
            productList.getItems().clear();

            //This part copies the information in the text file containing the information of the different products
            //into the previously mentioned ListView.

            textFilesIntoHashMaps();

            //This part refreshes/updates the ListView.

            productList.refresh();
        }
    }



    private void webViewShowHtml(String productInfo) throws IOException {
        String[] productFields = productInfo.split(";");
        String id = productFields[0].trim();

        Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + ".txt");
        String htmlContent = Files.readString(htmlFilePath);

        engine = webView.getEngine();
        engine.loadContent(htmlContent);
    }


    private void webViewShowHtmlArticle(String articleInfo) throws IOException {
        String[] articleFields = articleInfo.split(";");
        String id = articleFields[0].trim();

        Path htmlFilePath = Paths.get("src/main/data/ARTICLES/" + id + ".txt");
        String htmlContent = Files.readString(htmlFilePath);

        engine = webView2.getEngine();
        engine.loadContent(htmlContent);
    }


    public void hashMapsIntoTextFiles() {
        Path productsfilePath = Paths.get("src/main/data/Files for ListViews/productsFile.txt");

        Path articlesfilePath = Paths.get("src/main/data/Files for ListViews/articlesFile.txt");


        try {
            FileWriter myWriter = new FileWriter(productsfilePath.toString());

            for(Map.Entry<String, String> entry : products.entrySet()) {
                myWriter.write(entry.getValue() + ";;");
            }

            myWriter = new FileWriter(articlesfilePath.toString());

            for(Map.Entry<String, String> entry : articles.entrySet()) {
                myWriter.write(entry.getValue() + ";;");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void textProductsIntoHashMaps() {
        Path filePath = Paths.get("src/main/data/Files for ListViews/productsFile.txt");


        try {
            File productFile = new File(String.valueOf(filePath));

            System.out.println(productFile.createNewFile());

            System.out.println(!productFile.createNewFile());

            if (!productFile.createNewFile()) {
                String productsFileContent = Files.readString(filePath);

                String[] lines = productsFileContent.split(";;");

                Path productFilePath;

                Path filePathThumbnail;

                HashMap<String, String> newProducts = new HashMap<>();

                for(int i = 0; i < lines.length; i++){

                    System.out.println(lines[i].substring(0, lines[i].indexOf(";")));
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

                System.out.println(products);

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
            if (!articleFile.createNewFile()) {
                String articlesFileContent = Files.readString(filePath);

                String[] lines = articlesFileContent.split(";;");

                Path articleFilePath;

                HashMap<String, String> newArticles = new HashMap<>();

                if(lines.length != 1){
                    for(int i = 0; i < lines.length; i++){
                        articleFilePath = Paths.get("src/main/data/ARTICLES/" + lines[i].substring(0, lines[i].indexOf(";")) + ".txt");
                        File article = new File(String.valueOf(articleFilePath));
                        if(!article.createNewFile()){
                            newArticles.put(lines[i].substring(0, lines[i].indexOf(";")), lines[i]);
                        } else {
                            article.delete();
                        }

                    }
                }
                articles = newArticles;

                articleFile.delete();

                hashMapsIntoTextFiles();
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void textFilesIntoHashMaps() {
        textProductsIntoHashMaps();

        textArticlesIntoHashMaps();
    }


    //This function gets the information from the HTML files, so the
    //products in the ListView looks the same,
    //no matter if they were just created or are from old files,
    //before shutting down the application and starting it up again.
    public static String htmlToString(String html) {
        if(html==null){
            return html;
        }

        return html.substring(0, html.indexOf(";") - 4) + ";";
    }

    @FXML
    protected void searchProducts() throws IOException {

        //This part gets the text in the search bar in lowercase.

        String search_text = searchBar.getText().strip().toLowerCase();

        //This part updates our ListView.

        loadProducts();

        //This part makes an ArrayList of Strings to put any results,which contains the words
        //the user is searching for. A minimum of 3 symbols must be written to see any results.
        if (search_text.length() >= 3) {
            ArrayList<String> results = new ArrayList<>();

            for(String product: productList.getItems()) {
                if (product.toLowerCase().contains(search_text)) results.add(product);
            }

            //This part clears the ListView and shows the resulting products instead.

            productList.getItems().clear();
            if (results.size() > 0) {
                for(String found_product: results) productList.getItems().add(found_product);
                productList.refresh();
            }
        }
    }
}