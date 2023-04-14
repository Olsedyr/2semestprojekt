package com.example.demo1;

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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class HelloController implements Initializable{

    @FXML
    private WebView webView;
    @FXML
    private WebView webView2;
    @FXML
    private TextField searchBar;
    private WebEngine engine;
    @FXML
    private ListView<String> productList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        engine.load("https://www.instructables.com/How-To-Replace-the-Processor-in-a-Desktop-Computer/");
    }

    @FXML
    protected void addItem() throws IOException {
        String inputText = searchBar.getText(); // Assuming searchBar is the TextField for input
        String[] inputFields = inputText.split(",");

        if (inputFields.length != 6) {
            // Show an error message or do nothing if the input format is incorrect
            return;
        }

        String id = inputFields[0].trim();
        String name = inputFields[1].trim();
        String description = inputFields[2].trim();
        String producer = inputFields[3].trim();
        String price = inputFields[4].trim();
        String picture = inputFields[5].trim();

        // Generate the List content
        String listRow = id + ";" + name + ";" + description + ";" + producer + ";" + price + ";" + picture;

        // Generate the HTML content
        String htmlContent = create(id, name, description, producer, price, picture);

        Path htmlFilePath = Paths.get("src/main/data/" + id + "_html.txt");
        File htmlFile = new File(String.valueOf(htmlFilePath));
        if (htmlFile.createNewFile()) {
            FileWriter myWriter = new FileWriter(String.valueOf(htmlFilePath));
            myWriter.write(htmlContent);
            myWriter.close();
        }

        searchBar.setText(""); // Clear the input TextField

        productList.getItems().add(listRow); // Add the new String sb to the ListView
        productList.refresh(); // Refresh the ListView
    }

    public String create(String id, String name, String description, String producer, String price, String picture) {
        String html =
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>" + name + "</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>" + name + "</h1>\n" +
                "<img src=\"" + picture + "\" alt=\"" + name + "\">\n" +
                "<p>Description: " + description + "</p>\n" +
                "<p>Producer: " + producer + "</p>\n" +
                "<p>Price: $" + price + "</p>\n" +
                "</body>\n" +
                "</html>";

        return html;
    }

    @FXML
    protected void deleteItem() throws IOException {
        //This gets what is selected in the UI's TreeTableView.
        ObservableList<Integer> selectedIndices = productList.getSelectionModel().getSelectedIndices();

        //This checks if what is selected is only one item.
        if (selectedIndices.size() == 1) {

            //This part gets the information from the selected index and changes it to the correct Path.

            String product = productList.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";")[0];
            Path filePath = Paths.get("src/main/data/" + previousID + "_html.txt");

            //This part converts the Path into a File and deletes it.

            File fileToDelete = new File(filePath.toString());
            fileToDelete.delete();

            //This part isn't entirely implemented yet.

            //This part loads the productList again and resets the search bar's text.

            loadProducts();
            //searchBar.setText("");
        }
    }

    //Understand and comment this method. This method is necessary for the next method.
    public static ArrayList<String> productFilesInFolder(final File folder) throws IOException {

        //This part makes an ArrayList of String and reads the first line of all the files in the given folder.
        //The read information is stripped of all white space before and after the actual information
        // ("  This  " --> "This")
        //and put into the ArrayList. Then the ArrayList is returned.
        Path dataPath = Paths.get("src", "main", "data");
        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
        }

        ArrayList<String> files_arrayList = new ArrayList<>();
        if (!folder.isDirectory()) {
            throw new IOException("Path is not a directory: " + folder.getPath());
        }
        File[] files = folder.listFiles();
        if (files == null) {
            throw new IOException("Unable to list files in the directory: " + folder.getPath());
        }
        for (final File fileEntry : files) {
            List<String> list = Files.readAllLines(Paths.get(fileEntry.getPath()));

            String read;

            String product_String = "";

            for(int i = 0; i < list.size(); i++){
                read = list.get(i);

                read = read.replace("\n", ";");

                if(i != 0){
                    product_String += ";";
                }
                product_String += read.strip();


            }

            files_arrayList.add(product_String);
        }
        return files_arrayList;
    }


    public void loadProducts() throws IOException {

        //This part gets the file path and makes an ArrayList of Strings
        //from the information in every file therein.
        Path filePath = Paths.get("src/main/data");

        final File folder = new File(String.valueOf(filePath));

        ArrayList<String> files_arrayList = productFilesInFolder(folder);

        //This part clears the information in ListView containing the product list.

        if(productList != null){
            productList.getItems().clear();
        }

        //Make a part that gets the information out of the HTML.txt-files.


        //This part copies the information in the previously mentioned ArrayList
        //into the previously mentioned ListView and refreshes/updates the ListView.

        for(String product: files_arrayList) {
            assert productList != null;
            productList.getItems().add(product);
        }

        if(productList != null){
            productList.refresh();
        }
    }

    private void webViewShowHtml(String productInfo) throws IOException {
        String[] productFields = productInfo.split(";");
        String id = productFields[0].trim();

        Path htmlFilePath = Paths.get("src/main/data/" + id + "_html.txt");
        String htmlContent = Files.readString(htmlFilePath);

        engine = webView.getEngine();
        engine.loadContent(htmlContent);
    }
}