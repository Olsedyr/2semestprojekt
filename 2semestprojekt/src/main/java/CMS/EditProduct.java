package CMS;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;
import java.util.Optional;

public class EditProduct extends CMSController {

        private String result;

        public String getResult() {
            return this.result;
        }
        public EditProduct() {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Edit Product");
            dialog.setHeaderText("Edit Product");

            ButtonType confirm = new ButtonType("Apply");
            dialog.getDialogPane().getButtonTypes().add(confirm);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(30,160,20,20));

            Label id = new Label();
            id.setText("1");

            TextField name = new TextField();
            name.setPromptText("Product Name");

            TextField producI = new TextField();
            producI.setPromptText("Product Image");

            TextField description = new TextField();
            description.setPromptText("Description");

            TextField producer = new TextField();
            producer.setPromptText("Producer");

            TextField price = new TextField();
            price.setPromptText("Price");



            grid.add(new Label("Id:"), 0, 0);
            grid.add(id, 1, 0);

            grid.add(new Label("Name:"), 0, 1);
            grid.add(name, 1, 1);

            grid.add(new Label("Description:"), 0, 2);
            grid.add(description, 1, 2);

            grid.add(new Label("Producer:"), 0, 3);
            grid.add(producer, 1, 3);

            grid.add(new Label("Price:"), 0, 4);
            grid.add(price, 1, 4);

            grid.add(new Label("Image:"), 0, 5);
            grid.add(producI, 1,5);


            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirm) {
                    return  id.getText() + ";" + name.getText() + ";" + description.getText() + ";" + producer.getText() + ";" + price.getText() + ";";
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
