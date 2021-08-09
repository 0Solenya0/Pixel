package client.controllers.message;

import client.utils.ImageUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import shared.models.Message;

public class MessageCardController {

    @FXML
    private Label lblContent;

    @FXML
    private ImageView imgReceiverAvatar, imgSenderAvatar, imgPhoto;

    @FXML
    private JFXButton btnEdit, btnDelete;

    @FXML
    private FontAwesomeIconView iconEdit;

    @FXML
    private JFXTextArea txtEditMessage;

    @FXML
    void deleteMessage(ActionEvent event) {

    }

    @FXML
    void editMessage(ActionEvent event) {

    }

    public void showMessage(Message message) {
        lblContent.setText(message.getContent());
        if (message.getPhoto() != null)
            imgPhoto.setImage(ImageUtils.load(message.getPhoto()));
    }

}
