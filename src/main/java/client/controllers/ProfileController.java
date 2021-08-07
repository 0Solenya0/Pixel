package client.controllers;

import client.controllers.components.TweetListController;
import client.request.SocketHandler;
import client.utils.ImageUtils;
import client.views.AutoUpdate;
import client.views.UserListDialog;
import client.views.ViewManager;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
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
import shared.models.Tweet;
import shared.models.User;
import shared.models.fields.AccessLevel;
import shared.request.Packet;
import shared.request.StatusCode;
import shared.util.Config;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfileController {

    private Config config = Config.getLanguageConfig();
    private Config mainConfig = Config.getConfig("mainConfig");

    @FXML
    private ImageView imgAvatar;

    @FXML
    private Label lblFullName;

    @FXML
    private AnchorPane tweetPane;

    @FXML
    private Label lblPastTweets;

    @FXML
    private Label lblFollowing;

    @FXML
    private Label lblFollower;

    @FXML
    private Label lblFollowingCnt;

    @FXML
    private Label lblFollowerCnt;

    @FXML
    private Label lblLastSeen;

    @FXML
    private JFXButton btnToggleFollow;

    @FXML
    private FontAwesomeIconView iconToggleFollow;

    @FXML
    private AnchorPane BirthdayPane;

    @FXML
    private Label lblBirthday;

    @FXML
    private JFXButton btnFollowing;

    @FXML
    private JFXButton btnFollower;

    @FXML
    private Label lblUsername;

    @FXML
    private HBox hboxActions;

    @FXML
    private JFXButton btnToggleBlock;

    @FXML
    private FontAwesomeIconView iconToggleBlock;

    @FXML
    private JFXButton btnMessage, btnReport, btnMute;

    @FXML
    private FontAwesomeIconView iconMute;

    @FXML
    private Label lblBlackList, lblBlackListCnt;

    @FXML
    private JFXButton btnBlackList;

    @FXML
    private AnchorPane mailPane, phonePane;

    @FXML
    private Label lblPhone, lblRequested, lblEmail, lblBio;

    private int userId;
    private User user;
    private Packet res;
    private AutoUpdate updater = new AutoUpdate();

    @FXML
    void reportUser(ActionEvent event) {
        // TO DO
    }

    @FXML
    void sendMessage(ActionEvent event) {
        // TO DO
    }

    @FXML
    void showBlackList(ActionEvent event) {
        UserListDialog.show(user.getBlocked(), (u) -> {});
    }

    @FXML
    void showFollowerList(ActionEvent event) {
        UserListDialog.show(user.getFollowers(), (u) -> {});
    }

    @FXML
    void showFollowingList(ActionEvent event) {
        UserListDialog.show(user.getFollowings(), (u) -> {});
    }

    @FXML
    void toggleBlockUser(ActionEvent event) {
        Packet packet = new Packet("action");
        packet.put("type", "toggle-block");
        packet.put("target-id", user.id);
        SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        updateData();
    }

    @FXML
    void toggleFollow(ActionEvent event) {
        // TO DO
    }

    @FXML
    void toggleMute(ActionEvent event) {
        // TO DO
    }

    private void setListsVisibility(boolean visibility) {
        btnFollower.setVisible(visibility);
        btnFollowing.setVisible(visibility);
        btnBlackList.setVisible(visibility);
        lblFollower.setVisible(visibility);
        lblFollowing.setVisible(visibility);
        lblBlackList.setVisible(visibility);
        lblBlackListCnt.setVisible(visibility);
        lblFollowerCnt.setVisible(visibility);
        lblFollowingCnt.setVisible(visibility);
    }

    private void setBlackListVisibility(boolean visibility) {
        lblBlackList.setVisible(visibility);
        btnBlackList.setVisible(visibility);
        lblBlackListCnt.setVisible(visibility);
    }

    private boolean checkForAccess(AccessLevel level) {
        return  (level == AccessLevel.PUBLIC) ||
                (level == AccessLevel.CONTACTS && res.getBool("is-contact")) ||
                res.getBool("is-user");
    }

    void updateData() {
        DateTimeFormatter birthdayFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter lastSeenFormat = DateTimeFormatter.ofPattern("dd MMM HH:mm");
        BirthdayPane.setVisible(false);
        mailPane.setVisible(false);
        phonePane.setVisible(false);

        btnMessage.setVisible(false);
        btnReport.setVisible(true);
        btnToggleBlock.setVisible(true);

        lblLastSeen.setText(config.getProperty("LAST_SEEN_BLOCKED"));
        lblRequested.setVisible(false);

        btnToggleFollow.setVisible(false);

        setListsVisibility(false);

        ViewManager.Component<TweetListController> tweetList = ViewManager.getComponent("TWEET_LIST");

        Packet packet = new Packet("profile");
        packet.put("id", userId);
        res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);

        if (res.getStatus() == StatusCode.NOT_FOUND) {
            lblUsername.setText("User not found!");
            return;
        }

        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        user = res.getObject("user", User.class);
        user.setFollowers(res.getObject("followers", listType));
        user.setFollowings(res.getObject("following", listType));
        user.setBlocked(res.getObject("blocked", listType));
        lblUsername.setText(user.getUsername());
        lblFullName.setText(user.getFullName());
        lblBio.setText(user.getBio());

        tweetPane.getChildren().removeAll();
        tweetPane.getChildren().add(tweetList.getPane());
        TweetListController controller = tweetList.getController();
        controller.setHeight(465);
        if (res.getBool("is-blocked"))
            controller.setMessage(config.getProperty("BLOCKED"));
        else if (!checkForAccess(user.getVisibility()))
            controller.setMessage(config.getProperty("PRIVATE"));
        else
            controller.showUserTweets(userId);

        iconToggleBlock.setIcon(res.getBool("is-blocked") ?
                FontAwesomeIcon.CHECK : FontAwesomeIcon.BAN);
        if (res.getBool("is-blocked"))
            return;

        setListsVisibility(true);
        if (user.blocked.size() > 0) {
            setBlackListVisibility(true);
            lblBlackListCnt.setText(String.valueOf(user.blocked.size()));
        }
        lblFollowingCnt.setText(String.valueOf(user.followings.size()));
        lblFollowerCnt.setText(String.valueOf(user.followers.size()));
        if (res.getBool("follow-requested")) {
            lblRequested.setVisible(true);
            iconToggleFollow.setIcon(FontAwesomeIcon.USER_TIMES);
        }
        else
            iconToggleFollow.setIcon(res.getBool("is-contact") ?
                    FontAwesomeIcon.USER_TIMES : FontAwesomeIcon.USER_PLUS);

        btnToggleFollow.setVisible(true);

        iconMute.setIcon(res.getBool("is-muted") ? FontAwesomeIcon.MUSIC : FontAwesomeIcon.DEAF);

        if (user.getPhoto() != null)
            imgAvatar.setImage(ImageUtils.load(user.getPhoto()));
        btnMessage.setVisible(res.getBool("can-message", false));

        if (checkForAccess(user.getMail().getAccessLevel())) {
            mailPane.setVisible(true);
            lblEmail.setText(user.getMail().get());
        }

        if (checkForAccess(user.getPhone().getAccessLevel()) && user.getPhone().get() != null) {
            phonePane.setVisible(true);
            lblPhone.setText(user.getPhone().get());
        }

        if (checkForAccess(user.getBirthdate().getAccessLevel()) && user.getBirthdate().get() != null) {
            BirthdayPane.setVisible(true);
            lblBirthday.setText(user.getBirthdate().get().format(birthdayFormat));
        }

        if (checkForAccess(user.getLastSeen().getAccessLevel())) {
            if (res.getBool("online"))
                lblLastSeen.setText(config.getProperty("ONLINE"));
            else
                lblLastSeen.setText(user.getLastSeen().get().format(lastSeenFormat));
        }
        else
            lblLastSeen.setText(config.getProperty("LAST_SEEN_RECENTLY"));

        if (res.getBool("is-user")) {
            btnToggleFollow.setVisible(false);
            btnMute.setVisible(false);
            btnReport.setVisible(false);
            btnToggleBlock.setVisible(false);
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        updateData();
        updater.setTask(
                this::updateData,
                mainConfig.getProperty(Integer.class, "PROFILE_REFRESH_RATE")
        );
    }
}
