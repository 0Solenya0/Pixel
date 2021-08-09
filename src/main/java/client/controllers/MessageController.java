package client.controllers;

import client.controllers.message.ChatCardController;
import client.controllers.message.MessageCardController;
import client.request.SocketHandler;
import client.store.MessageStore;
import client.store.MyProfileStore;
import client.views.InfoDialog;
import client.views.StringDialog;
import client.views.UserListDialog;
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
import shared.util.Config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MessageController implements Initializable {

    private final Config config = Config.getLanguageConfig();

    @FXML
    private AnchorPane sendMessagePane;

    @FXML
    private JFXTextArea txtContent;

    @FXML
    private JFXButton btnSend, btnAddUser, btnUserList, btnLeave;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private VBox vboxMessage, vboxChat;

    @FXML
    private Label lblChatName;

    private User user;
    private Group group;
    private byte[] photo;

    @FXML
    void addUser(ActionEvent event) {
        MyProfileStore.getInstance().updateUserProfile();
        UserListDialog.show(MyProfileStore.getInstance().getUser().getFollowings(),
                (list) -> {
                    list.forEach((user) -> {
                        Packet packet = new Packet("group-action");
                        packet.put("type", "add-user");
                        packet.put("group-id", group.id);
                        packet.put("target-id", user.id);
                        SocketHandler.getSocketHandlerWithoutException().sendPacketAndListen(packet, (p) -> {});
                    });
                });
    }

    @FXML
    void attachImage(ActionEvent event) {
        File file = ViewManager.showFileDialog();
        try {
            photo = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            InfoDialog.showFailed(config.getProperty("IMAGE_LOAD_FAILED"));
        }
    }

    @FXML
    void createGroup(ActionEvent event) {
        String name = StringDialog.show("Group name");
        Packet packet = new Packet("create-group");
        packet.put("name", name);
        SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        updateData();
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
            canMessage = group.getUsers().contains(MyProfileStore.getInstance().getUser());
        }
        sendMessagePane.setVisible(canMessage);
    }

    public void setUserTarget(User user) {
        if (this.user != user)
            photo = null;
        group = null;
        this.user = user;
        checkForMessageAccess();
        lblChatName.setText(user.getUsername());
        setGroupButtonsVisibility(false);
        MessageStore.getInstance().updateData();
        ArrayList<Message> messages = MessageStore.getInstance().getByUser(user);
        showMessages(messages);
    }

    public void setGroupTarget(Group group) {
        if (this.group != group)
            photo = null;
        user = null;
        this.group = group;
        checkForMessageAccess();
        lblChatName.setText(group.getName());
        setGroupButtonsVisibility(true);
        MessageStore.getInstance().updateData();
        ArrayList<Message> messages = MessageStore.getInstance().getByGroup(group);
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
        btnLeave.setVisible(visibility);
    }

    @FXML
    void leaveGroup() {
        Packet packet = new Packet("group-action");
        packet.put("type", "leave");
        packet.put("group-id", group.id);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() == StatusCode.OK)
            updateData();
    }

    @FXML
    void sendMessage(ActionEvent event) {
        Message message = new Message();
        message.setContent(txtContent.getText());
        message.setSender(MyProfileStore.getInstance().getUser());
        message.setPhoto(photo);
        message.setPhoto(photo);
        if (user != null)
            message.setReceiver(user);
        else
            message.setReceiverGroup(group);
        MessageStore.getInstance().sendMessage(message);
        MessageStore.getInstance().commitChanges();
        txtContent.setText("");
        updateData();
    }

    @FXML
    void showUserList(ActionEvent event) {
        UserListDialog.show(group.getUsers(), (l) -> {});
    }

    void updateData() {
        if (user != null)
            setUserTarget(user);
        else if (group != null)
            setGroupTarget(group);
        vboxChat.getChildren().clear();
        MessageStore.getInstance().getChatGroups().forEach((g) -> {
            ViewManager.Component<ChatCardController> component = ViewManager.getComponent("CHAT_CARD");
            if (g instanceof User) {
                component.getController().setUser((User) g);
                component.getController().setClickListener(() -> this.setUserTarget((User) g));
            }
            else if (g instanceof Group) {
                component.getController().setGroup((Group) g);
                component.getController().setClickListener(() -> this.setGroupTarget((Group) g));
            }
            vboxChat.getChildren().add(component.getPane());
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MessageStore.getInstance().updateData();
        sendMessagePane.setVisible(false);
        lblChatName.setText("-");
        setGroupButtonsVisibility(false);
        updateData();
    }
}
