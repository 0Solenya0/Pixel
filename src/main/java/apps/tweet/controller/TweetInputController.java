package apps.tweet.controller;

import apps.auth.State;
import apps.tweet.model.Tweet;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import config.Config;
import controller.Controller;
import db.dbSet.UserDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.SuccessDialog;

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
        try {
            context.tweets.save(tweet);
        }
        catch (ValidationException e) {
            showError(e);
            return;
        }
        SuccessDialog.show("Tweeted successfully!");
    }

    public void resetErrorFields() {
        lblContentErr.setText("");
        lblGlobalErr.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetErrorFields();
        txtTweet.setPromptText(config.getProperty("TWEET_INPUT_PROMPT"));
        lblInputTweet.setText(config.getProperty("TWEET_INPUT_LABEL"));
        btnTweet.setText(config.getProperty("TWEET_BTN_TEXT"));
        lblGlobalErr.setTextFill(Paint.valueOf(config.getProperty("ERROR_COLOR")));
        lblContentErr.setTextFill(Paint.valueOf(config.getProperty("ERROR_COLOR")));
    }
}
