package controller;


import com.jfoenix.controls.JFXButton;
import config.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import view.ViewManager;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class MainPanelController {
    private Config config = Config.getConfig("TWEET_APP_CONFIG");

    @FXML
    private JFXButton btnHome, btnPostTweet;

    @FXML
    private BorderPane borderPane;

    public void changeCenterPane(Pane pane) {
        borderPane.setCenter(pane);
    }

    public void btnPostTweetClicked() throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(config.getProperty("TWEET_INPUT_VIEW"))));
        ViewManager.changeCenter(pane);
    }

}
