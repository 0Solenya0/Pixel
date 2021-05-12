package apps.messenger.controller;

import apps.auth.State;
import com.jfoenix.controls.JFXButton;
import controller.Controller;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Message;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageBoxCardController extends Controller implements Initializable {

    @FXML
    private FontAwesomeIconView iconEdit;

    @FXML
    private Label lblContent;

    @FXML
    private ImageView imgReceiverAvatar, imgSenderAvatar;

    @FXML
    private JFXButton btnEdit, btnDelete;

    private Message message;

    @FXML
    void deleteMessage(ActionEvent event) {

    }

    @FXML
    void editMessage(ActionEvent event) {

    }

    public void setMessage(Message message) {
        this.message = message;
        updateCard();
    }

    public void updateCard() {
        lblContent.setText(message.getContent());
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
    }
}
