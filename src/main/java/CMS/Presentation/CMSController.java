package CMS.Presentation;

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
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class CMSController implements Initializable{

    @FXML
    private ListView<String> productList, articleList;
    @FXML
    private TextField searchBar, searchBar2;
    @FXML
    private WebView webView, webView2;
    private WebEngine engine;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //This part loads our products when the application is started up.
        try {
            CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

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

    // region ----------------------------------------Add function----------------------------------------
    @FXML
    protected void addProduct() throws IOException {
        //This part makes a new pop-up window to write the information.
        PopupWindow popupWindow = new PopupWindow();
        String[] str = popupWindow.getResult();

        if (str != null) {

            //This part makes the information for the file path with the contents of the pop-up window.
            File htmlFile = new File(Paths.get("src/main/data/CMS/" + str[0]
                    + ".txt").toString());

            //This part makes a new file with the contents of the pop-up window.
            if (htmlFile.createNewFile()) {
                FileWriter myWriter = new FileWriter(String.valueOf(htmlFile));
                myWriter.write(str[2]);
                myWriter.close();
            }

            //This part puts the information about the new file into the HashMap containing this information.
            CMS.Domain.LoadingHashMaps.getInstance().getProducts().put(str[0], str[1]);

            String[] array = str[1].split(";;");

            CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().put(str[0].substring(0, array[0].indexOf("---"))
                    + "_thumbnail", array[0].substring(0, array[0].indexOf("---")) + "_thumbnail" + ";;" + array[1]
                    + ";;" + array[2] + ";;" + array[3] + ";;" + array[5]);

            //This part reloads the ListView.
            loadProducts();
        }
    }

    @FXML
    protected void addArticle() throws IOException {
        //This part makes a new pop-up window to write the information.
        PopupWindowArticle popupWindow = new PopupWindowArticle();
        String[] str = popupWindow.getResult();

        if (str != null) {
            //This part makes the information for the file path with the contents of the pop-up window.
            File htmlFile = new File(Paths.get("src/main/data/ARTICLES/" + str[0]
                    + ".txt").toString());

            //This part makes a new file with the contents of the pop-up window.
            if (htmlFile.createNewFile()) {
                FileWriter myWriter = new FileWriter(String.valueOf(htmlFile));
                myWriter.write(str[2]);
                myWriter.close();
            }

            //This part puts the information about the new file into the HashMap containing this information.
            CMS.Domain.LoadingHashMaps.getInstance().getArticles().put(str[0], str[1]);

            //This part reloads the ListView.
            loadArticles();
        }
    }
    //endregion

    // region ----------------------------------------Delete function----------------------------------------
    @FXML
    protected void deleteProduct() throws IOException {
        processDeleting(productList, "src/main/data/CMS/",
                CMS.Domain.LoadingHashMaps.getInstance().getProducts(),
                CMS.Domain.LoadingHashMaps.getInstance().getThumbnails(), () -> {
            try {
                loadProducts();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    protected void deleteArticle() throws IOException {
        processDeleting(articleList, "src/main/data/ARTICLES/",
                CMS.Domain.LoadingHashMaps.getInstance().getArticles(), null, () -> {
                    try {
                loadArticles();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processDeleting(ListView<String> listView, String directory, Map<String, String> dataMap,
                                 Map<String, String> thumbnails, Runnable loadMethod) throws IOException {

        //This part gets the index/indexes selected in our UI's ListView.
        var selectedIndices = listView.getSelectionModel().getSelectedIndices();

        //This part checks if there is only one index selected.
        if (selectedIndices.size() == 1) {

            //This part gets the information from the selected index and changes it to the correct Path.
            String item = listView.getItems().get(selectedIndices.get(0));
            String previousID = item.split(";;")[0];

            Path filePath = Paths.get(directory + previousID + ".txt");

            //This part converts the Path into a File and deletes it.
            File fileToDelete = new File(filePath.toString());
            fileToDelete.delete();

            //This part deletes the entry in the corresponding HashMap.
            dataMap.remove(previousID);

            if (thumbnails != null) {
                String thumbnailID = previousID.substring(0, previousID.indexOf("---")) + "_thumbnail";
                thumbnails.remove(thumbnailID);

                // specify the directory of thumbnail files
                Path thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + thumbnailID + ".txt");
                File thumbnailFileToDelete = new File(thumbnailFilePath.toString());
                thumbnailFileToDelete.delete();
            }

            //This part loads the productList again and resets the search bar's text.
            loadMethod.run();
        }
    }
    //endregion

    // region ----------------------------------------Edit function----------------------------------------
    @FXML
    protected void editProduct() throws IOException {
        //This part edits the product.
        processEditing(productList, "src/main/data/CMS/", CMS.Domain.LoadingHashMaps.getInstance().getProducts(),
                CMS.Domain.LoadingHashMaps.getInstance().getThumbnails(), () -> {
            try {
                loadProducts();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    protected void editArticle() throws IOException {
        //This part edits the article.
        processEditing(articleList, "src/main/data/ARTICLES/",
                CMS.Domain.LoadingHashMaps.getInstance().getArticles(), null, () -> {
            try {
                loadArticles();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processEditing(ListView<String> listView, String directory, Map<String, String> dataMap, Map<String, String> thumbnails, Runnable loadMethod) throws IOException {
        //This part gets the index/indexes selected in our UI's ListView.
        var selectedIndices = listView.getSelectionModel().getSelectedIndices();

        //This part checks if there is only one index selected
        if (selectedIndices.size() == 1) {

            //This part gets the id and template id from the selected index.
            String product = listView.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";;")[0];

            //This part makes a new pop-up window to write the information.
            PopupWindow popupWindow = new PopupWindow();
            String[] str = popupWindow.getResult();

            if (str != null) {
                //This part gets a filepath using the previous id and previous template id.
                File fileToDelete = new File(directory + previousID + ".txt");

                //This part deletes the old file.
                fileToDelete.delete();

                // Delete old thumbnail file.
                if (thumbnails != null) {
                    String thumbnailID = previousID.substring(0, previousID.indexOf("---")) + "_thumbnail";
                    thumbnails.remove(thumbnailID);

                    Path thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + thumbnailID + ".txt");
                    File thumbnailFileToDelete = new File(thumbnailFilePath.toString());
                    thumbnailFileToDelete.delete();
                }

                //This part makes a new file with the information from the pop-up window.
                File newFile = new File(directory + str[0] + ".txt");

                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str[2]);
                myWriter.close();

                //This part replaces the original information in the corresponding HashMap with
                //the new information from the pop-up window.
                dataMap.replace(str[0], str[1]);
                if (thumbnails != null) {
                    String[] array = str[1].split(";;");
                    thumbnails.replace(str[0].substring(0, array[0].indexOf("---"))
                            + "_thumbnail", array[0].substring(0, array[0].indexOf("---")) + "_thumbnail" + ";;" + array[1]
                            + ";;" + array[2] + ";;" + array[3] + ";;" + array[5]);
                }

                //This part reloads our ListView.
                loadMethod.run();
            }
        }
    }
    //endregion

    // region ----------------------------------------Load function----------------------------------------
    public void loadProducts() throws IOException {
        //This part loads the products.
        loadData(productList, CMS.Domain.LoadingHashMaps.getInstance().getProducts());
    }

    public void loadArticles() throws IOException {
        //This part loads the articles.
        loadData(articleList, CMS.Domain.LoadingHashMaps.getInstance().getArticles());
    }

    private void loadData(ListView<String> listView, Map<String, String> dataMap) throws IOException {

        //This part clears the information in ListView containing the list.
        if (listView != null) {
            listView.getItems().clear();

            //This part copies the information in the text file containing the information of the different products
            //into the previously mentioned ListView.
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                listView.getItems().add(entry.getValue());
            }

            //This part refreshes/updates the ListView.
            listView.refresh();
        }
    }
    //endregion

    // region ----------------------------------------WebView function----------------------------------------
    // Display product HTML in web view
    private void webViewShowHtml(String productInfo) throws IOException {
        webViewShowHtmlContent(productInfo, webView, "src/main/data/CMS/");
    }

    // Display article HTML in web view
    private void webViewShowHtmlArticle(String articleInfo) throws IOException {
        webViewShowHtmlContent(articleInfo, webView2, "src/main/data/ARTICLES/");
    }

    private void webViewShowHtmlContent(String info, WebView webView, String directory) throws IOException {
        //This parts gets the information from the listview and splits the string with a regex.
        String[] fields = info.split(";;");

        File htmlFile = new File(Paths.get(directory + fields[0] + ".txt").toString());

        if (htmlFile.exists()) {
            String htmlString = Files.readString(Paths.get(htmlFile.getPath()));

            webView.getEngine().loadContent(htmlString);
        } else {
            webView.getEngine().loadContent("");
        }
    }
    //endregion

    // region ----------------------------------------Search function----------------------------------------
    @FXML
    protected void searchProducts() throws IOException {
        //This part searches the products.
        searchItems(searchBar, productList, () -> {
            try {
                loadProducts();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    protected void searchArticles() throws IOException {
        //This part searches the articles.
        searchItems(searchBar2, articleList, () -> {
            try {
                loadArticles();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void searchItems(TextField searchBar, ListView<String> listView, Runnable loadMethod) throws IOException {
        //This part gets the text in the search bar in lowercase.
        String search_text = searchBar.getText().strip().toLowerCase();

        //This part updates our ListView.
        loadMethod.run();

        //This part makes an ArrayList of Strings to put any results,which contains the words
        //the user is searching for. A minimum of 3 symbols must be written to see any results.
        if (search_text.length() >= 3) {
            ArrayList<String> results = new ArrayList<>();
            for (String product : listView.getItems()) {
                if (product.toLowerCase().contains(search_text)) results.add(product);
            }

            //This part clears the ListView and shows the resulting products instead.
            listView.getItems().clear();
            if (results.size() > 0) {
                for (String found_product : results) listView.getItems().add(found_product);
                listView.refresh();
            }
        }
    }
    //endregion
}