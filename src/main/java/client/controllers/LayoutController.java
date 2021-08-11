package client.controllers;

import client.controllers.tweet.TweetListController;
import client.store.MyProfileStore;
import client.views.AutoUpdate;
import client.views.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import shared.util.Config;

import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    private final Config config = Config.getConfig("mainConfig");

    @FXML
    private StackPane stackPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnClose, btnBack;

    private AutoUpdate updater = new AutoUpdate();

    @FXML
    void backButtonClicked(ActionEvent event) {
        ViewManager.previousPanel();
    }

    @FXML
    void showExplorer(ActionEvent event) {
        ViewManager.showPanel("EXPLORER");
    }

    @FXML
    void showGroups(ActionEvent event) {
        ViewManager.showPanel("MANAGE_LISTS");
    }

    @FXML
    void showHomePage() {
        ViewManager.Component<TweetListController> component = ViewManager.getComponent("TWEET_LIST");
        borderPane.setCenter(component.getPane());
        component.getController().showHomeTweets();
        updater.setTask(
                () -> {component.getController().showHomeTweets();},
                config.getProperty(Integer.class, "HOME_REFRESH_RATE")
        );
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
        ViewManager.showPanel(ViewManager.getComponentFXML("TWEET_INPUT"));
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

    public Button getBtnBack() {
        return btnBack;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showHomePage();
    }
}
