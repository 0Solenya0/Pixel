package client.controllers;

import client.controllers.tweet.TweetListController;
import client.controllers.user.UserCardController;
import client.request.SocketHandler;
import client.views.AutoUpdate;
import client.views.ViewManager;
import com.google.gson.reflect.TypeToken;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import shared.models.User;
import shared.request.Packet;
import shared.util.Config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ExplorerController {

    private Config config = Config.getConfig("mainConfig");

    @FXML
    private Tab tabExploreUser, tabExploreFeed;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblUsername, lblErr;

    @FXML
    private VBox vBoxContainer;

    @FXML
    private FontAwesomeIconView iconErr;

    @FXML
    private AnchorPane explorerFeedPane;

    private Timer timer = new Timer();
    private AutoUpdate updater = new AutoUpdate();

    @FXML
    void switchToExploreFeed() {
        ViewManager.Component<TweetListController> component = ViewManager.getComponent("TWEET_LIST");
        explorerFeedPane.getChildren().clear();
        explorerFeedPane.getChildren().add(component.getPane());
        component.getController().showExplorerTweets();
        updater.setTask(
                () -> {component.getController().showExplorerTweets();},
                config.getProperty(Integer.class, "EXPLORER_REFRESH_RATE")
        );
    }

    private void updateUserList(ArrayList<User> users) {
        lblErr.setVisible(users.isEmpty());
        iconErr.setVisible(users.isEmpty());

        vBoxContainer.getChildren().clear();
        for (User user: users) {
            ViewManager.Component<UserCardController> component = ViewManager.getComponent("USER_CARD");

            vBoxContainer.getChildren().add(component.getPane());
            UserCardController controller = component.getController();
            controller.setListener(() -> {
                ProfileController c = ViewManager.showPanel("PROFILE");
                assert c != null;
                c.setUserId(user.id);
            });
            controller.setUser(user);
        }
    }

    @FXML
    void usernameChange(KeyEvent event) {
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Packet packet = new Packet("search-user");
                    packet.put("username", txtUsername.getText());
                    Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
                    Type listType = new TypeToken<ArrayList<User>>() {
                    }.getType();
                    updateUserList(res.getObject("users", listType));
                });
            }
        }, 500);
    }

}
