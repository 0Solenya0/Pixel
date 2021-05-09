package apps.tweet.controller;

import apps.auth.model.User;
import apps.tweet.model.Tweet;
import com.jfoenix.controls.JFXButton;
import controller.Controller;
import db.dbSet.DBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.SuccessDialog;

public class TweetController extends Controller {
    private static final Logger logger = LogManager.getLogger(TweetController.class);

    private Tweet currentTweet;

    @FXML
    private Label lblAuthor, lblTweet;

    @FXML
    private JFXButton btnSave, btnShare, btnRetweet, btnComment, btnLike, btnMute, btnReport;

    @FXML
    private HBox hboxForeign;

    @FXML
    private FontAwesomeIconView iconLike;

    @FXML
    void btnCommentClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnLikeClicked(ActionEvent event) throws ConnectionException {
        User user = apps.auth.State.getUser();
        if (iconLike.getGlyphName().equals("HEART")) {
            context.tweets.dislikeTweet(currentTweet, user);
            iconLike.setGlyphName("HEART");
        }
        else {
            context.tweets.likeTweet(currentTweet, user);
            iconLike.setGlyphName("HEART_ALT");
        }
        updateCard();
    }

    @FXML
    void btnMuteClicked(ActionEvent event) throws ConnectionException {
        context.users.muteUser(apps.auth.State.getUser(), context.users.get(currentTweet.getAuthor()));
        updateCard();
    }

    @FXML
    void btnReportClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnRetweetClicked(ActionEvent event) throws ConnectionException {
        User user = apps.auth.State.getUser();
        Tweet tweet = new Tweet(user, currentTweet.id);
        try {
            context.tweets.save(tweet);
        }
        catch (ValidationException e) {
            logger.error("unexpected retweet validation failed");
            return;
        }
        SuccessDialog.show("Retweet successful!");
    }

    @FXML
    void btnSaveClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnShareClicked(ActionEvent event) {
        // TO DO
    }

    public void setCurrentTweet(Tweet currentTweet) throws ConnectionException {
        this.currentTweet = currentTweet;
        updateCard();
    }

    public void updateCard() throws ConnectionException {
        User user = apps.auth.State.getUser();
        currentTweet = context.tweets.get(currentTweet.id);
        lblAuthor.setText("@" + context.users.get(currentTweet.getAuthor()).getUsername());
        // TO DO Handle retweets
        // TO DO Handle Muted Author
        lblTweet.setText(currentTweet.getContent());
        if (currentTweet.containsLike(user))
            iconLike.setGlyphName("HEART");
        else
            iconLike.setGlyphName("HEART_ALT");
        if (currentTweet.getAuthor() == user.id)
            hboxForeign.setVisible(false);
    }

}

