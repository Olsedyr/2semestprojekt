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

            //Loads the singleton instance of LoadingHashMaps and runs the textFilesIntoHashMaps
            CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

            //Runs these methods when application is executed
            createFolders();
            loadProducts();
            loadArticles();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Ensures that we can view our HTML-files. The engine renders and executes webpages
        engine = webView.getEngine();

        //This is a listener that tracks what is selected by the user in the productList
        //If a user changes what is selected this listener will change what is shown in the webView
        productList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    webViewShowHtml(newValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Ensures that we can view our HTML-files. The engine renders and executes webpages
        engine = webView2.getEngine();

        //This is a listener that tracks what is selected by the user in the articleList
        //If a user changes what is selected this listener will change what is shown in the webView
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
        //The processAdding is run and takes in multiple arguments/parameters
        processAdding(productList, "src/main/data/CMS/", CMS.Domain.LoadingHashMaps.getInstance().getProducts(),
                //The -> is a lambda expression showing an action need to be made
                CMS.Domain.LoadingHashMaps.getInstance().getThumbnails(), () -> {
                    try {
                        //Loads the products
                        loadProducts();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @FXML
    protected void addArticle() throws IOException {
        //The processAdding is run and takes in multiple arguments/parameters
        processAdding(articleList, "src/main/data/ARTICLES/",
                //The -> is a lambda expression showing an action need to be made
                //Thumbnails is null because an article doesn't have a thumbnail
                CMS.Domain.LoadingHashMaps.getInstance().getArticles(), null, () -> {
                    try {
                        //Loads the articles
                        loadArticles();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void processAdding(ListView<String> listView, String directory, Map<String, String> dataMap, Map<String, String> thumbnails, Runnable loadMethod) throws IOException {

        //String array
        String[] str;

        //Makes a new pop-up window to write the information in based on whether it is a product or article.
        //If it's a product thumbnail is not null else it's an article
        if (thumbnails != null) {
            PopupWindow popupWindow = new PopupWindow();
            //Retrieves the information in the PopUpWindow to the string array
            str = popupWindow.getResult();
        } else {
            PopupWindowArticle popupWindow = new PopupWindowArticle();
            //Retrieves the information in the PopUpWindow to the string array
            str = popupWindow.getResult();
        }

        //If String array str is not null (The user has given information in the PopUpWindow)
        if (str != null) {
            //Makes a new file using the first element of the string array.
            File newFile = new File(directory + str[0] + ".txt");

            //Makes a new file with the contents of the pop-up window that the user gave.
            if (newFile.createNewFile()) {
                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str[2]);
                myWriter.close();
            }

            //Puts the information from the pop-up window into the corresponding HashMap with the id and template id used for the key in the HashMap.
            dataMap.put(str[0], str[1]);

            //if thumbnail is null, then it's a product
            if (thumbnails != null) {
                //split at the second element by using ";;"
                //This will give array with the rest of the values for the specific product
                String[] array = str[1].split(";;");

                //Add the values to the thumbnails to the thumbnails HashMap getting a key value from beginning to "---"
                //This key is what stores the value for a thumbnail
                thumbnails.put(str[0].substring(0, array[0].indexOf("---"))

                        //This is the value string the key corresponds to
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
        //The processDeleting is run and takes in multiple arguments/parameters
        processDeleting(productList, "src/main/data/CMS/",
                CMS.Domain.LoadingHashMaps.getInstance().getProducts(),

                //The -> is a lambda expression showing an action need to be made
                CMS.Domain.LoadingHashMaps.getInstance().getThumbnails(), () -> {
                    try {
                        //Loads the products
                        loadProducts();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @FXML
    protected void deleteArticle() throws IOException {
        //The processDeleting is run and takes in multiple arguments/parameters
        processDeleting(articleList, "src/main/data/ARTICLES/",

                //The -> is a lambda expression showing an action need to be made
                //Thumbnails are null because an article doesn't have a thumbnail
                CMS.Domain.LoadingHashMaps.getInstance().getArticles(), null, () -> {
                    try {
                        //Load the articles
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

            //If the thumbnails is null
            if (thumbnails != null) {
                //Finds the thumbnail and removes it
                String thumbnailID = previousID.substring(0, previousID.indexOf("---")) + "_thumbnail";

                //Deletes the entry in the thumbnails HashMap.
                thumbnails.remove(thumbnailID);

                //specify the directory of thumbnail files
                Path thumbnailFilePath = Paths.get("src/main/data/Thumbnails/" + thumbnailID + ".txt");
                File thumbnailFileToDelete = new File(thumbnailFilePath.toString());

                //Deletes the thumbnail from the filesystem
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
        //The processEditing is run and takes in multiple arguments/parameters
        processEditing(productList, "src/main/data/CMS/", CMS.Domain.LoadingHashMaps.getInstance().getProducts(),

                //The -> is a lambda expression showing an action need to be made
                CMS.Domain.LoadingHashMaps.getInstance().getThumbnails(), () -> {
                    try {

                        //Loads the products
                        loadProducts();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @FXML
    protected void editArticle() throws IOException {
        //The processDeleting is run and takes in multiple arguments/parameters
        processEditing(articleList, "src/main/data/ARTICLES/",

                //The -> is a lambda expression showing an action need to be made
                //Thumbnails are null because an article doesn't have a thumbnail
                CMS.Domain.LoadingHashMaps.getInstance().getArticles(), null, () -> {
                    try {

                        //Loads the articles
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
        //This runs the hashMapIntoTextFiles method from LoadingHashMaps. It takes productsFile and the method getProducts as arguments
        CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("productsFile", CMS.Domain.LoadingHashMaps.getInstance().getProducts());

        //This runs the hashMapIntoTextFiles method. It takes thumbnailsFile and the method getThumbnails as arguments
        //This converts the HashMap data to textFiles
        CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("thumbnailsFile", CMS.Domain.LoadingHashMaps.getInstance().getThumbnails());

        //This runs the loadData method and loads the above retrieved info into the productList in the User Interface
        loadData(productList, CMS.Domain.LoadingHashMaps.getInstance().getProducts());
    }

    public void loadArticles() throws IOException {
        //This runs the hashMapIntoTextFiles method from LoadingHashMaps. It takes articleFile and the method getProducts as arguments
        CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("articlesFile", CMS.Domain.LoadingHashMaps.getInstance().getArticles());

        //This runs the loadData method and loads the above retrieved info into the articleList in the User Interface
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

        //This runs the webViewShowHtmlContent method with takes productInfo as a string
        //Processes the information and puts it into a WebView which is used in the User interface
        webViewShowHtmlContent(productInfo, webView, "src/main/data/CMS/");
    }

    private void webViewShowHtmlArticle(String articleInfo) throws IOException {

        //This runs the webViewShowHtmlContent method with takes articleInfo as a string
        //Processes the information and puts it into a WebView which is used in the User interface
        webViewShowHtmlContent(articleInfo, webView2, "src/main/data/ARTICLES/");
    }

    private void webViewShowHtmlContent(String info, WebView webView, String directory) throws IOException {

        //Gets the information from the listview and splits the string with a regex.
        String[] fields = info.split(";;");

        //Then creates a new file from the directory and the first element in the String array "fields",
        //given as argument when method is run.
        File htmlFile = new File(Paths.get(directory + fields[0] + ".txt").toString());

        //If the file already exists
        if (htmlFile.exists()) {

            //Turn the file into a string using the Files.readString method
            String htmlString = Files.readString(Paths.get(htmlFile.getPath()));

            //Load it into a webView
            webView.getEngine().loadContent(htmlString);
        } else {
            //Else it will just load nothing/empty WebPage content into the WebView
            webView.getEngine().loadContent("");
        }
    }
    //endregion

    // region ----------------------------------------Search function----------------------------------------
    //Search for products or articles based on input in the search bar.
    @FXML
    protected void searchProducts() throws IOException {

        //This runs the searchItems method which takes in the searchbar and the productList as arguments
        //The -> is a lambda expression that shows an action is needed to be made
        searchItems(searchBar, productList, () -> {
            try {

                //This loads the products
                loadProducts();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    protected void searchArticles() throws IOException {
        //This runs the searchItems method which takes in the searchbar and the articleList as arguments
        //The -> is a lambda expression that shows an action is needed to be made
        searchItems(searchBar2, articleList, () -> {
            try {

                //This loads the articles
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

            //Iterates over each item in the listView
            for (String product : listView.getItems()) {
                //This adds product to the arraylist "results" if it matches the searched text
                if (product.toLowerCase().contains(search_text)) results.add(product);
            }

            //Clears the ListView
            listView.getItems().clear();

            //If the results.size is over 0 (There are matches to the search)
            //The results are shown in the listView by iterating over them and adding them to a String called found_product
            //The ListView is then refreshed
            if (results.size() > 0) {
                for (String found_product : results) listView.getItems().add(found_product);
                listView.refresh();
            }
        }
    }
    //endregion


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


            //iterates over each fileInfo array within the createInfo two-dimensional-array
            for (String[] fileInfo : createInfo) {
                //Creates a filePath using a predefined path + fileInfo[0] + "---" + fileInfo[4]
                String filePath = "src/main/data/ARTICLES/" + fileInfo[0] + "---" + fileInfo[4] + ".txt";

                //Using the String filePath it gets the filepath using Paths.get method
                Path path = Paths.get(filePath);

                //if the file doesn't exist it will create a String containing all the info from the fileInfo array from the loop.
                //then it writes it to the path and converts it to a byte array. Bytes is the more suitable format for filewriting
                //instead of strings, which is a sequence of characters.
                if (!Files.exists(path)) {
                    String result = Create.create(fileInfo[0], fileInfo[1], fileInfo[2], fileInfo[3], Integer.parseInt(fileInfo[4]));
                    Files.write(path, result.getBytes());
                }

                //Update the Articles in the hashmap in the CMS.Domain.LoadingHashMaps using. The key for the HashMap is fileInfo[0] & fileInfo[4]
                //fileInfo[0] to fileInfo[4] is the value for the article in the HashMap
                CMS.Domain.LoadingHashMaps.getInstance().getArticles().put(fileInfo[0] + "---" + fileInfo[4], fileInfo[0] + "---" + fileInfo[4] + ";;" + fileInfo[1] + ";;" + fileInfo[2]
                        + ";;" + fileInfo[3]);

                //Load the updated/new Articles in the CMS.Domain.LoadingHashMaps using the getArticles method
                loadData(articleList, CMS.Domain.LoadingHashMaps.getInstance().getArticles());

                //Run the hashMapIntoTextFiles method with the string "articlesFile" and the Articles Hashmap.
                // This will convert the article HashMap data into a TextFile representation.
                CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("articlesFile", CMS.Domain.LoadingHashMaps.getInstance().getArticles());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//endregion}