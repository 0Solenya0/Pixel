package client.views;

import client.request.exception.ConnectionException;
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

    public static void showConnectionErrorSaveLater(ConnectionException e) {
        Dialog dialog = new Dialog();
        dialog.setTitle("connection error");
        if (e.getErrorType().equals(ConnectionException.ErrorType.CONNECTION_ERROR))
            dialog.setContentText(config.getProperty("CONNECTION_FAILED_DIALOG_SAVE_LATER"));
        else if (e.getErrorType().equals(ConnectionException.ErrorType.INTERNAL_SERVER_ERROR))
            dialog.setContentText(config.getProperty("INTERNAL_FAILED_DIALOG_NO_SAVE"));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
}
