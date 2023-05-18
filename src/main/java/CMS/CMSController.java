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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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



        engine = webView2.getEngine();
    }

    @FXML
    protected void addItem() throws IOException {

        //This part makes a new pop-up window to write the information.

        PopupWindow popupWindow = new PopupWindow();
        String str = popupWindow.getResult();

        if (str != null) {

            //This part deletes any file with the same id and template.

            File htmlFile = new File(Paths.get("src/main/data/CMS/" + str.substring(0, str.indexOf(";"))
                    + ".txt").toString());

            if (!htmlFile.createNewFile()) {
                htmlFile.delete();
            }

            //This part makes a new file with the contents of the pop-up window.


            FileWriter myWriter = new FileWriter(String.valueOf(htmlFile));
            myWriter.write(str.substring(str.indexOf(";") + 1));
            myWriter.close();

            //This part reloads the ListView.

            loadProducts();
        }
    }

    @FXML
    protected void createArticle() throws IOException {

        //This part makes a new pop-up window to write the information.

        PopupWindowArticle popupWindow = new PopupWindowArticle();

        String str = popupWindow.getResult();

        if (str != null) {

            //This part deletes any file with the same id and template.

            File htmlFile = new File(Paths.get("src/main/data/ARTICLES/" + str.substring(0, str.indexOf(";"))
                    + ".txt").toString());

            if (!htmlFile.createNewFile()) {
                htmlFile.delete();
            }

            //This part makes a new file with the contents of the pop-up window.


            FileWriter myWriter = new FileWriter(String.valueOf(htmlFile));
            myWriter.write(str.substring(str.indexOf(";") + 1));
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

            //This part loads the productList again and resets the search bar's text.

            loadArticles();
        }
    }

    public void loadArticles() throws IOException {

        //This part gets the file path and makes an ArrayList of Strings
        //from the information in every file therein.

        Path filePath = Paths.get("src/main/data/ARTICLES/");

        final File folder = new File(String.valueOf(filePath));

        ArrayList<String> files_arrayList = productFilesInFolder(folder);

        //This part clears the information in ListView containing the product list.

        if(articleList != null){
            articleList.getItems().clear();
        }

        //This part copies the information in the previously mentioned ArrayList
        //into the previously mentioned ListView.

        for(String product: files_arrayList) {
            assert articleList != null;
            articleList.getItems().add(product);
        }

        //This part refreshes/updates the ListView.

        if(articleList != null){
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
            String str = popupWindow.getResult();

            if (str != null) {
                //This part gets a filepath using the previous id and previous template id.

                Path filepath = Paths.get("src/main/data/CMS/" + previousID + ".txt");

                //This part deletes the old file.

                File fileToDelete = new File(filepath.toString());
                fileToDelete.delete();

                //This part makes a new file with the information from the pop-up window.

                File newFile = new File(Paths.get("src/main/data/CMS/" + str.substring(0, str.indexOf(";"))
                        + ".txt").toString());

                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str.substring(str.indexOf(";") + 1));
                myWriter.close();

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
            String str = popupWindowArticle.getResult();

            if (str != null) {
                //This part gets a filepath using the previous id and previous template id.

                Path filepath = Paths.get("src/main/data/ARTICLES/" + previousID + ".txt");

                //This part deletes the old file.

                File fileToDelete = new File(filepath.toString());
                fileToDelete.delete();

                //This part makes a new file with the information from the pop-up window.

                File newFile = new File(Paths.get("src/main/data/ARTICLES/" + str.substring(0, str.indexOf(";"))
                        + ".txt").toString());

                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str.substring(str.indexOf(";") + 1));
                myWriter.close();

                //This part reloads our ListView.

                loadArticles();
            }
        }
    }


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


    public void loadProducts() throws IOException {

        //This part gets the file path and makes an ArrayList of Strings
        //from the information in every file therein.

        Path filePath = Paths.get("src/main/data/CMS/");

        final File folder = new File(String.valueOf(filePath));

        ArrayList<String> files_arrayList = productFilesInFolder(folder);

        //This part clears the information in ListView containing the product list.

        if(productList != null){
            productList.getItems().clear();
        }

        //This part copies the information in the previously mentioned ArrayList
        //into the previously mentioned ListView.

        for(String product: files_arrayList) {
            assert productList != null;
            productList.getItems().add(product);
        }

        //This part refreshes/updates the ListView.

        if(productList != null){
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






    //This function gets the information from the HTML files, so the
    //products in the ListView looks the same,
    //no matter if they were just created or are from old files,
    //before shutting down the application and starting it up again.
    public static String htmlToString(String html) {
        if(html==null){
            return html;
        }

        //This part gets the link of the png.

        Document document = Jsoup.parse(html);

        String picture = html.substring(html.indexOf("src=") + 5);
        
        picture = picture.substring(0, picture.indexOf("=") - 5);


        //This part makes the html() used later preserve linebreaks and spacing.
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");


        //This part ensures that the ListView gets the right information by
        //cleaning, removing and replacing a lot of information.

        String s = document.html().replaceAll("\\\\n", "\n");


        String id = s.substring(s.indexOf("y") + 2, s.indexOf(";")).replace(".txt", "");


        String string = Jsoup.clean(s, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false));


        string = string.replace("\n", ";").replace(";;", ";");


        String name = string.substring(string.indexOf(";") + 1);

        name = name.substring(name.indexOf(" ") + 2).replace("   ", ";");

        name = name.substring(0, name.indexOf(";"));


        //This string is used to combine all of the useful information from
        // the String fed to the function and combining it in the right way.

        String cleanedHTML = id + ";" + name + ";";


        //This part makes any description that is longer than 20 characters end at 20 characters
        // in the productList, and adds "...;" to the end.

        int priceIndex = string.indexOf("    Price:");
        String description = (priceIndex != -1) ? string.substring(0, priceIndex) : string;


        char[] descriptionArray = description.toCharArray();

        if(descriptionArray.length > 20){
            for(int i = 0; i < 20; i++){
                cleanedHTML += descriptionArray[i];
            }

            cleanedHTML += "...;";

            string = string.substring(string.indexOf(";") + 1);
        }


        //And this part removes the last bits of unnecessary information.

        string = string.substring(string.indexOf(";") + 1);

        string = string.substring(string.indexOf(";") + 1);


        //This part removes the last bits of the less useful information.

        cleanedHTML += string.replace("Description: ", "").replace("Price: ", "")
                .replace("Stock: ", "").replace("$", "")
                .replace("  ","").replace(" ;", ";");


        cleanedHTML += ";" + picture;

        return cleanedHTML;
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