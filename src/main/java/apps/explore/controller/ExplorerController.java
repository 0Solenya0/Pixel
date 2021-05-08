package apps.explore.controller;

import apps.auth.State;
import apps.auth.model.User;
import apps.tweet.controller.TweetListController;
import apps.tweet.model.Tweet;
import config.Config;
import controller.Controller;
import db.exception.ConnectionException;
import db.queryBuilder.TweetQueryBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ExplorerController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(ExplorerController.class);
    private Config config = Config.getLanguageConfig();

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

    @FXML
    void usernameChange() throws ConnectionException {
        ArrayList<User> users = context.users.getAll(
                context.users.getQueryBuilder()
                .getByUsernamePrefix(txtUsername.getText())
                .getQuery()
        );
        vBoxContainer.getChildren().clear();
        if (users.isEmpty()) {
            lblErr.setVisible(true);
            iconErr.setVisible(true);
        }
        else {
            lblErr.setVisible(false);
            iconErr.setVisible(false);
        }
        for (User user: users) {
            FXMLLoader fxmlLoader = UserCardController.getFxmlLoader();
            try {
                Pane pane = fxmlLoader.load();
                UserCardController controller = fxmlLoader.getController();
                controller.setOnClickListener(s -> {
                    ViewManager.mainPanelController.showProfile(user);
                });
                controller.setUser(user);
                vBoxContainer.getChildren().add(pane);
            } catch (IOException e) {
                logger.error("failed to load view fxml file");
                e.printStackTrace();
            } catch (ConnectionException e) {
                ViewManager.connectionFailed();
            }
        }
    }

    @FXML
    void switchToExploreFeed() {
        FXMLLoader fxmlLoader = TweetListController.getTweetListLoader();
        try {
            Pane pane = fxmlLoader.load();
            TweetListController tweetListController = fxmlLoader.getController();
            ArrayList<Tweet> tweets = context.tweets.getAll(
                    context.tweets.getQueryBuilder()
                            .getEnabled()
                            .excludeUser(State.getUser())
                            .getPublicTweet()
                            .getNotMuted(State.getUser())
                    .getQuery()
            );
            tweets.sort(Comparator.comparingInt(tweet -> -tweet.getLikes().size()));

            tweetListController.addTweetList(tweets);
            tweetListController.resizeHeight(725);
            explorerFeedPane.getChildren().add(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUsername.setText(config.getProperty("USERNAME"));
        iconErr.setVisible(false);
        iconErr.setFill(Paint.valueOf(config.getProperty("ERROR_COLOR")));
        lblErr.setText(config.getProperty("USER_NOT_FOUND"));
        lblErr.setVisible(false);
        lblErr.setTextFill(Paint.valueOf(config.getProperty("ERROR_COLOR")));
    }
}
