package controller;


import apps.auth.State;
import apps.auth.model.User;
import apps.tweet.controller.TweetController;
import apps.tweet.controller.TweetListController;
import apps.tweet.model.Tweet;
import com.jfoenix.controls.JFXButton;
import com.sun.tools.javac.Main;
import config.Config;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPanelController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainPanelController.class);
    private Config config = Config.getConfig("TWEET_APP_CONFIG");

    @FXML
    private JFXButton btnHome, btnPostTweet;

    @FXML
    private BorderPane borderPane;

    public void changeCenterPane(Pane pane) {
        borderPane.setCenter(pane);
    }

    public void showPostTweetPage() throws IOException {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(config.getProperty("TWEET_INPUT_VIEW"))));
        ViewManager.changeCenter(pane);
    }

    public void showHomePage() {
        showTweetList(getTimelineTweets());
    }

    public ArrayList<Tweet> getTimelineTweets() {
        ArrayList<Tweet> tweets = new ArrayList<>();
        // for (User user: apps.auth.State.getUser())
        // TO DO
        return tweets;
    }

    public void showTweetList(ArrayList<Tweet> tweets) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(config.getProperty("TWEET_LIST_VIEW"))));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
