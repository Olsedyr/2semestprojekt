package CMS;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public interface ICMS {
    String getProductPage(String id, String template_id);

    String getProductPage(String name, String description, double price, int stock, String id, File imageFile,
                          int template_id);

    Map.Entry<String, String> getArticlePage(String title); //Read all article files into this HashMap and get one based on its id.

    HashMap<String, String> getArticlePages(); //Read all article files into this HashMap and get them all.

    Map.Entry<String, String> getThumbnail(String title); //Read all thumbnail files into this HashMap and get one based on its id.

    HashMap<String, String> getThumbnails(); //Read all thumbnail files into this HashMap and get them all.

}
