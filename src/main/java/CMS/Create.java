package CMS;

import java.io.FileNotFoundException;

public class Create {
    public static String create(String name, String description, String producer, String price,
                                String picture, int template_id) throws Exception{
        String html;
        switch (template_id){
            case 1:
            html = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<title>Name: " + name + "</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<h1>Name: " + name + "</h1>\n" +
                            "<img src=\"" + picture + "\" alt=\"" + name + "\">\n" +
                            "<p>Description: " + description + "</p>\n" +
                            "<p>Producer: " + producer + "</p>\n" +
                            "<p>Price: $" + price + "</p>\n" +
                            "</body>\n" +
                            "</html>";
            break;
            default:
                System.out.println("A template with this id doesn't exist.");
                
                throw new Exception();
        }

        return html;
    }
}
