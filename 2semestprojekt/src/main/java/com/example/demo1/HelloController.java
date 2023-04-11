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
import java.util.Objects;
import java.util.ResourceBundle;




public class HelloController implements Initializable{

    @FXML
    private WebView webView;
    @FXML
    private WebView webView2;
    @FXML
    private TextField searchBar;
    private WebEngine engine;
    private String url2;
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
        engine.load("https://www.komplett.dk/category/28003/hardware/pc-komponenter");

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

        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(";");
        sb.append(name);
        sb.append(";");
        sb.append(description);
        sb.append(";");
        sb.append(producer);
        sb.append(";");
        sb.append(price);
        sb.append(";");
        sb.append(picture);

        String data = new String(sb);
        Path p = Paths.get("src/main/data/" + id + ".txt");
        Files.createDirectories(p.getParent());
        File myObj = new File(String.valueOf(p));
        if (myObj.createNewFile()) {
            FileWriter myWriter = new FileWriter(String.valueOf(p));
            myWriter.write(data);
            myWriter.close();
        }

        searchBar.setText(""); // Clear the input TextField

        productList.getItems().add(data); // Add the new item to the ListView
        productList.refresh(); // Refresh the ListView

        create(id, name, description, producer, price, picture); // Generate the HTML file
    }

    public void create(String id, String name, String description, String producer, String price, String picture) throws IOException {
        String html = "<!DOCTYPE html>\n" +
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

        Path p = Paths.get("src/main/data/" + id + ".txt");
        File htmlFile = new File(String.valueOf(p));
        if (htmlFile.createNewFile()) {
            FileWriter myWriter = new FileWriter(String.valueOf(p));
            myWriter.write(html);
            myWriter.close();
        }
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

            Path filePath = Paths.get("src/main/data/" + previousID + ".txt");

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
            String read = Files.readAllLines(Paths.get(fileEntry.getPath())).get(0);
            files_arrayList.add(read.strip());
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


        //Make a part that gets the information out of the HTML-files.


        //This part copies the information in the previously mentioned ArrayList
        //into the previously mentioned ListView and refreshes/updates the ListView.

        for(String product: files_arrayList) {
            productList.getItems().add(product);
        }

        if(productList != null){
            productList.refresh();
        }
    }
}