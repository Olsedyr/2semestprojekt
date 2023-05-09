package CMS;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

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
    private Label id;

    @FXML
    private TextField name;

    @FXML
    private TextField description;

    @FXML
    private TextField producer;

    @FXML
    private TextField price;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //loads our products when the application is started up.

        try {
            loadProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Ensures that we can load our HTML-files.

        engine = webView.getEngine();

        //Xinyu, please comment this.

        productList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    webViewShowHtml(newValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Why do we have 2 WebViews?

        engine = webView2.getEngine();
        engine.load("https://www.instructables.com/How-To-Replace-the-Processor-in-a-Desktop-Computer/");
    }

    @FXML
    protected void addItem() throws IOException {
        // Assuming searchBar is the TextField for input, this get the text in the search bar.
        String inputText = searchBar.getText();

        // This splits the text up into smaller parts that can be put into the appropriate places.
        String[] inputFields = inputText.split(",");

        // This shows an error message and returns nothing if the input format is incorrect.
        if (inputFields.length != 7) {
            System.out.println("The given input is not the correct lenght or format. Please try again.");
            return;
        }

        // And the split info is put into different Strings to be placed.

        String id = inputFields[0].trim();
        String name = inputFields[1].trim();
        String description = inputFields[2].trim();
        String producer = inputFields[3].trim();
        String price = inputFields[4].trim();
        String picture = inputFields[5].trim();
        String template_id = inputFields[6].trim();

        // This part generates new content in the productList.
        String listRow = id + "-" + template_id + ";" + name + ";" + description + ";" + producer + ";" + price + ";" + picture + ";";

        // This part generates the HTML content.
        String htmlContent = null;
        try {
            htmlContent = Create.create(name, description, producer, price, picture, Integer.parseInt(template_id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //This part puts the HTML content into a file.

        Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + "-" + template_id + ".txt");
        File htmlFile = new File(String.valueOf(htmlFilePath));
        if (htmlFile.createNewFile()) {
            FileWriter myWriter = new FileWriter(String.valueOf(htmlFilePath));
            myWriter.write(htmlContent);
            myWriter.close();
        }

        //This part resets the search bar.

        searchBar.setText("");

        //This part ensures that no file with the same id can be made.

        boolean fileExists = false;

        for(String file: productList.getItems()){
            if(listRow.substring(0, listRow.indexOf(";")).equals(file.substring(0, listRow.indexOf(";"))) ){
                fileExists = true;
            }

        }

        // This part adds the new String sb to the ListView.

        if(!fileExists){
            productList.getItems().add(listRow);
        }

        //And this part refreshes the Listview.

        productList.refresh(); // Refresh the ListView
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
            String previousTemplate_id = product.split(";")[6];

            Path filePath = Paths.get("src/main/data/CMS/" + previousID + "-" + previousTemplate_id + ".txt");

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
        ObservableList<Integer> selectedIndices = productList.getSelectionModel().getSelectedIndices();

        if (selectedIndices.size() == 1) {

            String product = productList.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";")[0];
            String previousTemplateID;
            try {
                previousTemplateID = product.split(";")[6];
            } catch (ArrayIndexOutOfBoundsException e) {
                previousTemplateID = product.split(";")[5];;
            }

            EditProduct ep = new EditProduct();
            String str = ep.getResult();

            if (str != null) {
                Path filepath = Paths.get("src/main/data/CMS/" + previousID + ".txt");

                File fileToDelete = new File(filepath.toString());
                fileToDelete.delete();

                File newFile = new File(Paths.get("src/main/data/CMS/" + str.substring(0, str.indexOf(";"))
                        + ".txt").toString());

                if(newFile.createNewFile()) {
                    FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                    myWriter.write(str.substring(str.indexOf(";")));
                    myWriter.close();
                }

                loadProducts();
            }
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

        //This part copies the information in the previously mentioned ArrayList
        //into the previously mentioned ListView.

        for(String product: files_arrayList) {
            assert productList != null;
            productList.getItems().add(product);
        }

        //This part refreshes/updates the ListView

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

    //This function gets the information from the HTML files, so the
    //products in the ListView looks the same,
    //no matter if they were just created or are from old files,
    //before shutting down the application and starting it up again.
    public static String htmlToString(String html) {
        if(html==null){
            return html;
        }


        Document document = Jsoup.parse(html);
        Elements png = document.select("img[src$=.png]");

        String png_link = png.toString();

        png_link = png_link.substring(png_link.indexOf("=") + 2);

        png_link = png_link.substring(0, png_link.indexOf(" ") - 1);


        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");

        //This part ensures that the ListView gets the right information by
        //cleaning, removing and replacing a lot of information.

        String s = document.html().replaceAll("\\\\n", "\n");


        String id = s.substring(s.indexOf("-") - 1, s.indexOf(";")).replace(".txt", "");


        String string = Jsoup.clean(s, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false));

        string = string.replace("\n", ";").replace(";;", ";");


        String name = string.substring(string.indexOf(";") + 1);

        name = name.substring(name.indexOf(":") + 1);

        name = name.substring(name.indexOf(":") + 2);


        String cleanedHTML = id + ";" + name + ";";

        //This part puts the cleaned up information from the HTML together in the right way.

        ArrayList<String> lines = new ArrayList<>();

        string.lines().forEach(lines::add);

        for(int i = 1; i < lines.size(); i++){
            cleanedHTML += lines.get(i) + ";";
        }

        cleanedHTML += png_link;

        //And this part removes the last bits of unnecessary information.

        cleanedHTML = cleanedHTML.replace("Description: ", "").replace("Producer: ", "")
                .replace("Price: ", "").replace("$", "");

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

            //This part clears the ListView and shows the results.

            productList.getItems().clear();
            if (results.size() > 0) {
                for(String found_product: results) productList.getItems().add(found_product);
                productList.refresh();
            }
        }
    }
}