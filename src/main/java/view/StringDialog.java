package view;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import listener.StringListener;
import listener.UserListListener;
import model.User;

import java.util.ArrayList;

public class StringDialog {
    public static void show(String title, StringListener listener) {
        Pane rootPane = new Pane();
        JFXTextField textField = new JFXTextField();
        rootPane.getChildren().add(textField);
        Stage newDialog = new Stage(StageStyle.UTILITY);
        newDialog.initModality(Modality.APPLICATION_MODAL);
        newDialog.setTitle(title);
        Scene newDialogScene = new Scene(rootPane);
        newDialog.setScene(newDialogScene);
        newDialog.showAndWait();
        listener.listen(textField.getText());
    }
}
