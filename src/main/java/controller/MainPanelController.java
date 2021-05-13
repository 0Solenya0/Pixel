package controller;


import apps.auth.State;
import apps.auth.controller.ProfileController;
import apps.messenger.controller.MessengerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import model.User;
import apps.tweet.controller.TweetListController;
import model.Tweet;
import com.jfoenix.controls.JFXButton;
import util.Config;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ViewManager;

import javax.swing.text.View;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainPanelController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainPanelController.class);
    private Config tweetAppConfig = Config.getConfig("TWEET_APP_CONFIG");
    private Config authAppConfig = Config.getConfig("AUTH_APP_CONFIG");
    private Config exploreAppConfig = Config.getConfig("EXPLORE_APP_CONFIG");
    private Config notificationAppConfig = Config.getConfig("NOTIFICATION_APP_CONFIG");
    private Config messengerAppConfig = Config.getConfig("MESSENGER_APP_CONFIG");

    @FXML
    private JFXButton btnHome, btnPostTweet, btnMyProfile, btnExplorer, btnNotification, btnClose, btnBack;

    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane stackPane;

    private final RelationController relationController = new RelationController();

    public void changeCenterPane(Pane pane) {
        borderPane.setCenter(pane);
    }

    public void showPostTweetPage() throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(tweetAppConfig.getProperty("TWEET_INPUT_VIEW"))));
        ViewManager.changeCenter(pane);
    }

    public void showHomePage() throws ConnectionException {
        showTweetList(getTimelineTweets());
    }

    public ArrayList<Tweet> getTimelineTweets() throws ConnectionException {
        ArrayList<Tweet> tweets = new ArrayList<>();
        User user = apps.auth.State.getUser();
        for (User u: relationController.getFollowing(apps.auth.State.getUser()))
            if (!user.isMuted(u))
                tweets.addAll(context.tweets.getAll(
                        context.tweets.getQueryBuilder()
                                .getByAuthorId(u.id)
                                .getByParentTweet(0)
                                .getNotMuted(State.getUser())
                        .getQuery()
                ));
        tweets.sort(Comparator.comparingInt(t -> -t.id));
        return tweets;
    }

    public void showMessages(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(messengerAppConfig.getProperty("MESSENGER_VIEW"))));
            Pane pane = fxmlLoader.load();
            MessengerController messengerController = fxmlLoader.getController();
            messengerController.updateMessagesPane(user);
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
        }
    }

    public void showMessages() {
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(messengerAppConfig.getProperty("MESSENGER_VIEW"))));
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    public void showGroups() {
        try {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(messengerAppConfig.getProperty("GROUP_MANAGER_VIEW"))));
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    public void showTweetComments(Tweet tweet) throws ConnectionException {
        FXMLLoader fxmlLoader = TweetListController.getTweetListLoader();
        ArrayList<Tweet> comment = context.tweets.getAll(
                context.tweets.getQueryBuilder().getByParentTweet(tweet.id).getQuery()
        );
        try {
            Pane pane = fxmlLoader.load();
            TweetListController tweetListController = fxmlLoader.getController();
            tweetListController.addTweet(tweet);
            tweetListController.addCommentSection(tweet);
            tweetListController.addTweetList(comment);
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
        }
    }

    public void showMyProfile() throws ConnectionException {
        showProfile(State.getUser());
    }

    public void showExplorer() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(exploreAppConfig.getProperty("EXPLORE_VIEW")));
        try {
            Pane pane = fxmlLoader.load();
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    public void showNotifications() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(notificationAppConfig.getProperty("NOTIFICATION_VIEW")));
        try {
            Pane pane = fxmlLoader.load();
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    public void showSettings() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(authAppConfig.getProperty("SETTING_VIEW")));
        try {
            Pane pane = fxmlLoader.load();
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    public void showProfile(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(authAppConfig.getProperty("PROFILE_VIEW"))));
        try {
            Pane pane = fxmlLoader.load();
            ProfileController profileController = fxmlLoader.getController();
            profileController.setUser(user);
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
        }
    }

    public void showTweetList(ArrayList<Tweet> tweets) throws ConnectionException {
        FXMLLoader fxmlLoader = TweetListController.getTweetListLoader();
        try {
            Pane pane = fxmlLoader.load();
            TweetListController tweetListController = fxmlLoader.getController();
            tweetListController.addTweetList(tweets);
            ViewManager.changeCenter(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
        }
    }

    public void addStackPaneNode(Node node) {
        btnClose.setVisible(true);
        stackPane.getChildren().add(stackPane.getChildren().size() - 1, node);
        EventHandler<ActionEvent> handler = btnClose.getOnAction();
        btnClose.setOnAction(e -> {
            stackPane.getChildren().remove(node);
            if (stackPane.getChildren().size() == 2)
                btnClose.setVisible(false);
            if (handler != null)
                btnClose.setOnAction(handler);
        });
    }

    public void setBackButtonVisibility(boolean vis) {
        btnBack.setVisible(vis);
    }

    public void backButtonClicked() {
        ViewManager.back();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnClose.setVisible(false);
        btnBack.setVisible(false);
    }


}
