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
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.SuccessDialog;

import java.util.Objects;

public class TweetInputController extends Controller {
    private static final Logger logger = LogManager.getLogger(TweetInputController.class);

    @FXML
    private JFXTextArea txtTweet;

    @FXML
    private JFXButton btnTweet;

    @FXML
    private Label lblContentErr, lblGlobalErr;

    public void showError(ValidationException e) {
        if (e.getErrors("Global") != null)
            lblGlobalErr.setText(e.getErrors("Global").get(0));
        if (e.getErrors("Content") != null)
            lblContentErr.setText(e.getErrors("Content").get(0));
    }

    public void tweet() throws ConnectionException {
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
}
