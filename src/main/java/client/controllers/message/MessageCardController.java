package client.controllers.message;

import client.controllers.MessageController;
import client.request.SocketHandler;
import client.store.MessageStore;
import client.store.MyProfileStore;
import client.utils.ImageUtils;
import client.views.ViewManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import shared.models.Group;
import shared.models.Message;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import static client.views.HyperLinkTranslator.getHyperLink;

public class MessageCardController {

    @FXML
    private Label lblState;

    @FXML
    private ImageView imgReceiverAvatar, imgSenderAvatar, imgPhoto;

    @FXML
    private FontAwesomeIconView iconEdit;

    @FXML
    private JFXButton btnEdit, btnDelete;

    @FXML
    private JFXTextArea txtEditMessage;

    @FXML
    private TextFlow txtFlowContent;

    private boolean editMode;
    private Message message;

    private void setContent(String content) {
        int ls = 0;
        txtFlowContent.getChildren().clear();
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '@') {
                Text text = new Text(content.substring(ls, i));
                txtFlowContent.getChildren().add(text);
                i++;
                StringBuilder builder = new StringBuilder();
                for (; i < content.length() && content.charAt(i) != ' '; i++)
                    builder.append(content.charAt(i));
                ls = i;
                i--;
                Hyperlink hyperlink = getHyperLink(builder.toString());
                txtFlowContent.getChildren().add(hyperlink);
            }
        }
        Text text = new Text(content.substring(ls));
        txtFlowContent.getChildren().add(text);
    }

    @FXML
    void deleteMessage(ActionEvent event) {
        boolean success = false;
        if (message.id != 0) {
            Packet packet = new Packet("message-action");
            packet.put("type", "delete");
            packet.put("message-id", message.id);
            Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
            success = (res.getStatus() == StatusCode.OK);
        }
        else {
            MessageStore.getInstance().getPendingMessages().remove(message);
            success = true;
        }

        if (success) {
            btnDelete.setVisible(false);
            btnEdit.setVisible(false);
            setContent("Deleted!");
            txtEditMessage.setVisible(false);
            txtFlowContent.setVisible(true);
        }
    }

    @FXML
    void editMessage(ActionEvent event) {
        editMode = !editMode;
        txtFlowContent.setVisible(!editMode);
        txtEditMessage.setVisible(editMode);
        iconEdit.setIcon(editMode ? FontAwesomeIcon.CHECK : FontAwesomeIcon.PENCIL);
        if (editMode)
            txtEditMessage.setText(message.getContent());
        else {
            if (message.id == 0) {
                message.setContent(txtEditMessage.getText());
                setContent(message.getContent());
            }
            else {
                Packet packet = new Packet("message-action");
                packet.put("type", "edit");
                packet.put("content", txtEditMessage.getText());
                packet.put("message-id", message.id);
                Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
                if (res.getStatus() == StatusCode.OK) {
                    message.setContent(txtEditMessage.getText());
                    setContent(message.getContent());
                }
            }
        }
    }

    public void showMessage(Message message) {
        this.message = message;
        txtEditMessage.setVisible(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(false);
        if (message.getSender().id == MyProfileStore.getInstance().getUser().id) {
            btnDelete.setVisible(true);
            btnEdit.setVisible(true);
            if (message.getSender().getPhoto() != null) {
                Thread thread = new Thread(() -> {
                    Image image = ImageUtils.load(message.getSender().getPhoto());
                    Platform.runLater(() -> imgSenderAvatar.setImage(image));
                });
                thread.start();
            }
        }
        else if (message.getSender().getPhoto() != null) {
            Thread thread = new Thread(() -> {
                Image image = ImageUtils.load(message.getSender().getPhoto());
                Platform.runLater(() -> imgReceiverAvatar.setImage(image));
            });
            thread.start();
        }
        setContent(message.getContent());
        if (message.getPhoto() != null) {
            Thread thread = new Thread(() -> {
                Image image = ImageUtils.load(message.getPhoto());
                Platform.runLater(() -> imgPhoto.setImage(image));
            });
            thread.start();
        }

        if (message.getSender().id == MyProfileStore.getInstance().getUser().id) {
            if (message.getViewers().size() > 1)
                lblState.setText("seen");
            else if (message.getDelivers().size() > 1)
                lblState.setText("delivered");
            else if (message.id == 0)
                lblState.setText("draft");
            else
                lblState.setText("sent");
        }

        if (message.id != 0) {
            Thread thread = new Thread(() -> {
                Packet packet = new Packet("message-action");
                packet.put("type", "see");
                packet.put("message-id", message.id);
                Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
            });
            thread.start();
        }
    }
}
