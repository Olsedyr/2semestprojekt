package CMS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateTest {
    private String name;
    private String description;
    private String producer;
    private String price;
    private String picture;
    private int template_id;

    @BeforeEach
    public void setUp() {
        name = "Test Product";
        description = "This is a test product";
        producer = "Test Producer";
        price = "100";
        picture = "test.jpg";
        template_id = 1;
    }

    @Test
    public void createTest() throws Exception {
        String html = Create.create(name, description, producer, price, picture, template_id);
        assertTrue(html.contains(name));
        assertTrue(html.contains(description));
        assertTrue(html.contains(producer));
        assertTrue(html.contains(price));
        assertTrue(html.contains(picture));
    }

    @Test
    public void createThumbnailTest() {
        Create.createThumbnail(String.valueOf(template_id), name, price, picture);

        // Now we need to check if the file was created and if it contains the right data
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/data/CMS/" + template_id + "_thumbnail.txt"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String fileContent = sb.toString();
            assertTrue(fileContent.contains(name));
            assertTrue(fileContent.contains(price));
            assertTrue(fileContent.contains(picture));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
