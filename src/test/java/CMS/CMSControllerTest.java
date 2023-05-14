package CMS;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CMSControllerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCMSController() {
        assertTrue(CMSController.getCMSController() instanceof CMSController);
    }

    @Test
    void getProductPageTest1() {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "  <title>Example Product</title>\n" +
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
                "<img src=\"https://example.com/image.png\" alt=\"Example Product\">\n" +
                "  <h1>Example Product</h1>\n" +
                "\n" +
                "  <h2>Product Description:</h2>\n" +
                "  <p>\n" +
                "This is an example product  </p>\n" +
                "\n" +
                "  <h2>Price:</h2>\n" +
                "  <p>\n" +
                "$49.99  </p>\n" +
                "\n" +
                "  <h2>Stock:</h2>\n" +
                "  <p>\n" +
                "5  </p>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        boolean htmlTest = CMSController.getCMSController().getProductPage("ProductPageTest1", "1").equals(html.replace("\n", System.getProperty("line.separator")));

        assertTrue(htmlTest);
    }

    @Test
    void getProductPageTest2() {
    }

    @Test
    void addItem() {
        //This would be very difficult to make a test for, given that it uses the ListView's selected indexes.
    }

    @Test
    void deleteItem() {
        //This would be very difficult to make a test for, given that it uses the ListView's selected indexes.
    }

    @Test
    void editProduct() {
        //This would be very difficult to make a test for, given that it uses the ListView's selected indexes.
    }

    @Test
    void productFilesInFolder() {

        Path filePath = Paths.get("src/test/java/CMS/Test Files");

        final File folder = new File(String.valueOf(filePath));

        String testString = "productFilesInFolderTest-1;Example Product;This is an example product;49.99;5;https://example.com/image.png";

        try {
            boolean htmlTest = CMSController.productFilesInFolder(folder).get(0).equals(testString.replace("\n", System.getProperty("line.separator")));

            assertTrue(htmlTest);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadProducts() {

        //CMSController.getCMSController().getProductList();
    }

    @Test
    void htmlToString() {
        String testString = "productFilesInFolderTest-1;Example Product;This is an example product;49.99;5;https://example.com/image.png";

        String htmlTestString ="productFilesInFolderTest-1;" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "  <title>Example Product</title>\n" +
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
                "<img src=\"https://example.com/image.png\" alt=\"Example Product\">\n" +
                "  <h1>Example Product</h1>\n" +
                "\n" +
                "  <h2>Product Description:</h2>\n" +
                "  <p>\n" +
                "This is an example product  </p>\n" +
                "\n" +
                "  <h2>Price:</h2>\n" +
                "  <p>\n" +
                "$49.99  </p>\n" +
                "\n" +
                "  <h2>Stock:</h2>\n" +
                "  <p>\n" +
                "5  </p>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        //System.out.println(CMSController.getCMSController().htmlToString("productFilesInFolderTest-1;" + htmlTestString));
        boolean test = testString.equals(CMSController.getCMSController().htmlToString(htmlTestString.replace("\n", System.getProperty("line.separator"))));

        assertTrue(test);
    }

    @Test
    void searchProducts() {
    }
}