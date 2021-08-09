package client.controllers;

import client.store.MyProfileStore;
import client.views.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnClose;

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
        ViewManager.showPanel("MESSAGE");
    }

    @FXML
    void showMyProfile(ActionEvent event) {
        ProfileController controller = ViewManager.showPanel("PROFILE");
        MyProfileStore.getInstance().updateUserProfile();
        controller.setUserId(MyProfileStore.getInstance().getUser().id);
    }

    @FXML
    void showNotifications(ActionEvent event) {
        ViewManager.showPanel("NOTIFICATION");
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

    public StackPane getStackPane() {
        return stackPane;
    }

    public Button getBtnClose() {
        return btnClose;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
