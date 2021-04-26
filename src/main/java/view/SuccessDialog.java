package view;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class SuccessDialog {
    public static void show(String text) {
        Dialog dialog = new Dialog();
        dialog.setTitle("success");
        dialog.setContentText(text);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
}
