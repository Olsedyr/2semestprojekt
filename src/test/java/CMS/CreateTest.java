package CMS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateTest {
    private static final String THUMBNAIL_PATH = "src/main/data/Thumbnails/";

    @BeforeEach
    public void setup() {
        // Remove thumbnails generated from previous tests
        File thumbnailDirectory = new File(THUMBNAIL_PATH);
        File[] files = thumbnailDirectory.listFiles();
        if (files != null) { // Ensure the list of files is not null before attempting to delete
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Test
    public void testCreateProduct() throws Exception {
        String name = "Test Product";
        String description = "This is a test product.";
        String stock = "100";
        String price = "99.99";
        String picture = "src/main/resources/images/test.jpg";
        int templateId = 1;

        String html = Create.create(name, description, stock, price, picture, templateId);

        assertTrue(html.contains(name));
        assertTrue(html.contains(description));
        assertTrue(html.contains(stock));
        assertTrue(html.contains(price));
        assertTrue(html.contains(picture.replace("\\", "/")));

        // Check if thumbnail file is created
        Path thumbnailPath = Paths.get(THUMBNAIL_PATH + templateId + "_thumbnail.txt");
        assertTrue(Files.exists(thumbnailPath));
    }

    @Test
    public void testCreateArticle() throws Exception {
        String name = "Test Article";
        String subject = "Test Subject";
        String text = "This is a test article.";
        String picture = "src/main/resources/images/test.jpg";
        int templateId = 1;

        String html = Create.create(name, subject, text, picture, templateId);

        assertTrue(html.contains(name));
        assertTrue(html.contains(picture.replace("\\", "/")));
    }
}
