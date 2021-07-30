package client.controllers.components;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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

    @FXML
    void btnCommentClicked(ActionEvent event) {

    }

    @FXML
    void btnLikeClicked(ActionEvent event) {

    }

    @FXML
    void btnMuteClicked(ActionEvent event) {

    }

    @FXML
    void btnReportClicked(ActionEvent event) {

    }

    @FXML
    void btnRetweetClicked(ActionEvent event) {

    }

    @FXML
    void btnSaveClicked(ActionEvent event) {

    }

    @FXML
    void btnShareClicked(ActionEvent event) {

    }

    @FXML
    void showParentTweet(ActionEvent event) {

    }

}
