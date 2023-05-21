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


            Create.createThumbnail("ProductPageTest1", "ProductPageTest1 Name", "49.99",
                    "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"));
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


            Create.createThumbnail("ProductPageTest2", "ProductPageTest2 Name", "49.99",
                    "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"));
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
        try {
            Create.createThumbnail("getThumbnailTest", "getThumbnailTest Name", "9.99",
                    "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"));

            Path htmlFilePath = Paths.get("src/main/data/Thumbnails/" + "getThumbnailTest_thumbnail" + ".txt");
            String htmlContent = Files.readString(htmlFilePath);

            CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().put("getThumbnailTest", htmlContent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean htmlTest = CMS.Domain.LoadingHashMaps.getInstance().getThumbnails().get("getThumbnailTest_thumbnail")
                .equals(CMS.Domain.ShopAccess.getInstance().getThumbnail("getThumbnailTest_thumbnail").getValue());

        assertTrue(htmlTest);
    }

    @Test
    void getThumbnails() {

        try {
            Create.create("getThumbnailTest", "getThumbnailTest Name", "9.99","5",
                    "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"), 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Create.createThumbnail("getThumbnailTest", "getThumbnailTest Name", "9.99",
                "C:\\Users\\patri\\OneDrive\\Dokumenter\\GitHub\\2semestprojekt\\src\\test\\java\\CMS\\Test Pictures\\Example_picture.png".replace("\\", "/"));

        CMS.Domain.LoadingHashMaps.getInstance().textFilesIntoHashMaps();

        Path htmlFilePath = Paths.get("src/main/data/Thumbnails/" + "getThumbnailTest_thumbnail" + ".txt");

        String htmlContent;

        try {
            htmlContent = Files.readString(htmlFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Debugging code.

        System.out.println(CMS.Domain.ShopAccess.getInstance().getThumbnails().containsKey("getThumbnailTest"));

        System.out.println(CMS.Domain.ShopAccess.getInstance().getThumbnails().containsKey("getThumbnailTest_thumbnail"));

        //More debugging code.

        char[] fileArray = CMS.Domain.ShopAccess.getInstance().getThumbnails().get("getThumbnailTest_thumbnail").toCharArray();

        char[] testArray = htmlContent.replace(System.getProperty("line.separator"), "\n").toCharArray();

        int count = 999999;

        for(int i = 0; i < fileArray.length; i++){
            if(fileArray[i] != testArray[i] && count == 999999){
                System.out.println(i);

                System.out.println("Function");

                for(int n = 0; n < 20; n++){
                    System.out.println(fileArray[i - 5 + n]);
                }

                System.out.println("xxxxxxxx");

                System.out.println("Test");

                for(int n = 0; n < 20; n++){
                    System.out.println(testArray[i - 5 + n]);
                }

                count = i;
            }
        }

        boolean htmlTest = htmlContent.equals(CMS.Domain.ShopAccess.getInstance().getThumbnails().get("getThumbnailTest"));

        assertTrue(htmlTest);
    }
}