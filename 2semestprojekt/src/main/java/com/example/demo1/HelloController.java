package com.example.demo1;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
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

    private WebEngine engine;

    private String url2;

    TreeItem allProductsTreeItem = new TreeItem();

    @FXML
    private TreeTableView productList = new TreeTableView<>(allProductsTreeItem) ;

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
    protected void deleteItem() throws IOException {
        //This gets what is selected in the UI's TreeTableView.
        ObservableList<Integer> selectedIndices = productList.getSelectionModel().getSelectedIndices();

        //This checks if what is selected is only one item.
        if (selectedIndices.size() == 1) {

            //This part gets the information from the selected index and changes it to the correct Path.

            String product = productList.getSelectionModel().getSelectedIndices().toString();

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
    public static ArrayList<String> listFilesInFolder(final File folder) throws IOException {
        ArrayList<String> files_arrayList = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String read = Files.readAllLines(Paths.get(fileEntry.getPath())).get(0);
            files_arrayList.add(read.strip());
        }
        return files_arrayList;
    }

    //Hvorvidt den her er korrekt er et godt spørgsmål. TreeTableViews er fucking besværlige.

    public void loadProducts() throws IOException {
        Path filePath = Paths.get("src/main/data");

        final File folder = new File(String.valueOf(filePath));

        ArrayList<String> files_arrayList = listFilesInFolder(folder);

        try{
            productList.getColumns().removeAll();
        } catch (NullPointerException ex){

        }

        for(int i = 0; i < files_arrayList.size(); i++) {

            String[] product_array = files_arrayList.get(i).split(";");

            for(int n = 0; n < product_array.length; n++){
                productList.getColumns().add(n, product_array[n]);
            }
        }

        productList.setRoot(allProductsTreeItem);

        //Skal det her overhovedet bruges?

        productList.refresh();
    }

}