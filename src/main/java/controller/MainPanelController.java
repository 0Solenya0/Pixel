package controller;


import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class MainPanelController {

    @FXML
    private JFXButton btnHome, btnPostTweet;

    public void btnPostTweetClicked() {
        Dialog dialog = new Dialog();
        dialog.setTitle("New Tweet");
        dialog.getDialogPane().get
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Your name: " + result.get());
        }
    }

}
