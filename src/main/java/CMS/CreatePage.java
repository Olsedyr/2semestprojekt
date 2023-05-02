package CMS;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
public class CreatePage extends CMSController {

        private String result;

        public String getResult() {
            return this.result;
        }
        public CreatePage() {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Edit Page");
            dialog.setHeaderText("Edit Page");

            ButtonType confirm = new ButtonType("Create");
            dialog.getDialogPane().getButtonTypes().add(confirm);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(500,1200,20,20));

            TextField id = new TextField();
            id.setPromptText("ID");

            TextField name = new TextField();
            name.setPromptText("Page Title");

            TextField productImage = new TextField();
            productImage.setPromptText("Page Subject");

            TextField description = new TextField();
            description.setPromptText("Article");

            TextField producer = new TextField();
            producer.setPromptText("Image");


            grid.add(new Label("Id:"), 0, 0);
            grid.add(id, 1, 0);

            grid.add(new Label("Title:"), 0, 1);
            grid.add(name, 1, 1);

            grid.add(new Label("Subject:"), 0, 2);
            grid.add(description, 1, 2);

            grid.add(new Label("Article:"), 0, 3);
            grid.add(producer, 1, 3);

            grid.add(new Label("Image:"), 0, 4);
            grid.add(productImage, 1,4);


            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirm) {

                }

                return null;
            });

            Optional<String> rslt = dialog.showAndWait();
            if (rslt.isPresent() ) {
                this.result = rslt.get();
            }
            else this.result = null;
        }
    }


