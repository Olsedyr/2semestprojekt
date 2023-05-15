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
import java.util.*;

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

    private final static CMSController instance = new CMSController();
    public static CMSController getCMSController() {
        return instance;
    }

    String getProductPage(String id, String template_id){
        try {

            Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + "-" + template_id + ".txt");

            return Files.readString(htmlFilePath);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String getProductPage(String name, String description, double price, int stock, String id, File imageFile,
                          int template_id){

        String price_String = price + "";

        String stock_String = stock + "";

        String filepath = imageFile.getPath();
        try {
            String html = Create.create(name, description, price_String, stock_String, filepath, template_id);

            Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + "-" + template_id + ".txt");

            File htmlFile = new File(String.valueOf(htmlFilePath));

            if (!htmlFile.createNewFile()) {
                htmlFile.delete();
            }

            FileWriter myWriter = new FileWriter(String.valueOf(htmlFilePath));
            myWriter.write(html);
            myWriter.close();

            return html;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //This part loads our products when the application is started up.

        try {
            loadProducts();
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
        engine.load("https://www.instructables.com/How-To-Replace-the-Processor-in-a-Desktop-Computer/");
    }

    @FXML
    protected void addItem() throws IOException {

        //This part gets

        PopupWindow popupWindow = new PopupWindow();
        String str = popupWindow.getResult();

        if (str != null) {
            File htmlFile = new File(Paths.get("src/main/data/CMS/" + str.substring(0, str.indexOf(";"))
                    + ".txt").toString());

            if (!htmlFile.createNewFile()) {
                htmlFile.delete();
            }

            FileWriter myWriter = new FileWriter(String.valueOf(htmlFile));
            myWriter.write(str.substring(str.indexOf(";")));
            myWriter.close();

            loadProducts();
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
        ObservableList<Integer> selectedIndices = productList.getSelectionModel().getSelectedIndices();

        if (selectedIndices.size() == 1) {

            String product = productList.getItems().get(selectedIndices.get(0));
            String previousID = product.split(";")[0];

            PopupWindow popupWindow = new PopupWindow();
            String str = popupWindow.getResult();

            if (str != null) {
                Path filepath = Paths.get("src/main/data/CMS/" + previousID + ".txt");

                File fileToDelete = new File(filepath.toString());
                fileToDelete.delete();

                File newFile = new File(Paths.get("src/main/data/CMS/" + str.substring(0, str.indexOf(";"))
                        + ".txt").toString());

                FileWriter myWriter = new FileWriter(String.valueOf(newFile));
                myWriter.write(str.substring(str.indexOf(";")));
                myWriter.close();


                loadProducts();
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
        Elements png = document.select("img[src$=.png]");

        String png_link = png.toString();

        png_link = png_link.substring(png_link.indexOf("=") + 2);

        png_link = png_link.substring(0, png_link.indexOf(" ") - 1);


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


        //And this part removes the last bits of unnecessary information.

        string = string.substring(string.indexOf(";") + 1);

        string = string.substring(string.indexOf(";") + 1);


        //This part removes the last bits of the less useful information.

        cleanedHTML += string.replace("Description: ", "").replace("Price: ", "")
                .replace("Stock: ", "").replace("$", "")
                .replace("  ","").replace(" ;", ";");


        cleanedHTML += ";" + png_link;

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