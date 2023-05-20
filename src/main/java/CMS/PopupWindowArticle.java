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


    /*This is the method for returning the string containing the result of the text
      the user has put in the textfields.*/
    public String getResult() {

        return this.result;
    }

    /*This method is what is used to create the popupwindow when creating an article
      This is where all the textfields and text is placed inside the window */
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

        TextField subject = new TextField();
        subject.setPromptText("Article Subject");

        TextField articleText = new TextField();
        articleText.setPromptText("Article Text");


        TextField image = new TextField();
        image.setPromptText("Image");

        TextField templateID = new TextField();
        templateID.setPromptText("Template ID");



        grid.add(new Label("Id:"), 0, 0);
        grid.add(id, 1, 0);

        grid.add(new Label("Article Subject:"), 0, 2);
        grid.add(subject, 1, 2);

        grid.add(new Label("Article Text:"), 0, 3);
        grid.add(articleText, 1, 3);


        grid.add(new Label("Image:"), 0, 5);
        grid.add(image, 1,5);

        grid.add(new Label("Template ID:"), 0, 6);
        grid.add(templateID, 1,6);


        dialog.getDialogPane().setContent(grid);


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirm) {
                String htmlContent = null;
                try {

                    CMSController.getCMSController().getArticles().put(id.getText() + "-" + templateID.getText(),
                            id.getText() + "-" + templateID.getText() + ";" + subject.getText() + ";" + articleText.getText()
                                    + ";" + image.getText());

                    htmlContent = id.getText() + "-" + templateID.getText() + ";" + Create.create(id.getText(), subject.getText(),
                            articleText.getText(), image.getText(), Integer.parseInt(templateID.getText()));

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
