package CMS;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
public class FxmlLoader extends CMSController {

    private Pane view;

    public Pane getPane(String fileName) {
        try {
            URL fileUrl = CMSApplication.class.getResource(fileName);
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("Cant find file");
            }
            view = new FXMLLoader().load(fileUrl);

        } catch (Exception e) {
            System.out.println("No fxml file: " + fileName);
        }
        return view;
    }
}


