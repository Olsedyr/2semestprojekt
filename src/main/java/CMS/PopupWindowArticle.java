package CMS;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class PopupWindowArticle extends CMSController {

    private String result;

    public String getResult() {
        return this.result;
    }
    public PopupWindowArticle() {
        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Create Article");
        dialog.setHeaderText("Create Article");

        ButtonType confirm = new ButtonType("Apply");
        dialog.getDialogPane().getButtonTypes().add(confirm);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30,160,20,20));

        TextField id = new TextField();
        id.setPromptText("ID");

        TextField name = new TextField();
        name.setPromptText(" Article Name");

        TextField productImage = new TextField();
        productImage.setPromptText("Article Subject");

        TextField articleText = new TextField();
        articleText.setPromptText("Article Text");



        TextField articleNumber = new TextField();
        articleNumber.setPromptText("Article Number");

        TextField price = new TextField();
        price.setPromptText("Image");

        TextField templateID = new TextField();
        templateID.setPromptText("Template ID");



        grid.add(new Label("Id:"), 0, 0);
        grid.add(id, 1, 0);

        grid.add(new Label("Article Name:"), 0, 1);
        grid.add(name, 1, 1);

        grid.add(new Label("Article Subject:"), 0, 2);
        grid.add(articleText, 1, 2);

        grid.add(new Label("Article Text:"), 0, 3);
        grid.add(price, 1, 3);

        grid.add(new Label("Article Number:"), 0, 4);
        grid.add(articleNumber, 1, 4);

        grid.add(new Label("Image:"), 0, 5);
        grid.add(productImage, 1,5);

        grid.add(new Label("Template ID:"), 0, 6);
        grid.add(templateID, 1,6);


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirm) {
                String htmlContent = null;
                try {

                    htmlContent = id.getText() + "-" + templateID.getText() + ";" + Create.create(name.getText(), articleText.getText(),
                            price.getText(), articleNumber.getText(), productImage.getText(), Integer.parseInt(templateID.getText()));

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
