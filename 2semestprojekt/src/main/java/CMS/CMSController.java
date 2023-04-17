package CMS;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
import java.util.Optional;
import java.util.ResourceBundle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

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
    private String result;

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
        String htmlContent = create(name, description, producer, price, picture);

        Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + ".txt");
        File htmlFile = new File(String.valueOf(htmlFilePath));
        if (htmlFile.createNewFile()) {
            FileWriter myWriter = new FileWriter(String.valueOf(htmlFilePath));
            myWriter.write(htmlContent);
            myWriter.close();
        }

        searchBar.setText(""); // Clear the input TextField

        boolean fileExists = false;

        for(String file: productList.getItems()){
            if(listRow.substring(0, listRow.indexOf(";")).equals(file.substring(0, listRow.indexOf(";"))) ){
                fileExists = true;
            }

        }

        if(!fileExists){
            productList.getItems().add(listRow); // Add the new String sb to the ListView
        }

        //productList.getItems().add(listRow); // Add the new String sb to the ListView
        productList.refresh(); // Refresh the ListView
    }

    public String create(String name, String description, String producer, String price, String picture) {
        String html =
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Name: " + name + "</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Name: " + name + "</h1>\n" +
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
            Path filePath = Paths.get("src/main/data/CMS/" + previousID + ".txt");

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
        Path dataPath = Paths.get("src/main/data/CMS/");
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

        for(int i = 0; i < files.length; i++){
            Path filePath = Paths.get(files[i].getPath());
            List<String> list = Files.readAllLines(filePath);

            String product_String = files[i].getName() + ";";

            for(int n = 0; n < list.size(); n++){
                product_String += list.get(n);
            }

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



    public void EditProduct() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Edit Product");

        ButtonType confirm = new ButtonType("Apply");
        dialog.getDialogPane().getButtonTypes().add(confirm);

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(30, 160, 20, 20));


        ObservableList<Integer> selectedIndices = productList.getSelectionModel().getSelectedIndices();

        //This checks if what is selected is only one item.
        if (selectedIndices.size() == 1) {

            //This part gets the information from the selected index and changes it to the correct Path.

            String product = productList.getItems().get(selectedIndices.get(0));

            //Gets the correct string
            String previousID = product.split(";")[0];

            String previousName = product.split(";")[1];

            String previousDescription = product.split(";")[2];

            String previousProducer = product.split(";")[3];

            String previousPrice = product.split(";")[4];

            String previousImage = product.split(";")[5];

            Path filePath = Paths.get("src/main/data/CMS/" + previousID + ".txt");


            //Sets all the labels and TextPrompts
            Label id = new Label();
            id.setText(previousID);

            TextField productName = new TextField();
            productName.setPromptText(previousName);

            TextField productDescription = new TextField();
            productDescription.setPromptText(previousDescription);

            TextField productProducer = new TextField();
            productProducer.setPromptText(previousProducer);

            TextField productPrice = new TextField();
            productPrice.setPromptText(previousPrice);

            TextField productImage = new TextField();
            productImage.setPromptText(previousImage);


            //Adds all labels and textPromts to the pop-up window
            grid.add(new Label("ID:"), 0, 0);
            grid.add(id, 1, 0);

            grid.add(new Label("Product Name:"), 0, 1);
            grid.add(productName, 1, 1);

            grid.add(new Label("Description:"), 0, 2);
            grid.add(productDescription, 1, 2);

            grid.add(new Label("Producer:"), 0, 3);
            grid.add(productProducer, 1, 3);

            grid.add(new Label("Price:"), 0, 4);
            grid.add(productPrice, 1, 4);

            grid.add(new Label("Image:"), 0, 5);
            grid.add(productImage, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirm) {

                    //Prints the new info if apply is clicked
                    System.out.println("Changed Product Info:" + "\n" +
                            "ID: " + previousID+ "\n" +
                            "Name: " +productName.getText() + "\n" +
                            "Description: " +productDescription.getText()+ "\n" +
                            "Producer: " + productProducer.getText()+ "\n" +
                            "Price: " + productPrice.getText() + "\n" +
                            "Image: " + productImage.getText());

                    return id.getText() + ";" + productName.getText() + ";" + productDescription.getText() + ";" + productProducer.getText()+ ";" + productPrice.getText()+ ";" + productImage.getText();
                }
                return null;
            });

            Optional<String> rslt = dialog.showAndWait();
            if (rslt.isPresent()) {
                this.result = rslt.get();
            } else this.result = null;
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

    public static String htmlToString(String html) {
        if(html==null)
            return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        String s = document.html().replaceAll("\\\\n", "\n");

        String id = s.substring(0, s.indexOf(";")).replace(".txt", "");

        s = s.replace("\n",";").replace("Name:", ";;").replace(";;",";").substring(s.indexOf(";") + 1);

        String name = s.substring(0, s.indexOf(";"));

        name = name.substring(0, (name.length()/2) + 1);

        s = id + name + s.substring(s.indexOf(";") + 1).replace("Description: ","")
                .replace("Producer: ", "").replace("Price: $", "").replace("; ", ";");

        return Jsoup.clean(s, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false));
    }
}