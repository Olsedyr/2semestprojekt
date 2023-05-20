package CMS.Domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
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
    }

    @Test
    void getProductPageTest1() {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>" + "ProductPageTest1" + " - Product Page</title>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<style>\n" +
                "body {\n" +
                "font-family: Arial, sans-serif;\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "}\n" +
                "header {\n" +
                "background-color: #333;\n" +
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
                "<h1>" + "ProductPageTest1" + " - Product Page</h1>\n" +
                "</header>\n" +
                "<div class=\"container\">\n" +
                "<div class=\"product-info\">\n" +
                "<img class=\"productImage\" alt=\"Image of Product\" src=\"file:///"
                + "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/") + "\">\n" +
                "<div class=\"details\">\n" +
                "<h2>" + "ProductPageTest1" + "</h2>\n" +
                "<p>Price: $" + "49.99" + "</p>\n" +
                "<p>Stock: " + "5" + "</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"product-description\">\n" +
                "<h2>Product Description</h2>\n" +
                "<p>" + "ProductPageTest1 description" + "</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";


        boolean htmlTest = CMS.Domain.ShopAccess.getInstance().getProductPage("ProductPageTest1", "1").equals(html.replace(System.getProperty("line.separator"), "\n"));

        assertTrue(htmlTest);
    }

    @Test
    void getProductPageTest2() {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>" + "ProductPageTest1" + " - Product Page</title>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<style>\n" +
                "body {\n" +
                "font-family: Arial, sans-serif;\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "}\n" +
                "header {\n" +
                "background-color: #333;\n" +
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
                "<h1>" + "ProductPageTest1" + " - Product Page</h1>\n" +
                "</header>\n" +
                "<div class=\"container\">\n" +
                "<div class=\"product-info\">\n" +
                "<img class=\"productImage\" alt=\"Image of Product\" src=\"file:///"
                + "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/") + "\">\n" +
                "<div class=\"details\">\n" +
                "<h2>" + "ProductPageTest1" + "</h2>\n" +
                "<p>Price: $" + "49.99" + "</p>\n" +
                "<p>Stock: " + "5" + "</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"product-description\">\n" +
                "<h2>Product Description</h2>\n" +
                "<p>" + "ProductPageTest1 description" + "</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";


        Path filePath = Paths.get("src/test/java/CMS/Test Pictures/Example_picture.png");

        File file = new File(String.valueOf(filePath));


        boolean htmlTest = CMS.Domain.ShopAccess.getInstance().getProductPage("ProductPageTest1", "ProductPageTest1 description", 49.99, 5,"ProductPageTest1", file, 1).equals(html.replace(System.getProperty("line.separator"), "\n"));

        assertTrue(htmlTest);
    }

    @Test
    void getArticlePage() {
    }

    @Test
    void getArticlePages() {
    }

    @Test
    void getThumbnail() {
    }

    @Test
    void getThumbnails() {
    }
}