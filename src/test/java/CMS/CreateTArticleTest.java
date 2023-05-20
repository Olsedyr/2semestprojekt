package CMS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateTArticleTest {

    private String name;

    private String subject;

    private String text;

    private String picture;

    private int template_id;

    @BeforeEach
    void setUp() {
        name = "test name";
        subject = "test subject";
        text = "test text";
        picture = "test picture";
        template_id = 1;
    }

    @Test
    void create() throws Exception {
        String html = Create.create(name, subject, text, picture, template_id);
        assertTrue(html.contains(name));
        assertTrue(html.contains(subject));
        assertTrue(html.contains(text));
        assertTrue(html.contains(picture));
    }
}