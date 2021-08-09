package client.controllers;

import client.controllers.message.MessageCardController;
import client.request.SocketHandler;
import client.store.Messages;
import client.store.MyProfile;
import client.views.ViewManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import shared.models.Group;
import shared.models.Message;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MessageController implements Initializable {

    @FXML
    private AnchorPane sendMessagePane;

    @FXML
    private JFXTextArea txtContent;

    @FXML
    private JFXButton btnSend, btnAddUser, btnUserList;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private VBox vboxMessage, vboxChat;

    @FXML
    private Label lblChatName;

    private User user;
    private Group group;

    @FXML
    void addUser(ActionEvent event) {
        // TO DO
    }

    @FXML
    void attachImage(ActionEvent event) {
        // TO DO
    }

    @FXML
    void createGroup(ActionEvent event) {
        // TO DO
    }

    private void checkForMessageAccess() {
        boolean canMessage = true;
        if (user != null) {
            Packet packet = new Packet("profile");
            packet.put("id", user.id);
            Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
            canMessage = res.getBool("can-message", true);
        }
        else {
            // TO DO access for groups
        }
        sendMessagePane.setVisible(canMessage);
    }

    public void setUserTarget(User user) {
        group = null;
        this.user = user;
        checkForMessageAccess();
        lblChatName.setText(user.getUsername());
        setGroupButtonsVisibility(false);
        Messages.getInstance().updateData();
        ArrayList<Message> messages = Messages.getInstance().getByUser(user);
        showMessages(messages);
    }

    private void showMessages(ArrayList<Message> messages) {
        vboxMessage.getChildren().clear();
        for (Message message: messages) {
            ViewManager.Component<MessageCardController> component = ViewManager.getComponent("MESSAGE_CARD");
            component.getController().showMessage(message);
            vboxMessage.getChildren().add(component.getPane());
        }
    }

    void setGroupButtonsVisibility(boolean visibility) {
        btnUserList.setVisible(visibility);
        btnAddUser.setVisible(visibility);
    }

    @FXML
    void sendMessage(ActionEvent event) {
        Message message = new Message();
        message.setContent(txtContent.getText());
        message.setSender(MyProfile.getInstance().getUser());
        if (user != null)
            message.setReceiver(user);
        else
            message.setReceiverGroup(group);
        Messages.getInstance().sendMessage(message);
        Messages.getInstance().commitChanges();
        txtContent.setText("");
        setUserTarget(user);
    }

    @FXML
    void showUserList(ActionEvent event) {
        // TO DO
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Messages.getInstance().updateData();
        sendMessagePane.setVisible(false);
        lblChatName.setText("-");
        btnAddUser.setVisible(false);
        btnUserList.setVisible(false);
    }
}
