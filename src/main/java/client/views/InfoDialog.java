package client.views;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import shared.util.Config;

public class InfoDialog {
    private static final Config config = Config.getLanguageConfig();

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

    public static void showConnectionError() {
        Dialog dialog = new Dialog();
        dialog.setTitle("connection error");
        dialog.setContentText(config.getProperty("CONNECTION_FAILED_DIALOG"));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
}
