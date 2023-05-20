package CMS.Presentation;

import CMS.Domain.Create;
import CMS.Domain.ShopAccess;
import CMS.Presentation.PopupWindow;
import CMS.Presentation.PopupWindowArticle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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
    @FXML
    private TextField searchBar2;

    private WebEngine engine;
    @FXML
    private ListView<String> productList;

    @FXML
    private ListView<String> articleList;



    private final static CMSController instance = new CMSController();
    public static CMSController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //This part loads our products when the application is started up.

        try {
            CMS.Domain.ShopAccess.getInstance().textFilesIntoHashMaps();
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

            CMS.Domain.ShopAccess.getInstance().getArticles().remove(previousID);

            //This part loads the productList again and resets the search bar's text.

            loadArticles();
        }
    }

    public void loadArticles() throws IOException {

        CMS.Domain.ShopAccess.getInstance().hashMapArticlesIntoTextFiles();


        //This part clears the information in ListView containing the product list.

        if(articleList != null){
            articleList.getItems().clear();

            //This part copies the information in the text file containing the information of the different products
            //into the previously mentioned ListView.

            CMS.Domain.ShopAccess.getInstance().textFilesIntoHashMaps();

            for(Map.Entry<String, String> entry : CMS.Domain.ShopAccess.getInstance().getArticles().entrySet()) {
                articleList.getItems().add(entry.getValue());
            }

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

            CMS.Domain.ShopAccess.getInstance().getProducts().remove(previousID);

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



                CMS.Domain.ShopAccess.getInstance().getProducts().replace(str[0], str[1]);

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



                CMS.Domain.ShopAccess.getInstance().getArticles().replace(str[0], str[1]);

                //This part reloads our ListView.

                loadArticles();
            }
        }
    }



    public void loadProducts() throws IOException {

        CMS.Domain.ShopAccess.getInstance().hashMapsIntoTextFiles();

        //This part gets the file path and makes an ArrayList of Strings
        //from the information in every file therein.




        //This part clears the information in ListView containing the product list.

        if(productList != null){
            productList.getItems().clear();

            //This part copies the information in the text file containing the information of the different products
            //into the previously mentioned ListView.

            CMS.Domain.ShopAccess.getInstance().textFilesIntoHashMaps();

            for(Map.Entry<String, String> entry : CMS.Domain.ShopAccess.getInstance().getProducts().entrySet()) {
                productList.getItems().add(entry.getValue());
            }

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
        //This parts gets the information from the listview and splits the string with a regex.
        String[] articleFields = articleInfo.split(";");

        //This part gets the ID of the selected item in the listview by checking index 0
        String id = articleFields[0].trim();

        //This part gets the correct file by using the previously obtained id
        Path htmlFilePath = Paths.get("src/main/data/ARTICLES/" + id + ".txt");

        //This part then reads the file as a string
        String htmlContent = Files.readString(htmlFilePath);

        //This part loads the htmlContent into the webview, showing the article in the User Interface.
        engine = webView2.getEngine();
        engine.loadContent(htmlContent);
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


    @FXML
    protected void searchArticles() throws IOException {

        //This part gets the text in the search bar in lowercase.

        String search_text = searchBar2.getText().strip().toLowerCase();

        //This part updates our ListView.

        loadArticles();

        //This part makes an ArrayList of Strings to put any results,which contains the words
        //the user is searching for. A minimum of 3 symbols must be written to see any results.
        if (search_text.length() >= 3) {
            ArrayList<String> results = new ArrayList<>();

            for(String product: articleList.getItems()) {
                if (product.toLowerCase().contains(search_text)) results.add(product);
            }

            //This part clears the ListView and shows the resulting products instead.

            articleList.getItems().clear();
            if (results.size() > 0) {
                for(String found_product: results) articleList.getItems().add(found_product);
                articleList.refresh();
            }
        }
    }



}