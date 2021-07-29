package client.views;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class InfoDialog {
    public static void showSuccess(String text) {
        Dialog dialog = new Dialog();
        dialog.setTitle("success");
        dialog.setContentText(text);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    public static void showFailed(String text) {
        Dialog dialog = new Dialog();
        dialog.setTitle("failed");
        dialog.setContentText(text);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
}
