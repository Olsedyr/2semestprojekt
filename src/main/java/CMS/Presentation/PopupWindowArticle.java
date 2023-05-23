package CMS.Presentation;
import CMS.Domain.Create;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class PopupWindowArticle extends CMSController {

    //This is the String array where the result of the textfields.
    private String[] result;

    //This is method retrives the value of the string array.
    public String[] getResult() {

        return this.result;
    }


    //This is constructor where the DialogPane(popupwindow) is initialized, and all of its content
    // (textfields, labels etc.) is  assigned.
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


        //This gets the DialogPane (The window) and sets the content assigned above to the gridPane inside it
        dialog.getDialogPane().setContent(grid);


        //This method handles the action for when the apply button is clicked
        //It creates an array where all the textfileds are placed into.
        dialog.setResultConverter(dialogButton -> {
            TextField[] textFields = {id, subject, articleText, image, templateID};
            boolean emptyFields = false;

            //Checks if there are any empty fields, by checking the length of the array and checking if one of the
            //Textfields are empty.
            for(int i = 0; i < textFields.length; i++){
                if(textFields[i].getText().isEmpty()){
                    emptyFields = true;
                }
            }

            //Here a new array is created with a lenght of 3 where all the textfields values are assigned into.
            if (dialogButton == confirm && emptyFields != true) {
                String[] array = new String[3];
                try {

                    array[0] = id.getText() + "---" + templateID.getText();

                    array[1] = id.getText() + "---" + templateID.getText() + ";;" + subject.getText() + ";;" + articleText.getText()
                            + ";;" + image.getText();

                    //Here the Create.create method from the create class is run which generates an HTML webpage from the info
                    array[2] = Create.create(id.getText(), subject.getText(),
                            articleText.getText(), image.getText(), Integer.parseInt(templateID.getText()));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                return array;
            }

            return null;
        });


        //This captures the result of the window
        Optional<String[]> rslt = dialog.showAndWait();

        //If any value is present the result of the PopUpWindow object is assigned  rslt.get()
        if (rslt.isPresent() ) {
            this.result = rslt.get();
        }
        else this.result = null;
    }
    //Basicly, you create an instance of the PopupWindow class, use the getResult() method, it will return the string array
    // with the values in the pop-up window or null if the pop-up window was closed without applying
    // any changes.
}