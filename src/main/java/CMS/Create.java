package CMS;

public class Create {
    public static String create(String name, String description, String price, String stock,
                                String picture, int template_id) throws Exception{
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
                        "<img src=\"" + picture + "\" alt=\"" + name + "\">\n" +
                        "  <h1>" + name + "</h1>\n" +
                        "\n" +
                        "  <h2>Product Description:</h2>\n" +
                        "  <p>\n" +
                        description +
                        "  </p>\n" +
                        "\n" +
                        "  <h2>Price:</h2>\n" +
                        "  <p>\n" +
                        "$" +
                        price +
                        "  </p>\n" +
                        "\n" +
                        "  <h2>Stock:</h2>\n" +
                        "  <p>\n" +
                        stock +
                        "  </p>\n" +
                        "\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>";
            break;
            default:
                System.out.println("A template with this id doesn't exist.");
                
                throw new Exception();
        }

        return html;
    }
}
