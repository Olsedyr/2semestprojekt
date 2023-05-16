package CMS;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

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

public class CMSInfoController implements Initializable {


    @FXML
    private ListView<String> articleList;

    @FXML
    private TextField page_id;

    @FXML
    private TextField subject_id;

    @FXML
    private TextField article;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      /*  try {
            loadProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }*/



    }

    @FXML
    protected void addItem() throws IOException {
        String id = page_id.getText();
        String name = subject_id.getText();
        String description = article.getText();
        String picture = "";

        // Generate the List content
        String listRow = id + ";" + name + ";" + description + ";"  + picture;

        // Generate the HTML content
        String htmlContent = create(name, description, picture);

        Path htmlFilePath = Paths.get("src/main/data/CMS/" + id + ".txt");
        File htmlFile = new File(String.valueOf(htmlFilePath));
        if (htmlFile.createNewFile()) {
            FileWriter myWriter = new FileWriter(String.valueOf(htmlFilePath));
            myWriter.write(htmlContent);
            myWriter.close();
        }


        boolean fileExists = false;

        for (String file : articleList.getItems()) {
            if (listRow.substring(0, listRow.indexOf(";")).equals(file.substring(0, listRow.indexOf(";")))) {
                fileExists = true;
            }

        }

        if (!fileExists) {
            articleList.getItems().add(listRow); // Add the new String sb to the ListView
        }

        articleList.refresh(); // Refresh the ListView
    }


    public String create(String name, String description, String picture) {
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
                        "</body>\n" +
                        "</html>";

        return html;
    }

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

        for (int i = 0; i < files.length; i++) {
            Path filePath = Paths.get(files[i].getPath());
            List<String> list = Files.readAllLines(filePath);

            String product_String = files[i].getName() + ";";

            for (int n = 0; n < list.size(); n++) {
                product_String += list.get(n);
            }

            files_arrayList.add(htmlToString(product_String));
        }
        return files_arrayList;
    }


    public static String htmlToString(String html) {
        if (html == null)
            return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        String s = document.html().replaceAll("\\\\n", "\n");

        String id = s.substring(0, s.indexOf(";")).replace(".txt", "");

        s = s.replace("\n", ";").replace("Name:", ";;").replace(";;", ";").substring(s.indexOf(";") + 1);

        String name = s.substring(0, s.indexOf(";"));

        name = name.substring(0, (name.length() / 2) + 1);

        s = id.replace(".txt", "") + name + s.substring(s.indexOf(";") + 1).replace("Description: ", "")
                .replace("Producer: ", "").replace("Price: $", "").replace("; ", ";");

        return Jsoup.clean(s, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false));
    }


    
    public void loadProducts() throws IOException {

        //This part gets the file path and makes an ArrayList of Strings
        //from the information in every file therein.
        Path filePath = Paths.get("src/main/data/CMS/");

        final File folder = new File(String.valueOf(filePath));

        ArrayList<String> files_arrayList = productFilesInFolder(folder);

        //This part clears the information in ListView containing the product list.

        if (articleList != null) {
            articleList.getItems().clear();
        }

        //Make a part that gets the information out of the HTML.txt-files.


        //This part copies the information in the previously mentioned ArrayList
        //into the previously mentioned ListView and refreshes/updates the ListView.

        for (String product : files_arrayList) {
            assert articleList != null;
            articleList.getItems().add(product);
        }

        if (articleList != null) {
            articleList.refresh();
        }
    }



    //This deletes all the info the user has written in the textfields.
    public void cancel(){
        page_id.setText("");
        subject_id.setText("");
        article.setText("");

    }






}
