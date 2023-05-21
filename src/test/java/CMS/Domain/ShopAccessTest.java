package CMS.Domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
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
    }

    @Test
    void getProductPageTest1() {
        String html;

        File file = new File("C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"));
        try {

            CMS.Domain.ShopAccess.getInstance().getProductPage("ProductPageTest1", "ProductPageTest1 description", 49.99, 5,"ProductPageTest1",
                    file, 1);
            html = Create.create("ProductPageTest1", "ProductPageTest1 description", "49.99", "5",
                    "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"), 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        boolean htmlTest = CMS.Domain.ShopAccess.getInstance().getProductPage("ProductPageTest1", "1").equals(html.replace(System.getProperty("line.separator"), "\n"));

        assertTrue(htmlTest);
    }

    @Test
    void getProductPageTest2() {
        String html;
        try {
            html = Create.create("ProductPageTest2", "ProductPageTest2 description", "49.99", "5",
                    "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"), 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        Path filePath = Paths.get("src/test/java/CMS/Test Pictures/Example_picture.png");

        File file = new File(String.valueOf(filePath));


        boolean htmlTest = CMS.Domain.ShopAccess.getInstance().getProductPage("ProductPageTest2", "ProductPageTest2 description", 49.99, 5,"ProductPageTest2", file, 1).equals(html.replace(System.getProperty("line.separator"), "\n"));

        assertTrue(htmlTest);
    }

    @Test
    void getArticlePage() {
        String html;
        try {
            html = Create.create("getArticlePageTest", "getArticlePageTest Subject", "getArticlePageTest description",
                    "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"), 1);
            CMS.Domain.LoadingHashMaps.getInstance().getArticles().put("getArticlePageTest", html);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean htmlTest = CMS.Domain.LoadingHashMaps.getInstance().getArticles().get("getArticlePageTest")
                .equals(CMS.Domain.ShopAccess.getInstance().getArticlePage("getArticlePageTest").getValue());

        assertTrue(htmlTest);
    }

    @Test
    void getArticlePages() {
    }

    @Test
    void getThumbnail() {
        Path htmlFilePath = Paths.get("C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"));
        try {
            File file = new File(String.valueOf(htmlFilePath));
            CMS.Domain.ShopAccess.getInstance().getProductPage("getThumbnailTest", "getThumbnailTest description", 9.99,5, "getThumbnailTest",
                    file, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        boolean htmlTest = CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().get("getThumbnailTest_thumbnail")
                .equals(CMS.Domain.ShopAccess.getInstance().getThumbnail("getThumbnailTest_thumbnail").getValue());

        assertTrue(htmlTest);
    }

    @Test
    void getThumbnails() {
        Path htmlFilePath = Paths.get("C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"));

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