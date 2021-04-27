package apps.tweet.controller;

import apps.tweet.model.Tweet;
import config.Config;
import db.exception.ConnectionException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TweetListController {
    private static final Logger logger = LogManager.getLogger(TweetListController.class);
    private Config config = Config.getConfig("TWEET_APP_CONFIG");

    @FXML
    private VBox containerPane;

    public void addTweet(Tweet tweet) throws ConnectionException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(config.getProperty("TWEET_CARD_VIEW"))));
        try {
            Pane pane = fxmlLoader.load();
            TweetController tweetController = fxmlLoader.getController();
            tweetController.setCurrentTweet(tweet);
            containerPane.getChildren().add(pane);
        }
        catch (IOException e) {
            logger.error("couldn't load card view fxml - " + e.getMessage());
        }
    }

    public void addTweetList(ArrayList<Tweet> tweets) throws ConnectionException {
        for (Tweet tweet: tweets)
            addTweet(tweet);
    }
}