package apps.tweet.controller;

import apps.auth.State;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.stage.FileChooser;
import model.Tweet;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import util.Config;
import controller.Controller;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.InfoDialog;
import view.ViewManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TweetInputController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(TweetInputController.class);
    private Config config = Config.getLanguageConfig();

    @FXML
    private JFXTextArea txtTweet;

    @FXML
    private JFXButton btnTweet;

    @FXML
    private Label lblContentErr, lblGlobalErr, lblInputTweet;

    private int parentTweet, parentRetweet;
    private String photoId;

    public void showError(ValidationException e) {
        if (e.getErrors("Global") != null)
            lblGlobalErr.setText(e.getErrors("Global").get(0));
        if (e.getErrors("Content") != null)
            lblContentErr.setText(e.getErrors("Content").get(0));
    }

    public void tweet() throws ConnectionException {
        resetErrorFields();
        if (apps.auth.State.getUser() == null) {
            ValidationException validationException = new ValidationException();
            validationException.addError("Global", "You are not logged in");
            logger.error("User is not logged in but can tweet");
            showError(validationException);
            return;
        }
        Tweet tweet = new Tweet(Objects.requireNonNull(State.getUser()), txtTweet.getText());
        tweet.setParentTweet(parentTweet);
        tweet.setReTweet(parentRetweet);
        tweet.setPhoto(photoId);
        try {
            context.tweets.save(tweet);
        }
        catch (ValidationException e) {
            showError(e);
            return;
        }
        InfoDialog.showSuccess("Tweeted successfully!");
    }

    public void resetErrorFields() {
        lblContentErr.setText("");
        lblGlobalErr.setText("");
    }

    public void attachPhoto() throws ConnectionException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your image");
        File file = fileChooser.showOpenDialog(ViewManager.getWindow());
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            photoId = context.images.save(bufferedImage);
        } catch (IOException e) {
            logger.error("Failed to read photo");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetErrorFields();
        txtTweet.setPromptText(config.getProperty("TWEET_INPUT_PROMPT"));
        lblInputTweet.setText(config.getProperty("TWEET_INPUT"));
        btnTweet.setText(config.getProperty("TWEET_BTN_TEXT"));
        lblGlobalErr.setTextFill(Paint.valueOf(config.getProperty("ERROR_COLOR")));
        lblContentErr.setTextFill(Paint.valueOf(config.getProperty("ERROR_COLOR")));
    }

    public void setParentTweet(int parentTweet) {
        this.parentTweet = parentTweet;
    }

    public void setParentRetweet(int parentRetweet) {
        this.parentRetweet = parentRetweet;
    }
}
