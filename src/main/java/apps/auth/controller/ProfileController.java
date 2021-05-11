package apps.auth.controller;

import apps.auth.State;
import controller.RelationController;
import controller.UserController;
import model.User;
import model.field.AccessLevel;
import model.Notification;
import model.field.NotificationType;
import model.Relation;
import model.field.RelStatus;
import apps.tweet.controller.TweetListController;
import com.jfoenix.controls.JFXButton;
import util.Config;
import controller.Controller;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.UserListDialog;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(ProfileController.class);
    private final Config languageConfig = Config.getLanguageConfig();

    @FXML
    private Label lblFullName, lblLastSeen, lblBirthday, lblUsername, lblBio, lblPastTweets,
            lblFollowing, lblFollower, lblBlackList, lblBlackListCnt, lblFollowerCnt, lblFollowingCnt,
            lblEmail, lblPhone, lblRequested, lblJoinedAt;

    @FXML
    private JFXButton btnToggleFollow, btnFollowing, btnFollower, btnToggleBlock, btnMessage,
            btnReport, btnBlackList, btnMute;

    @FXML
    private FontAwesomeIconView iconToggleFollow, iconToggleBlock, iconMute;

    @FXML
    private ImageView imgAvatar; // TO DO

    @FXML
    private HBox hboxActions;

    @FXML
    private AnchorPane tweetPane, BirthdayPane, phonePane, mailPane;

    User userModel;

    private final RelationController relationController = new RelationController();
    private final UserController userController = new UserController();

    @FXML
    void toggleBlockUser(ActionEvent event) throws ConnectionException {
        if (iconToggleBlock.getGlyphName().equals(String.valueOf(FontAwesomeIcon.BAN)))
            relationController.block(Objects.requireNonNull(State.getUser()), userModel);
        else
            relationController.resetRel(Objects.requireNonNull(State.getUser()), userModel);
        updateData();
    }

    @FXML
    void reportUser(ActionEvent event) throws ConnectionException {
        try {
            Notification notification = new Notification(
                    Objects.requireNonNull(State.getUser()).id, userModel.id, NotificationType.REPORT);
            context.notifications.save(notification);
        }
        catch (ValidationException e) {
            logger.error("unexpected retweet validation failed");
        }
    }

    @FXML
    void sendMessage(ActionEvent event) {

    }

    @FXML
    void toggleMute(ActionEvent event) throws ConnectionException {
        if (iconMute.getGlyphName().equals(String.valueOf(FontAwesomeIcon.DEAF)))
            userController.muteUser(State.getUser(), userModel);
        else
            userController.unMuteUser(State.getUser(), userModel);
        updateData();
    }

    @FXML
    void showBlackList(ActionEvent event) throws ConnectionException {
        ArrayList<User> users = relationController.getBlackList(userModel);
        UserListDialog.show(users);
    }

    @FXML
    void showFollowerList(ActionEvent event) throws ConnectionException {
        ArrayList<User> users = relationController.getFollowers(userModel);
        UserListDialog.show(users);
    }

    @FXML
    void showFollowingList(ActionEvent event) throws ConnectionException {
        ArrayList<User> users = relationController.getFollowing(userModel);
        UserListDialog.show(users);
    }

    @FXML
    void toggleFollow(ActionEvent event) throws ConnectionException {
        if (iconToggleFollow.getGlyphName().equals(String.valueOf(FontAwesomeIcon.USER_PLUS)))
            relationController.follow(Objects.requireNonNull(State.getUser()), userModel);
        else
            relationController.resetRel(Objects.requireNonNull(State.getUser()), userModel);
        updateData();
    }

    private void updateData() throws ConnectionException {
        iconToggleBlock.setGlyphName(String.valueOf(FontAwesomeIcon.BAN));
        btnToggleFollow.setVisible(true);
        btnMessage.setVisible(true);
        btnFollower.setVisible(true);
        btnFollowing.setVisible(true);
        lblFollower.setVisible(true);
        lblFollowerCnt.setVisible(true);
        lblFollowing.setVisible(true);
        lblFollowingCnt.setVisible(true);
        phonePane.setVisible(false);
        mailPane.setVisible(false);

        lblBlackListCnt.setVisible(false);
        btnBlackList.setVisible(false);
        lblBlackList.setVisible(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        lblJoinedAt.setText(languageConfig.getProperty("JOINED_AT") + " " + userModel.getCreatedAt().format(formatter));

        Relation relation = context.relations.getFirst(
                context.relations.getQueryBuilder()
                        .getByTwoUser(Objects.requireNonNull(State.getUser()).id, userModel.id)
                        .getQuery()
        );
        RelStatus relStatus = null;
        if (relation != null)
            relStatus = relation.getType();
        if (relStatus == RelStatus.BLOCKED) {
            btnToggleFollow.setVisible(false);
            btnMessage.setVisible(false);
            btnFollower.setVisible(false);
            btnFollowing.setVisible(false);
            lblFollower.setVisible(false);
            lblFollowerCnt.setVisible(false);
            lblFollowing.setVisible(false);
            lblFollowingCnt.setVisible(false);
        }
        if (Objects.requireNonNull(State.getUser()).id == userModel.id) {
            lblBlackListCnt.setVisible(true);
            btnBlackList.setVisible(true);
            lblBlackList.setVisible(true);
            btnToggleFollow.setVisible(false);
            hboxActions.setVisible(false);
            lblBlackListCnt.setText(String.valueOf(relationController.getBlackList(userModel).size()));
        }

        if (!State.getUser().isMuted(userModel))
            iconMute.setGlyphName(String.valueOf(FontAwesomeIcon.DEAF));
        else
            iconMute.setGlyphName(String.valueOf(FontAwesomeIcon.MUSIC));

        userModel = context.users.getByAccess(Objects.requireNonNull(State.getUser()), userModel.id);

        if (!userModel.getPhone().get().equals("")) {
            phonePane.setVisible(true);
            lblPhone.setText(userModel.getPhone().get());
        }
        if (!userModel.getMail().get().equals("")) {
            mailPane.setVisible(true);
            lblEmail.setText(userModel.getMail().get());
        }
        if (!userModel.getBirthdate().get().equals(LocalDate.MIN))
            lblBirthday.setText(languageConfig.getProperty("BIRTHDAY") + ": " + userModel.getBirthdate());
        else
            BirthdayPane.setVisible(false);
        if (!userModel.getLastseen().get().equals(LocalDateTime.MIN))
            lblLastSeen.setText(languageConfig.getProperty("LAST_SEEN") + " at " + userModel.getLastseen().get());
        else
            lblLastSeen.setText(languageConfig.getProperty("LAST_SEEN_RECENTLY"));
        lblBio.setText(userModel.getBio());
        lblUsername.setText(userModel.getUsername());
        lblFullName.setText(userModel.getFullName());
        lblFollowerCnt.setText(String.valueOf(relationController.getFollowers(userModel).size()));
        lblFollowingCnt.setText(String.valueOf(relationController.getFollowing(userModel).size()));

        Notification request = context.notifications.getFirst(
                context.notifications.getQueryBuilder()
                        .getByTwoUser(State.getUser(), userModel)
                        .getByType(NotificationType.REQUEST)
                .getQuery()
        );
        btnToggleFollow.setDisable(request != null);
        lblRequested.setVisible(request != null);
        if (relation == null)
            iconToggleFollow.setGlyphName(String.valueOf(FontAwesomeIcon.USER_PLUS));
        else if (relation.getType() == RelStatus.FOLLOW)
            iconToggleFollow.setGlyphName(String.valueOf(FontAwesomeIcon.USER_TIMES));
        else if (relation.getType() == RelStatus.BLOCKED)
            iconToggleBlock.setGlyphName(String.valueOf(FontAwesomeIcon.CHECK));

        FXMLLoader fxmlLoader = TweetListController.getTweetListLoader();
        try {
            Pane pane = fxmlLoader.load();
            TweetListController tweetListController = fxmlLoader.getController();
            if (userModel.getVisibility() == AccessLevel.PRIVATE && relStatus != RelStatus.FOLLOW && State.getUser().id != userModel.id)
                tweetListController.privateList();
            else {
                tweetListController.addTweetList(context.tweets.getAll(
                        context.tweets.getQueryBuilder()
                                .getByAuthorId(userModel.id)
                                .getByParentTweet(0).getQuery()
                ));
            }
            tweetListController.resizeHeight(465);
            tweetPane.getChildren().add(pane);
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
        }
    }

    public void setUser(User user) throws ConnectionException {
        userModel = user;
        updateData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblFollowing.setText(languageConfig.getProperty("FOLLOWING"));
        lblFollower.setText(languageConfig.getProperty("FOLLOWER"));
        lblBlackList.setText(languageConfig.getProperty("BLACKLIST"));
        lblPastTweets.setText(languageConfig.getProperty("PAST_TWEETS"));
        lblRequested.setText(languageConfig.getProperty("REQUESTED"));
    }
}
