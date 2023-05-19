package CMS;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Create {
    public static String create(String name, String description, String producer, String price, String picture, int template_id) throws Exception{
        //I tried to make the template into txt files and read it here,
        //but there are a lot of conflicts in webview and load method.
        //So we are stuck with the massive string here for now

        String html;
        switch (template_id){
            case 1:
                html =
                    "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<title>" + name + " - Product Page</title>\n" +
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
                        "<h1>" + name + " - Product Page</h1>\n" +
                        "</header>\n" +
                        "<div class=\"container\">\n" +
                        "<div class=\"product-info\">\n" +
                        "<img class=\"productImage\" alt=\"Image of Product\" src=\"file:///"
                        + picture.replace("\\", "/") + "\">\n" +
                        "<div class=\"details\">\n" +
                        "<h2>" + name + "</h2>\n" +
                        "<p>Price: $" + price + "</p>\n" +
                        "<p>Producer: " + producer + "</p>\n" +
                        "</div>\n" +
                        "</div>\n" +
                        "<div class=\"product-description\">\n" +
                        "<h2>Product Description</h2>\n" +
                        "<p>" + description + "</p>\n" +
                        "</div>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>";

                // Create thumbnail
                createThumbnail(String.valueOf(template_id), name, price, picture);
                break;
            case 2:
                html =
                    "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<title>" + name + " - Product Page</title>\n" +
                        "<meta charset=\"UTF-8\">\n" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "<style>\n" +
                        "body {\n" +
                        "font-family: Arial, sans-serif;\n" +
                        "margin: 0;\n" +
                        "padding: 0;\n" +
                        "background-color: lightblue;\n" +
                        "}\n" +
                        "header {\n" +
                        "background-color: #1F618D;\n" +
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
                        "<h1>" + name + " - Product Page</h1>\n" +
                        "</header>\n" +
                        "<div class=\"container\">\n" +
                        "<div class=\"product-info\">\n" +
                        "<img class=\"productImage\" alt=\"Image of Product\" src=\"file:///"
                        + picture.replace("\\", "/") + "\">\n" +
                        "<div class=\"details\">\n" +
                        "<h2>" + name + "</h2>\n" +
                        "<p>Price: $" + price + "</p>\n" +
                        "<p>Producer: " + producer + "</p>\n" +
                        "</div>\n" +
                        "</div>\n" +
                        "<div class=\"product-description\">\n" +
                        "<h2>Product Description</h2>\n" +
                        "<p>" + description + "</p>\n" +
                        "</div>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>";

                // Create thumbnail
                createThumbnail(String.valueOf(template_id), name, price, picture);
                break;
            default:
                System.out.println("A template with this id doesn't exist.");
                throw new Exception();
        }
        return html;
    }

    public static void createThumbnail(String id, String name, String price, String picture) {
        String thumbnailHtml =
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<title>" + name + " - Product Thumbnail</title>\n" +
                        "<meta charset=\"UTF-8\">\n" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "<style>\n" +
                        "body {\n" +
                        "font-family: Arial, sans-serif;\n" +
                        "margin: 0;\n" +
                        "padding: 0;\n" +
                        "}\n" +
                        ".thumbnail {\n" +
                        "width: 200px;\n" +
                        "border: 1px solid #ccc;\n" +
                        "padding: 10px;\n" +
                        "margin: 10px;\n" +
                        "}\n" +
                        ".thumbnail img {\n" +
                        "width: 100%;\n" +
                        "}\n" +
                        ".thumbnail h2 {\n" +
                        "font-size: 18px;\n" +
                        "margin: 10px 0;\n" +
                        "}\n" +
                        ".thumbnail p {\n" +
                        "font-size: 16px;\n" +
                        "margin: 10px 0;\n" +
                        "}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<a href=\"../../data/CMS/" + id + ".html\">\n" +
                        "<div class=\"thumbnail\">\n" +
                        "<img class=\"productImage\" alt=\"Image of Product\" src=\"file:///"
                        + picture.replace("\\", "/") + "\">\n" +
                        "<h2>" + name + "</h2>\n" +
                        "<p>Price: $" + price + "</p>\n" +
                        "</div>\n" +
                        "</a>\n" +
                        "</body>\n" +
                        "</html>";
        try {
            // Specify the directory to save the file
            Path htmlFilePath = Paths.get("src/main/data/Thumbnails/" + id + "_thumbnail.txt");

            File file = new File(htmlFilePath.toString());
            // Create a new file and write the thumbnailHtml to it
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(file)));
            writer.write(thumbnailHtml);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String create(String name, String subject, String text, String picture, int template_id) throws Exception{
        String html;
        switch (template_id){
            case 1:
                html = "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "\n" +
                        "<head>\n" +
                        "  <title>"+ name +"</title>\n" +
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
                        "<img src=\"" + picture.replace("\\", "/") + "\" alt=\"" + name + "\">\n" +
                        "  <h1>" + name + "</h1>\n" +
                        "\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>";
                break;
            default:
                System.out.println("A template with this id doesn't exist...");

                throw new Exception();
        }
        return html;
    }





}