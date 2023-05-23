package CMS;

import CMS.Domain.Create;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CreateTest {
    private static final String THUMBNAIL_PATH = "src/main/data/Thumbnails/";

    @BeforeEach
    public void setup() {
        // Cleans up the thumbnail directory.
        cleanUpThumbnailDirectory();
    }

    @AfterEach
    public void tearDown() {
        // Cleans up the thumbnail directory.
        cleanUpThumbnailDirectory();
    }

    // Tests if a product is properly created
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

            assertTrue(html.contains("<h2>" + name + "</h2>"));
            assertTrue(html.contains("<p>" + description + "</p>"));
            assertTrue(html.contains("<p>Stock: " + stock + "</p>"));
            assertTrue(html.contains("<p>Price: $" + price + "</p>"));
            assertTrue(html.contains(picture.replace("\\", "/")));

        } catch (Exception e) {
            // Call in the catch block to indicate the test has failed if any exception occurs.
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    // Tests if an article is properly created.
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
            // Call in the catch block to indicate the test has failed if any exception occurs.
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    // Deletes all the files in the thumbnail directory.
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
