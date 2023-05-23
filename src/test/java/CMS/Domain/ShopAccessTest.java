package CMS.Domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ShopAccessTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
        assertTrue(CMS.Domain.ShopAccess.getInstance() instanceof ShopAccess);
    }

    @Test
    void getProductPageTest1() {
        String html;

        File file = new File("src/test/java/CMS/Test Pictures/Example_picture.png");
        try {

            CMS.Domain.ShopAccess.getInstance().getProductPage("ProductPageTest1", "ProductPageTest1 description", 49.99, 5,"ProductPageTest1",
                    file, 1);

            //This creates the "1_thumbnail.txt"-file.
            html = Create.create("ProductPageTest1", "ProductPageTest1 description", "49.99", "5",
                    file.getAbsolutePath(), 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        boolean htmlTest = CMS.Domain.ShopAccess.getInstance().getProductPage("ProductPageTest1", "1").equals(html.replace(System.getProperty("line.separator"), "\n"));

        assertTrue(htmlTest);
    }

    @Test
    void getProductPageTest2() {
        String html;

        File file = new File("src/test/java/CMS/Test Pictures/Example_picture.png");
        try {
            html = Create.create("ProductPageTest2", "ProductPageTest2 description", "49.99", "5",
                    file.getAbsolutePath(), 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean htmlTest = CMS.Domain.ShopAccess.getInstance().getProductPage("ProductPageTest2", "ProductPageTest2 description", 49.99, 5,"ProductPageTest2", file, 1).equals(html.replace(System.getProperty("line.separator"), "\n"));

        assertTrue(htmlTest);
    }

    @Test
    void getArticlePage() {
        String html;

        File file = new File("src/test/java/CMS/Test Pictures/Example_picture.png");
        try {
            html = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "  <title>"+ "getArticlePageTest" +"</title>\n" +
                    "  <style>\n" +
                    "    body {\n" +
                    "      font-family: Arial, sans-serif;\n" +
                    "      margin: 0;\n" +
                    "      padding: 20px;\n" +
                    "    }\n" +
                    "\n" +
                    "    h1 {\n" +
                    "      color: #333;\n" +
                    "    }\n" +
                    "\n" +
                    "    h2 {\n" +
                    "      color: #555;\n" +
                    "      margin-top: 30px;\n" +
                    "    }\n" +
                    "\n" +
                    "    p {\n" +
                    "      color: #444;\n" +
                    "      line-height: 1.6;\n" +
                    "    }\n" +
                    "\n" +
                    "    a {\n" +
                    "      color: #007bff;\n" +
                    "      text-decoration: none;\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "<img src=\"" + (file.getAbsolutePath()).replace("\\", "/") + "\" alt=\"" + "getArticlePageTest" + "\">\n" +
                    "  <h1>" + "getArticlePageTest" + "</h1>\n" +
                    "\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";

            File newFile = new File(Paths.get("src/main/data/ARTICLES/" + "getArticlePageTest---1"
                    + ".txt").toString());

            FileWriter myWriter = new FileWriter(String.valueOf(newFile));
            myWriter.write(html);
            myWriter.close();

            CMS.Domain.LoadingHashMaps.getInstance().getArticles().put("getArticlePageTest---1", "getArticlePageTest---1"
                    + ";;" + "getArticlePageTest Subject" + ";;" + "getArticlePageTest description" + ";;"
                    + (file.getAbsolutePath()).replace("\\", "/"));


            CMS.Domain.LoadingHashMaps.getInstance().hashMapArticlesIntoTextFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean htmlTest = html.equals(CMS.Domain.ShopAccess.getInstance().getArticlePage("getArticlePageTest---1").getValue());

        assertTrue(htmlTest);
    }

    @Test
    void getArticlePages() {
        String html;

        File file = new File("src/test/java/CMS/Test Pictures/Example_picture.png");
        try {
            html = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "\n" +
                    "<head>\n" +
                    "  <title>"+ "getArticlePagesTest" +"</title>\n" +
                    "  <style>\n" +
                    "    body {\n" +
                    "      font-family: Arial, sans-serif;\n" +
                    "      margin: 0;\n" +
                    "      padding: 20px;\n" +
                    "    }\n" +
                    "\n" +
                    "    h1 {\n" +
                    "      color: #333;\n" +
                    "    }\n" +
                    "\n" +
                    "    h2 {\n" +
                    "      color: #555;\n" +
                    "      margin-top: 30px;\n" +
                    "    }\n" +
                    "\n" +
                    "    p {\n" +
                    "      color: #444;\n" +
                    "      line-height: 1.6;\n" +
                    "    }\n" +
                    "\n" +
                    "    a {\n" +
                    "      color: #007bff;\n" +
                    "      text-decoration: none;\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "<img src=\"" + (file.getAbsolutePath()).replace("\\", "/") + "\" alt=\"" + "getArticlePagesTest" + "\">\n" +
                    "  <h1>" + "getArticlePagesTest" + "</h1>\n" +
                    "\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";

            File newFile = new File(Paths.get("src/main/data/ARTICLES/" + "getArticlePagesTest---1"
                    + ".txt").toString());

            FileWriter myWriter = new FileWriter(String.valueOf(newFile));
            myWriter.write(html);
            myWriter.close();

            CMS.Domain.LoadingHashMaps.getInstance().getArticles().put("getArticlePagesTest---1", "getArticlePagesTest---1"
                    + ";;" + "getArticlePagesTest Subject" + ";;" + "getArticlePagesTest description" + ";;"
                    + (file.getAbsolutePath()).replace("\\", "/"));

            CMS.Domain.LoadingHashMaps.getInstance().hashMapArticlesIntoTextFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean keyTest = html.equals(CMS.Domain.ShopAccess.getInstance().getArticlePages().get("getArticlePagesTest---1"));

        assertTrue(keyTest);
    }

    @Test
    void getThumbnail() {
        Path htmlFilePath = Paths.get("src/test/java/CMS/Test Pictures/Example_picture.png");
        Path FilePath = Paths.get("src/main/data/Thumbnails/" + "getThumbnailTest_thumbnail" + ".txt");
        String htmlContent;
        try {
            File file = new File(String.valueOf(htmlFilePath));
            CMS.Domain.ShopAccess.getInstance().getProductPage("getThumbnailTest", "getThumbnailTest description", 99.99,55, "getThumbnailTest",
                    file, 1);

            htmlContent = Files.readString(FilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        boolean htmlTest = htmlContent.equals(CMS.Domain.ShopAccess.getInstance().getThumbnail("getThumbnailTest_thumbnail").getValue());

        assertTrue(htmlTest);
    }

    @Test
    void getThumbnails() {
        Path htmlFilePath = Paths.get("src/test/java/CMS/Test Pictures/Example_picture.png");

        Path FilePath = Paths.get("src/main/data/Thumbnails/" + "getThumbnailsTest_thumbnail" + ".txt");
        String htmlContent;
        try {
            File file = new File(String.valueOf(htmlFilePath));
            CMS.Domain.ShopAccess.getInstance().getProductPage("getThumbnailsTest", "getThumbnailsTest description", 9.99,5, "getThumbnailsTest",
                    file, 1);

            htmlContent = Files.readString(FilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean htmlTest = htmlContent.equals(CMS.Domain.ShopAccess.getInstance().getThumbnails().get("getThumbnailsTest_thumbnail"));

        assertTrue(htmlTest);
    }
}