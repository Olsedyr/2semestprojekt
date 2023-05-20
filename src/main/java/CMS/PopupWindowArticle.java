package CMS;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class PopupWindowArticle extends CMSController {

    private String[] result;

    public String[] getResult() {
        return this.result;
    }
    public PopupWindowArticle() {
        Dialog<String[]> dialog = new Dialog<>();

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
            TextField[] textFields = {id, subject, articleText, image, templateID};
            boolean emptyFields = false;

            for(int i = 0; i < textFields.length; i++){
                if(textFields[i].getText().isEmpty()){
                    emptyFields = true;
                }
            }

            if (dialogButton == confirm && emptyFields != true) {
                String[] array = new String[3];
                try {

                    array[0] = id.getText() + "-" + templateID.getText();

                    array[1] = id.getText() + "-" + templateID.getText() + ";" + subject.getText() + ";" + articleText.getText()
                            + ";" + image.getText();

                    array[2] = Create.create(id.getText(), subject.getText(),
                            articleText.getText(), image.getText(), Integer.parseInt(templateID.getText()));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return array;
            }

            return null;
        });

        Optional<String[]> rslt = dialog.showAndWait();
        if (rslt.isPresent() ) {
            this.result = rslt.get();
        }
        else this.result = null;
    }
}