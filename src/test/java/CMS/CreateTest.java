package CMS;

import CMS.Domain.Create;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CreateTest {
    private static final String THUMBNAIL_PATH = "src/main/data/Thumbnails/";

    @BeforeEach
    public void setup() {
        cleanUpThumbnailDirectory();
    }

    @AfterEach
    public void tearDown() {
        cleanUpThumbnailDirectory();
    }

    @Test
    public void testCreateProduct() {
        try {
            String name = "Test Product";
            String description = "This is a test product.";
            String stock = "100";
            String price = "99.99";
            String picture = "src/test/java/CMS/Test Pictures/Example_picture.png";
            int templateId = 1;

            String html = Create.create(name, description, price, stock, picture, templateId);

            System.out.println(html);

            assertTrue(html.contains("<h2>" + name + "</h2>"));
            assertTrue(html.contains("<p>" + description + "</p>"));
            assertTrue(html.contains("<p>Stock: " + stock + "</p>"));
            assertTrue(html.contains("<p>Price: $" + price + "</p>"));
            assertTrue(html.contains(picture.replace("\\", "/")));
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArticle() {
        try {
            String name = "Test Article";
            String subject = "Test Subject";
            String text = "This is a test article.";
            String picture = "src/test/java/CMS/Test Pictures/Example_picture.png";
            int templateId = 1;

            String html = Create.create(name, subject, text, picture, templateId);

            assertTrue(html.contains("<h1>" + subject + "</h1>"));
            assertTrue(html.contains(picture.replace("\\", "/")));
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    private void cleanUpThumbnailDirectory() {
        File thumbnailDirectory = new File(THUMBNAIL_PATH);
        File[] files = thumbnailDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}
