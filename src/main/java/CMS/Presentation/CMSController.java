package CMS.Presentation;

// In this file, various FXML elements are imported for UI functionality,
// including ListView, TextField, WebView, and more.

import CMS.Domain.Create;
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

public class CMSController implements Initializable {

    @FXML
    private ListView<String> productList, articleList;
    @FXML
    private TextField searchBar, searchBar2;
    @FXML
    private WebView webView, webView2;
    private WebEngine engine;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Loads our products when the application is started up.
        try {
            CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();
            createFolders();
            loadProducts();
            loadArticles();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Ensures that we can view our HTML-files.
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

        //Ensures that we can view our HTML-articles.
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
    // Add a new product. A popup window is displayed for input.
    // The input is then used to create a new file and update the HashMaps.
    @FXML
    protected void addProduct() throws IOException {
        processAdding(productList, "src/main/data/CMS/", CMS.Domain.LoadingHashMaps.getInstance().getProducts(),
                CMS.Domain.LoadingHashMaps.getInstance().getThumbnails(), () -> {
                    try {
                        loadProducts();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @FXML
    protected void addArticle() throws IOException {
        processAdding(articleList, "src/main/data/ARTICLES/",
                CMS.Domain.LoadingHashMaps.getInstance().getArticles(), null, () -> {
                    try {
                        loadArticles();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void processAdding(ListView<String> listView, String directory, Map<String, String> dataMap, Map<String, String> thumbnails, Runnable loadMethod) throws IOException {

        String[] str;

        //Makes a new pop-up window to write the information in based on whether it is a product or article.
        if (thumbnails != null) {
            PopupWindow popupWindow = new PopupWindow();
            str = popupWindow.getResult();
        } else {
            PopupWindowArticle popupWindow = new PopupWindowArticle();
            str = popupWindow.getResult();
        }

        if (str != null) {

            //Makes a new file with the information from the pop-up window.
            File newFile = new File(directory + str[0] + ".txt");

            //Makes a new file with the contents of the pop-up window.
            if (newFile.createNewFile()) {
                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str[2]);
                myWriter.close();
            }

            //Puts the information from the pop-up window into the corresponding HashMap with the id and template id used for the key.

            dataMap.put(str[0], str[1]);

            if (thumbnails != null) {
                String[] array = str[1].split(";;");
                thumbnails.put(str[0].substring(0, array[0].indexOf("---"))
                        + "_thumbnail", array[0].substring(0, array[0].indexOf("---")) + "_thumbnail" + ";;" + array[1]
                        + ";;" + array[2] + ";;" + array[3] + ";;" + array[5]);
            }

            //Reloads our ListView.
            loadMethod.run();
        }
    }
    //endregion

    // region ----------------------------------------Delete function----------------------------------------
    // Delete selected product or article. The file associated with
    // the product or article is also deleted and HashMaps updated.
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

        //Gets the index/indexes selected in our UI's ListView.
        var selectedIndices = listView.getSelectionModel().getSelectedIndices();

        //Checks if there is only one index selected.
        if (selectedIndices.size() == 1) {

            //Gets the information from the selected index and changes it to the correct Path.
            String item = listView.getItems().get(selectedIndices.get(0));
            String previousID = item.split(";;")[0];

            Path filePath = Paths.get(directory + previousID + ".txt");

            //Converts the Path into a File and deletes it.
            File fileToDelete = new File(filePath.toString());
            fileToDelete.delete();

            //Deletes the entry in the corresponding HashMap.
            dataMap.remove(previousID);

            if (thumbnails != null) {
                String thumbnailID = previousID.substring(0, previousID.indexOf("---")) + "_thumbnail";
                thumbnails.remove(thumbnailID);

                // specify the directory of thumbnail files
                Path thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + thumbnailID + ".txt");
                File thumbnailFileToDelete = new File(thumbnailFilePath.toString());
                thumbnailFileToDelete.delete();
            }

            //Loads the productList again and resets the search bar's text.
            loadMethod.run();
        }
    }
    //endregion

    // region ----------------------------------------Edit function----------------------------------------
    // Edit selected product or article. A popup window is displayed for input.
    // The input is then used to create a new file and update the HashMaps.
    @FXML
    protected void editProduct() throws IOException {
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
        //Gets the index/indexes selected in our UI's ListView.
        var selectedIndices = listView.getSelectionModel().getSelectedIndices();

        //Checks if there is only one index selected
        if (selectedIndices.size() == 1) {

            //Gets the id and template id from the selected index.
            String product = listView.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";;")[0];

            String[] str;

            //Makes a new pop-up window to write the information in based on whether it is a product or article.
            if (thumbnails != null) {
                PopupWindow popupWindow = new PopupWindow();
                str = popupWindow.getResult();
            } else {
                PopupWindowArticle popupWindow = new PopupWindowArticle();
                str = popupWindow.getResult();
            }

            if (str != null) {
                //Gets a filepath using the previous id and previous template id.
                File fileToDelete = new File(directory + previousID + ".txt");

                //Deletes the old file.
                fileToDelete.delete();

                // Delete old thumbnail file.
                if (thumbnails != null) {
                    String thumbnailID = previousID.substring(0, previousID.indexOf("---")) + "_thumbnail";
                    thumbnails.remove(thumbnailID);

                    Path thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + thumbnailID + ".txt");
                    File thumbnailFileToDelete = new File(thumbnailFilePath.toString());
                    thumbnailFileToDelete.delete();
                }

                //Makes a new file with the information from the pop-up window.
                File newFile = new File(directory + str[0] + ".txt");

                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str[2]);
                myWriter.close();

                //Replaces the original information in the corresponding HashMap with
                //the new information from the pop-up window.
                dataMap.remove(previousID);
                dataMap.put(str[0], str[1]);

                if (thumbnails != null) {
                    String[] array = str[1].split(";;");
                    thumbnails.put(str[0].substring(0, array[0].indexOf("---"))
                            + "_thumbnail", array[0].substring(0, array[0].indexOf("---")) + "_thumbnail" + ";;" + array[1]
                            + ";;" + array[2] + ";;" + array[3] + ";;" + array[5]);
                }

                //Reloads our ListView.
                loadMethod.run();
            }
        }
    }
    //endregion

    // region ----------------------------------------Load function----------------------------------------
    //Load products or articles. The ListView is updated with the content of the HashMap.
    public void loadProducts() throws IOException {
        CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("productsFile", CMS.Domain.LoadingHashMaps.getInstance().getProducts());
        CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("thumbnailsFile", CMS.Domain.LoadingHashMaps.getInstance().getThumbnails());
        loadData(productList, CMS.Domain.LoadingHashMaps.getInstance().getProducts());
    }

    public void loadArticles() throws IOException {
        CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("articlesFile", CMS.Domain.LoadingHashMaps.getInstance().getArticles());
        loadData(articleList, CMS.Domain.LoadingHashMaps.getInstance().getArticles());
    }

    private void loadData(ListView<String> listView, Map<String, String> dataMap) throws IOException {

        //Clears the information in ListView containing the list.
        if (listView != null) {
            listView.getItems().clear();

            //Copies the information in the text file containing the information of the different products
            //into the previously mentioned ListView.
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                listView.getItems().add(entry.getValue());
            }

            //Refreshes/updates the ListView.
            listView.refresh();
        }
    }
    //endregion

    // region ----------------------------------------WebView function----------------------------------------
    // Display HTML content of a product or an article in the associated WebView.
    private void webViewShowHtml(String productInfo) throws IOException {
        webViewShowHtmlContent(productInfo, webView, "src/main/data/CMS/");
    }

    private void webViewShowHtmlArticle(String articleInfo) throws IOException {
        webViewShowHtmlContent(articleInfo, webView2, "src/main/data/ARTICLES/");
    }

    private void webViewShowHtmlContent(String info, WebView webView, String directory) throws IOException {
        //Gets the information from the listview and splits the string with a regex.
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
    //Search for products or articles based on input in the search bar.
    @FXML
    protected void searchProducts() throws IOException {
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
        searchItems(searchBar2, articleList, () -> {
            try {
                loadArticles();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void searchItems(TextField searchBar, ListView<String> listView, Runnable loadMethod) throws IOException {
        //Gets the text in the search bar in lowercase.
        String search_text = searchBar.getText().strip().toLowerCase();

        //The ListView is updated to show the search results.
        loadMethod.run();

        //Makes an ArrayList of Strings to put any results,which contains the words
        //the user is searching for. A minimum of 3 symbols must be written to see any results.
        if (search_text.length() >= 3) {
            ArrayList<String> results = new ArrayList<>();
            for (String product : listView.getItems()) {
                if (product.toLowerCase().contains(search_text)) results.add(product);
            }

            //Clears the ListView and shows the resulting products instead.
            listView.getItems().clear();
            if (results.size() > 0) {
                for (String found_product : results) listView.getItems().add(found_product);
                listView.refresh();
            }
        }
    }
    //endregion


    // region ----------------------------------------Search function----------------------------------------
    private void createFolders() throws Exception {
        //The folder paths to create when loading application stored in a String array
        String[] folderPaths = {
                "src/main/data/ARTICLES/",
                "src/main/data/CMS/",
                "src/main/data/Files for ListViews/",
                "src/main/data/Thumbnails/",
        };

        //Loops through each folderPath from the String array
        try {
            for (String folderPath : folderPaths) {
                Path path = Paths.get(folderPath);

                //Creating the folder using createDirectories method, if the folderpath doesn't exist
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
            }

            //Two-dimensional array. The inner array holds the information for creating a file, the information in the inner
            //array holds the information for each file (In this case changing of CPU,GPU and monitor information)
            String[][] createInfo = {{"ChangeCPU", "Change CPU", "This is how to change your CPU", "CPU picture", "1"},
                    {"ChangeGPU", "Change GPU", "This is how to change your GPU", "GPU picture", "1"},
                    {"MonitorInfo", "Choosing Monitor ", "This is how you choose the best monitor", "MonitorInfo picture", "1"}};

            //
            for (String[] fileInfo : createInfo) {
                String filePath = "src/main/data/ARTICLES/" + fileInfo[0] + "---" + fileInfo[4] + ".txt";
                Path path = Paths.get(filePath);


                if (!Files.exists(path)) {
                    String result = Create.create(fileInfo[0], fileInfo[1], fileInfo[2], fileInfo[3], Integer.parseInt(fileInfo[4]));
                    Files.write(path, result.getBytes());
                }

                CMS.Domain.LoadingHashMaps.getInstance().getArticles().put(fileInfo[0] + "---" + fileInfo[4], fileInfo[0] + "---" + fileInfo[4] + ";;" + fileInfo[1] + ";;" + fileInfo[2]
                        + ";;" + fileInfo[3]);

                loadData(articleList, CMS.Domain.LoadingHashMaps.getInstance().getArticles());

                CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("articlesFile", CMS.Domain.LoadingHashMaps.getInstance().getArticles());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//endregion}