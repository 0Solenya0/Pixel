package apps.messenger.controller;

import apps.auth.State;
import apps.tweet.controller.TweetCardController;
import com.jfoenix.controls.JFXButton;
import controller.Controller;
import db.exception.ConnectionException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MessageBoxCardController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(MessageBoxCardController.class);
    private final Config tweetAppConfig = Config.getConfig("TWEET_APP_CONFIG");

    @FXML
    private FontAwesomeIconView iconEdit;

    @FXML
    private Label lblContent;

    @FXML
    private ImageView imgReceiverAvatar, imgSenderAvatar;

    @FXML
    private JFXButton btnEdit, btnDelete, btnShowTweet;

    private Message message;

    @FXML
    void deleteMessage(ActionEvent event) {

    }

    @FXML
    void editMessage(ActionEvent event) {

    }

    @FXML
    void showTweet(ActionEvent event) throws ConnectionException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(tweetAppConfig.getProperty("TWEET_CARD_VIEW")));
            Pane pane = fxmlLoader.load();
            TweetCardController controller = fxmlLoader.getController();
            controller.setCurrentTweet(context.tweets.get(message.getTweetId()));
            ViewManager.mainPanelController.addStackPaneNode(pane);
        }
        catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    public void setMessage(Message message) {
        this.message = message;
        updateCard();
    }

    public void updateCard() {
        lblContent.setText(message.getContent());
        if (message.getTweetId() != 0) {
            btnShowTweet.setVisible(true);
            lblContent.setVisible(false);
        }
        if (State.getCurrentUserId() == message.getSender()) {
            imgSenderAvatar.setVisible(true);
        }
        else {
            imgReceiverAvatar.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnDelete.setVisible(false);
        btnEdit.setVisible(false);
        imgSenderAvatar.setVisible(false);
        imgReceiverAvatar.setVisible(false);
        btnShowTweet.setVisible(false);
    }
}
