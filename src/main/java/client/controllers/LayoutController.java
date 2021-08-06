package client.controllers;

import client.controllers.components.TweetInputController;
import client.store.MyProfile;
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
        // TO DO
    }

    @FXML
    void showExplorer(ActionEvent event) {
        ViewManager.showPanel("EXPLORER");
    }

    @FXML
    void showGroups(ActionEvent event) {
        // TO DO
    }

    @FXML
    void showHomePage(ActionEvent event) {
        // TO DO
    }

    @FXML
    void showMessages(ActionEvent event) {
        // TO DO
    }

    @FXML
    void showMyProfile(ActionEvent event) {
        ProfileController controller = ViewManager.showPanel("PROFILE");
        MyProfile.getInstance().updateUserProfile();
        controller.setUserId(MyProfile.getInstance().getUser().id);
    }

    @FXML
    void showNotifications(ActionEvent event) {
        // TO DO
    }

    @FXML
    void showPostTweetPage(ActionEvent event) {
        ViewManager.changePanel(ViewManager.getComponentFXML("TWEET_INPUT"));
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