package apps.tweet.controller;

import apps.auth.State;
import controller.MessageController;
import controller.UserController;
import db.dbSet.ImageDBSet;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.ChatGroup;
import model.Group;
import model.User;
import model.Tweet;
import com.jfoenix.controls.JFXButton;
import controller.Controller;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;
import view.ForwardListDialog;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class TweetCardController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(TweetCardController.class);
    private Config tweetAppConfig = Config.getConfig("TWEET_APP_CONFIG");
    private Config languageConfig = Config.getLanguageConfig();

    private Tweet currentTweet;

    @FXML
    private Label lblAuthor, lblTweet;

    @FXML
    private ImageView imgAvatar, imgPhoto;

    @FXML
    private JFXButton btnSave, btnShare, btnRetweet, btnComment, btnLike, btnMute, btnReport, btnParentTweet;

    @FXML
    private HBox hboxForeign;

    @FXML
    private FontAwesomeIconView iconLike;

    private final UserController userController = new UserController();
    private final controller.TweetController tweetController =  new controller.TweetController();
    private final MessageController messageController = new MessageController();

    @FXML
    void btnCommentClicked(ActionEvent event) throws ConnectionException {
        ViewManager.mainPanelController.showTweetComments(currentTweet);
    }

    @FXML
    void btnLikeClicked(ActionEvent event) throws ConnectionException {
        User user = apps.auth.State.getUser();
        if (iconLike.getGlyphName().equals(languageConfig.getProperty("LIKE_FILLED_ICON"))) {
            tweetController.dislikeTweet(currentTweet, user);
            iconLike.setGlyphName(languageConfig.getProperty("LIKE_FILLED_ICON"));
        }
        else {
            tweetController.likeTweet(currentTweet, user);
            iconLike.setGlyphName(languageConfig.getProperty("LIKE_EMPTY_ICON"));
        }
        updateCard();
    }

    @FXML
    void btnMuteClicked(ActionEvent event) throws ConnectionException {
        userController.muteUser(apps.auth.State.getUser(), context.users.get(currentTweet.getAuthor()));
        updateCard();
    }

    @FXML
    void btnReportClicked(ActionEvent event) throws ConnectionException {
        tweetController.reportTweet(currentTweet, State.getUser());
    }

    @FXML
    void btnRetweetClicked(ActionEvent event) throws ConnectionException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(tweetAppConfig.getProperty("TWEET_INPUT_VIEW")));
            Pane pane = fxmlLoader.load();
            TweetInputController inputController = fxmlLoader.getController();
            inputController.setParentRetweet(currentTweet.id);
            ViewManager.mainPanelController.addStackPaneNode(pane);
        }
        catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    @FXML
    void btnSaveClicked(ActionEvent event) throws ConnectionException {
        try {
            Objects.requireNonNull(State.getUser());
            messageController.sendMessage(State.getUser(), State.getUser(),
                    currentTweet.id);
        }
        catch (ValidationException e) {
           logger.error("validation error while saving a tweet");
           logger.error(e.getLog());
        }
    }

    @FXML
    void btnShareClicked(ActionEvent event) throws ConnectionException {
        Objects.requireNonNull(State.getUser());
        ForwardListDialog.show((users, groups, chatGroups) -> {
            try {
                HashSet<User> sendUser = new HashSet<>(users);
                for (Group group : groups) {
                    for (int userId : group.getUsers())
                        sendUser.add(context.users.get(userId));
                }
                for (User user: sendUser)
                    messageController.sendMessage(State.getUser(), user, currentTweet.id);
                for (ChatGroup chatGroup: chatGroups)
                    messageController.sendMessage(State.getUser(), chatGroup, currentTweet.id);
            }
            catch (ConnectionException e) {
                ViewManager.connectionFailed();
            } catch (ValidationException e) {
                logger.error("validation error while sharing a tweet");
                logger.error(e.getLog());
            }
        });
    }

    public void setCurrentTweet(Tweet currentTweet) throws ConnectionException {
        this.currentTweet = currentTweet;
        updateCard();
    }

    @FXML
    void showParentTweet(ActionEvent event) throws ConnectionException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(tweetAppConfig.getProperty("TWEET_CARD_VIEW")));
            Pane pane = fxmlLoader.load();
            TweetCardController controller = fxmlLoader.getController();
            Tweet tweet;
            if (currentTweet.getReTweet() != 0)
                tweet = context.tweets.get(currentTweet.getReTweet());
            else
                tweet = context.tweets.get(currentTweet.getParentTweet());
            controller.setCurrentTweet(tweet);
            ViewManager.mainPanelController.addStackPaneNode(pane);
        }
        catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    public void updateCard() throws ConnectionException {
        User user = apps.auth.State.getUser();
        if (currentTweet.getPhoto() != null)
            imgPhoto.setImage(context.images.load(currentTweet.getPhoto()));
        currentTweet = context.tweets.get(currentTweet.id);
        lblAuthor.setText("@" + context.users.get(currentTweet.getAuthor()).getUsername());
        if (currentTweet.getReTweet() != 0) {
            String name = context.users.get(context.tweets.get(currentTweet.getReTweet()).getAuthor()).getUsername();
            lblAuthor.setText(lblAuthor.getText() + " Retweeted a tweet by @" + name);
            btnParentTweet.setVisible(true);
        }
        if (currentTweet.getParentTweet() != 0) {
            String name = context.users.get(context.tweets.get(currentTweet.getParentTweet()).getAuthor()).getUsername();
            lblAuthor.setText(lblAuthor.getText() + " commented on a tweet by @" + name);
            btnParentTweet.setVisible(true);
        }
        lblTweet.setText(currentTweet.getContent());
        if (currentTweet.containsLike(user))
            iconLike.setGlyphName(languageConfig.getProperty("LIKE_FILLED_ICON"));
        else
            iconLike.setGlyphName(languageConfig.getProperty("LIKE_EMPTY_ICON"));
        if (currentTweet.getAuthor() == user.id)
            hboxForeign.setVisible(false);
        User target = context.users.get(currentTweet.getAuthor());
        new Thread(() -> {
            ImageDBSet imageDBSet = new ImageDBSet();
            if (target.getPhoto() != null) {
                try {
                    imgAvatar.setVisible(true);
                    imgAvatar.setImage(imageDBSet.load(target.getPhoto()));
                } catch (ConnectionException e) {
                    ViewManager.connectionFailed();
                }
            }
            else
                imgAvatar.setVisible(false);
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnParentTweet.setVisible(false);
    }
}

