package client.controllers.components;

import client.request.SocketHandler;
import client.utils.ImageUtils;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import shared.models.Tweet;
import shared.request.Packet;

import java.net.URL;
import java.util.ResourceBundle;

public class TweetCardController {

    @FXML
    private Label lblAuthor, lblTweet;

    @FXML
    private JFXButton btnSave, btnShare, btnRetweet, btnComment, btnLike;

    @FXML
    private FontAwesomeIconView iconLike;

    @FXML
    private HBox hboxForeign;

    @FXML
    private JFXButton btnMute, btnReport;

    @FXML
    private JFXButton btnParentTweet;

    @FXML
    private ImageView imgAvatar, imgPhoto;

    private int tweetId, parentId;

    @FXML
    void btnCommentClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnLikeClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnMuteClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnReportClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnRetweetClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnSaveClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void btnShareClicked(ActionEvent event) {
        // TO DO
    }

    @FXML
    void showParentTweet(ActionEvent event) {
        // TO DO
    }

    public void updateData() {
        btnParentTweet.setVisible(false);

        Packet packet = new Packet("get-tweet");
        packet.put("id", tweetId);
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);

        Tweet tweet = response.getObject("tweet", Tweet.class);
        lblAuthor.setText(tweet.getAuthor().getUsername());
        lblTweet.setText(tweet.getContent());
        if (tweet.getPhoto() != null)
            imgPhoto.setImage(ImageUtils.load(tweet.getPhoto()));
        if (tweet.getAuthor().getPhoto() != null)
            imgAvatar.setImage(ImageUtils.load(tweet.getAuthor().getPhoto()));

        if (tweet.getParent() != null)
            parentId = tweet.getParent().id;

        btnParentTweet.setVisible(parentId != 0);

        hboxForeign.setVisible(!response.getBool("same-user"));

        iconLike.setIcon(response.getBool("liked") ?
                FontAwesomeIcon.HEART : FontAwesomeIcon.HEART_ALT);
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
        updateData();
    }
}