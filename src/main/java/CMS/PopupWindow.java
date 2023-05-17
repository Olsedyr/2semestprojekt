package CMS;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class PopupWindow extends CMSController {

        private String result;

        public String getResult() {
            return this.result;
        }
        public PopupWindow() {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Pop-up Window");
            dialog.setHeaderText("Pop-up Window");

            ButtonType confirm = new ButtonType("Apply");
            dialog.getDialogPane().getButtonTypes().add(confirm);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(30,160,20,20));

            TextField id = new TextField();
            id.setPromptText("ID");

            TextField name = new TextField();
            name.setPromptText("Product Name");

            TextField productImage = new TextField();
            productImage.setPromptText("Product Image");

            TextField description = new TextField();
            description.setPromptText("Description");

            TextField price = new TextField();
            price.setPromptText("Price");

            TextField stock = new TextField();
            stock.setPromptText("Stock");

            TextField templateID = new TextField();
            templateID.setPromptText("Template ID");



            grid.add(new Label("Id:"), 0, 0);
            grid.add(id, 1, 0);

            grid.add(new Label("Name:"), 0, 1);
            grid.add(name, 1, 1);

            grid.add(new Label("Description:"), 0, 2);
            grid.add(description, 1, 2);

            grid.add(new Label("Price:"), 0, 3);
            grid.add(price, 1, 3);

            grid.add(new Label("Stock:"), 0, 4);
            grid.add(stock, 1, 4);

            grid.add(new Label("Image:"), 0, 5);
            grid.add(productImage, 1,5);

            grid.add(new Label("Template ID:"), 0, 6);
            grid.add(templateID, 1,6);


            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                TextField[] textFields = {id, name, description, price, stock, productImage, templateID};

                boolean emptyFields = false;

                for(int i = 0; i < textFields.length; i++){
                    if(textFields[i].getText().isEmpty()){
                        emptyFields = true;
                    }
                }

                if (dialogButton == confirm && emptyFields != true) {
                    String htmlContent = null;
                    try {

                        htmlContent = id.getText() + "-" + templateID.getText() + ";" + Create.create(name.getText(), description.getText(),
                                price.getText(), stock.getText(), productImage.getText(), Integer.parseInt(templateID.getText()));

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    return htmlContent;
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
