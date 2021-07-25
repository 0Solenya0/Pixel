package client.controllers;

import client.views.ViewManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    void backButtonClicked(ActionEvent event) {

    }

    @FXML
    void showExplorer(ActionEvent event) {

    }

    @FXML
    void showGroups(ActionEvent event) {

    }

    @FXML
    void showHomePage(ActionEvent event) {

    }

    @FXML
    void showMessages(ActionEvent event) {

    }

    @FXML
    void showMyProfile(ActionEvent event) {

    }

    @FXML
    void showNotifications(ActionEvent event) {

    }

    @FXML
    void showPostTweetPage(ActionEvent event) {

    }

    @FXML
    void showSettings(ActionEvent event) {
        ViewManager.showPanel("SETTINGS");
    }

    public BorderPane getPanel() {
        return borderPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
