package apps.tweet.controller;

import apps.tweet.model.Tweet;
import config.Config;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class TweetListController implements Initializable {
    private static final Logger logger = LogManager.getLogger(TweetListController.class);
    private Config tweetAppConfig = Config.getConfig("TWEET_APP_CONFIG");
    private Config languageConfig = Config.getLanguageConfig();

    public static FXMLLoader getTweetListLoader() {
        Config config = Config.getConfig("TWEET_APP_CONFIG");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(TweetListController.class.getResource(config.getProperty("TWEET_LIST_VIEW"))));
        return fxmlLoader;
    }

    @FXML
    private VBox containerPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label lblMessage;

    public void addTweet(Tweet tweet) throws ConnectionException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(tweetAppConfig.getProperty("TWEET_CARD_VIEW"))));
        try {
            Pane pane = fxmlLoader.load();
            TweetController tweetController = fxmlLoader.getController();
            tweetController.setCurrentTweet(tweet);
            containerPane.getChildren().add(pane);
            lblMessage.setText("");
        }
        catch (IOException e) {
            logger.error("couldn't load card view fxml - " + e.getMessage());
        }
    }

    public void addTweetList(ArrayList<Tweet> tweets) throws ConnectionException {
        for (Tweet tweet: tweets)
            addTweet(tweet);
    }

    public void resizeHeight(int h) {
        scrollPane.setPrefHeight(h);
    }

    public void privateList() {
        lblMessage.setText(languageConfig.getProperty("PRIVATE_TWEET_LIST"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        lblMessage.setText(languageConfig.getProperty("EMPTY_TWEET_LIST"));
        lblMessage.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
    }
}