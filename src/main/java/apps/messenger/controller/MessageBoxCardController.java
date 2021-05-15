package apps.messenger.controller;

import apps.auth.State;
import apps.tweet.controller.TweetCardController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import controller.Controller;
import db.dbSet.ImageDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Message;
import model.User;
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
    private final Config languageConfig = Config.getLanguageConfig();

    @FXML
    private FontAwesomeIconView iconEdit;

    @FXML
    private Label lblContent;

    @FXML
    private ImageView imgReceiverAvatar, imgSenderAvatar, imgPhoto;

    @FXML
    private JFXButton btnEdit, btnDelete, btnShowTweet;

    @FXML
    private JFXTextArea txtEditMessage;

    private Message message;

    @FXML
    void deleteMessage(ActionEvent event) throws ConnectionException {
        context.messages.delete(message);
        updateCard();
    }

    @FXML
    void editMessage(ActionEvent event) throws ConnectionException {
        if (txtEditMessage.isVisible()) {
            txtEditMessage.setVisible(false);
            lblContent.setVisible(true);
            message.setContent(txtEditMessage.getText());
            try {
                context.messages.save(message);
            }
            catch (ValidationException e) {
                logger.error("validation error while editing");
                logger.error(e.getLog());
            }
            lblContent.setText(message.getContent());
            iconEdit.setGlyphName(languageConfig.getProperty("EDIT_ICON"));
            updateCard();
        }
        else {
            txtEditMessage.setVisible(true);
            lblContent.setVisible(false);
            txtEditMessage.setText(message.getContent());
            iconEdit.setGlyphName(languageConfig.getProperty("ACCEPT_ICON"));
        }
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

    public void setMessage(Message message) throws ConnectionException {
        this.message = message;
        updateCard();
    }

    public void updateCard() throws ConnectionException {
        if (message.isDeleted) {
            lblContent.setText("DELETED");
            return;
        }
        lblContent.setText(message.getContent());
        if (message.getTweetId() != 0) {
            btnShowTweet.setVisible(true);
            lblContent.setVisible(false);
        }
        if (message.getPhoto() != null)
            imgPhoto.setImage(context.images.load(message.getPhoto()));
        if (State.getCurrentUserId() == message.getSender()) {
            new Thread(() -> {
                try {
                    imgSenderAvatar.setVisible(true);
                    ImageDBSet imageDBSet = new ImageDBSet();
                    if (State.getUser().getPhoto() != null)
                            imgSenderAvatar.setImage(imageDBSet.load(State.getUser().getPhoto()));
                    else
                        imgSenderAvatar.setVisible(false);
                } catch (ConnectionException e) {
                ViewManager.connectionFailed();
            }
            }).start();
            if (message.getTweetId() == 0) {
                btnDelete.setVisible(true);
                btnEdit.setVisible(true);
            }
        }
        else {
            imgReceiverAvatar.setVisible(true);
            User user = context.users.get(message.getSender());
            new Thread(() -> {
                try {
                    imgReceiverAvatar.setVisible(true);
                    ImageDBSet imageDBSet = new ImageDBSet();
                    if (user.getPhoto() != null)
                        imgReceiverAvatar.setImage(imageDBSet.load(user.getPhoto()));
                    else
                        imgReceiverAvatar.setVisible(false);
                } catch (ConnectionException e) {
                    ViewManager.connectionFailed();
                }
            }).start();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnDelete.setVisible(false);
        btnEdit.setVisible(false);
        imgSenderAvatar.setVisible(false);
        imgReceiverAvatar.setVisible(false);
        btnShowTweet.setVisible(false);
        txtEditMessage.setVisible(false);
        iconEdit.setGlyphName(languageConfig.getProperty("EDIT_ICON"));
    }
}
