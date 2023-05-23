package CMS.Domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShopAccessTest {

    @BeforeEach
    void setUp() {
    }
    @AfterEach
    void tearDown() {
    }

    //Checks if the ShopAccess singleton instance can be retrieved properly.
    @Test
    void getInstance() {
        assertTrue(CMS.Domain.ShopAccess.getInstance() instanceof ShopAccess);
    }


    // Checks if a product page can be retrieved properly when given a product name and a thumbnail ID.
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


    // Checks if a product page can be retrieved properly when given the full details of a product.
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


    // Checks if an article page can be retrieved properly when given an article ID.
    @Test
    void getArticlePage() {
        String html;

        File file = new File("src/test/java/CMS/Test Pictures/Example_picture.png");
        try {
            html = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>" + "getArticlePageTest" + " - " + "getArticlePageTest Subject" + "</title>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "<style>\n" +
                    "body {\n" +
                    "font-family: Arial, sans-serif;\n" +
                    "margin: 0;\n" +
                    "padding: 0;\n" +
                    "background-color: pink;\n" +
                    "}\n" +
                    "header {\n" +
                    "background-color: #c3272b;\n" +
                    "color: white;\n" +
                    "padding: 20px;\n" +
                    "text-align: center;\n" +
                    "}\n" +
                    "h1 {\n" +
                    "font-size: 36px;\n" +
                    "margin-bottom: 0;\n" +
                    "}\n" +
                    ".container {\n" +
                    "display: flex;\n" +
                    "flex-wrap: wrap;\n" +
                    "justify-content: center;\n" +
                    "align-items: flex-start;\n" +
                    "margin: 20px;\n" +
                    "}\n" +
                    ".product-info {\n" +
                    "background-color: #f2f2f2;\n" +
                    "border: 1px solid #ccc;\n" +
                    "border-radius: 5px;\n" +
                    "box-shadow: 0 2px 2px #ccc;\n" +
                    "display: flex;\n" +
                    "flex-wrap: wrap;\n" +
                    "margin: 10px;\n" +
                    "padding: 10px;\n" +
                    "width: 800px;\n" +
                    "}\n" +
                    ".product-info img {\n" +
                    "flex: 1 1 300px;\n" +
                    "margin: 0 auto;\n" +
                    "max-width: 100%;\n" +
                    "}\n" +
                    ".product-info .details {\n" +
                    "flex: 1 1 300px;\n" +
                    "margin: 0 20px;\n" +
                    "}\n" +
                    ".product-info h2 {\n" +
                    "font-size: 24px;\n" +
                    "margin: 10px 0;\n" +
                    "}\n" +
                    ".product-info p {\n" +
                    "font-size: 18px;\n" +
                    "margin: 10px 0;\n" +
                    "}\n" +
                    ".product-description {\n" +
                    "margin: 20px;\n" +
                    "width: 800px;\n" +
                    "}\n" +
                    ".product-description h2 {\n" +
                    "font-size: 24px;\n" +
                    "margin: 10px 0;\n" +
                    "}\n" +
                    ".product-description p {\n" +
                    "font-size: 18px;\n" +
                    "margin: 10px 0;\n" +
                    "}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<header>\n" +
                    "<h1>" + "getArticlePageTest Subject" + "</h1>\n" +
                    "</header>\n" +
                    "<div class=\"container\">\n" +
                    "<div class=\"product-info\">\n" +
                    "<img src=\"" + file.getAbsolutePath().replace("\\", "/") + "\" alt=\"Image of " + "getArticlePageTest" + "\">\n" +
                    "<div class=\"details\">\n" +
                    "<h2>" + "getArticlePageTest Subject" + "</h2>\n" +
                    "<p>" + "getArticlePageTest text" + "</p>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";

            File newFile = new File(Paths.get("src/main/data/ARTICLES/" + "getArticlePageTest---1"
                    + ".txt").toString());

            FileWriter myWriter = new FileWriter(String.valueOf(newFile));
            myWriter.write(html);
            myWriter.close();

            CMS.Domain.LoadingHashMaps.getInstance().getArticles().put("getArticlePageTest---1", "getArticlePageTest---1"
                    + ";;" + "getArticlePageTest Subject" + ";;" + "getArticlePageTest text" + ";;"
                    + (file.getAbsolutePath()).replace("\\", "/"));


            CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("articlesFile",  CMS.Domain.LoadingHashMaps.getInstance().getArticles());;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        boolean htmlTest = html.equals(CMS.Domain.ShopAccess.getInstance().getArticlePage("getArticlePageTest---1").getValue());

        assertTrue(htmlTest);
    }


    // Checks if multiple article pages can be retrieved properly.
    @Test
    void getArticlePages() {
        String html;

        File file = new File("src/test/java/CMS/Test Pictures/Example_picture.png");
        try {
            html = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>" + "getArticlePagesTest" + " - " + "getArticlePagesTest Subject" + "</title>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "<style>\n" +
                    "body {\n" +
                    "font-family: Arial, sans-serif;\n" +
                    "margin: 0;\n" +
                    "padding: 0;\n" +
                    "background-color: pink;\n" +
                    "}\n" +
                    "header {\n" +
                    "background-color: #c3272b;\n" +
                    "color: white;\n" +
                    "padding: 20px;\n" +
                    "text-align: center;\n" +
                    "}\n" +
                    "h1 {\n" +
                    "font-size: 36px;\n" +
                    "margin-bottom: 0;\n" +
                    "}\n" +
                    ".container {\n" +
                    "display: flex;\n" +
                    "flex-wrap: wrap;\n" +
                    "justify-content: center;\n" +
                    "align-items: flex-start;\n" +
                    "margin: 20px;\n" +
                    "}\n" +
                    ".product-info {\n" +
                    "background-color: #f2f2f2;\n" +
                    "border: 1px solid #ccc;\n" +
                    "border-radius: 5px;\n" +
                    "box-shadow: 0 2px 2px #ccc;\n" +
                    "display: flex;\n" +
                    "flex-wrap: wrap;\n" +
                    "margin: 10px;\n" +
                    "padding: 10px;\n" +
                    "width: 800px;\n" +
                    "}\n" +
                    ".product-info img {\n" +
                    "flex: 1 1 300px;\n" +
                    "margin: 0 auto;\n" +
                    "max-width: 100%;\n" +
                    "}\n" +
                    ".product-info .details {\n" +
                    "flex: 1 1 300px;\n" +
                    "margin: 0 20px;\n" +
                    "}\n" +
                    ".product-info h2 {\n" +
                    "font-size: 24px;\n" +
                    "margin: 10px 0;\n" +
                    "}\n" +
                    ".product-info p {\n" +
                    "font-size: 18px;\n" +
                    "margin: 10px 0;\n" +
                    "}\n" +
                    ".product-description {\n" +
                    "margin: 20px;\n" +
                    "width: 800px;\n" +
                    "}\n" +
                    ".product-description h2 {\n" +
                    "font-size: 24px;\n" +
                    "margin: 10px 0;\n" +
                    "}\n" +
                    ".product-description p {\n" +
                    "font-size: 18px;\n" +
                    "margin: 10px 0;\n" +
                    "}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<header>\n" +
                    "<h1>" + "getArticlePagesTest Subject" + "</h1>\n" +
                    "</header>\n" +
                    "<div class=\"container\">\n" +
                    "<div class=\"product-info\">\n" +
                    "<img src=\"" + file.getAbsolutePath().replace("\\", "/") + "\" alt=\"Image of " + "getArticlePagesTest" + "\">\n" +
                    "<div class=\"details\">\n" +
                    "<h2>" + "getArticlePagesTest Subject" + "</h2>\n" +
                    "<p>" + "getArticlePagesTest text" + "</p>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";

            File newFile = new File(Paths.get("src/main/data/ARTICLES/" + "getArticlePagesTest---1"
                    + ".txt").toString());

            FileWriter myWriter = new FileWriter(String.valueOf(newFile));
            myWriter.write(html);
            myWriter.close();

            CMS.Domain.LoadingHashMaps.getInstance().getArticles().put("getArticlePagesTest---1", "getArticlePagesTest---1"
                    + ";;" + "getArticlePagesTest Subject" + ";;" + "getArticlePagesTest text" + ";;"
                    + (file.getAbsolutePath()).replace("\\", "/"));

            CMS.Domain.LoadingHashMaps.getInstance().hashMapIntoTextFiles("articlesFile",  CMS.Domain.LoadingHashMaps.getInstance().getArticles());;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean keyTest = html.equals(CMS.Domain.ShopAccess.getInstance().getArticlePages().get("getArticlePagesTest---1"));

        assertTrue(keyTest);
    }


    // Checks if a thumbnail can be retrieved properly when given a thumbnail ID.
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


    // Checks if multiple thumbnails can be retrieved properly.
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