package apps.auth.controller;

import apps.auth.State;
import apps.auth.model.User;
import apps.relation.model.Relation;
import apps.relation.model.field.RelStatus;
import com.jfoenix.controls.JFXButton;
import config.Config;
import controller.Controller;
import db.exception.ConnectionException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable {
    private Config config = Config.getLanguageConfig();

    @FXML
    private Label lblFullName, lblLastSeen, lblBirthday, lblUsername, lblBio, lblPastTweets,
            lblFollowing, lblFollower, lblBlackList, lblBlackListCnt, lblFollowerCnt, lblFollowingCnt;

    @FXML
    private JFXButton btnToggleFollow, btnFollowing, btnFollower, btnToggleBlock, btnMessage, btnReport, btnBlackList;

    @FXML
    private FontAwesomeIconView iconToggleFollow, iconToggleBlock;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private HBox hboxActions;

    @FXML
    private AnchorPane tweetPane, BirthdayPane;

    User userModel;

    @FXML
    void toggleBlockUser(ActionEvent event) {

    }

    @FXML
    void reportUser(ActionEvent event) {

    }

    @FXML
    void sendMessage(ActionEvent event) {

    }

    @FXML
    void toggleFollow(ActionEvent event) {

    }

    private void updateData() throws ConnectionException {
        lblBlackListCnt.setVisible(false);
        btnBlackList.setVisible(false);
        lblBlackList.setVisible(false);
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
            lblBlackListCnt.setText(String.valueOf(context.relations.getFollowing(userModel).size()));
        }

        userModel = context.users.getByAccess(Objects.requireNonNull(State.getUser()), userModel.id);

        if (userModel.getBirthdate().get() != LocalDate.MIN)
            lblBirthday.setText(config.getProperty("BIRTHDAY") + ": " + userModel.getBirthdate());
        else
            BirthdayPane.setVisible(false);
        if (userModel.getLastseen().get() != LocalDateTime.MIN)
            lblLastSeen.setText(config.getProperty("LAST_SEEN") + ": " + userModel.getLastseen().get());
        else
            lblLastSeen.setText(config.getProperty("LAST_SEEN") + ": last seen recently");
        lblBio.setText(userModel.getBio());
        lblUsername.setText(userModel.getUsername());
        lblFullName.setText(userModel.getFullName());
        lblFollowerCnt.setText(String.valueOf(context.relations.getFollowers(userModel).size()));
        lblFollowingCnt.setText(String.valueOf(context.relations.getFollowing(userModel).size()));
    }

    public void setUser(User user) throws ConnectionException {
        userModel = user;
        updateData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblFollowing.setText(config.getProperty("FOLLOWING"));
        lblFollower.setText(config.getProperty("FOLLOWER"));
        lblBlackList.setText(config.getProperty("BLACKLIST"));
        lblPastTweets.setText(config.getProperty("PAST_TWEETS"));
    }
}
