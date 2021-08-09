package client.controllers.message;

import client.store.MyProfileStore;
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
    private JFXTextArea txtEditMessage;

    @FXML
    void deleteMessage(ActionEvent event) {

    }

    @FXML
    void editMessage(ActionEvent event) {

    }

    public void showMessage(Message message) {
        txtEditMessage.setVisible(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(false);
        if (message.getSender().id == MyProfileStore.getInstance().getUser().id) {
            btnDelete.setVisible(true);
            btnEdit.setVisible(true);
            if (message.getSender().getPhoto() != null)
                imgSenderAvatar.setImage(ImageUtils.load(message.getSender().getPhoto()));
        }
        else if (message.getSender().getPhoto() != null)
            imgReceiverAvatar.setImage(ImageUtils.load(message.getSender().getPhoto()));
        lblContent.setText(message.getContent());
        if (message.getPhoto() != null)
            imgPhoto.setImage(ImageUtils.load(message.getPhoto()));
    }
}
