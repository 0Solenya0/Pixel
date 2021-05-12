package apps.messenger.controller;

import apps.auth.State;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import controller.Controller;
import controller.MainPanelController;
import controller.MessageController;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.ChatGroup;
import model.Message;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;
import view.SuccessDialog;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class MessengerController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(MessengerController.class);
    private final Config messengerAppConfig = Config.getConfig("MESSENGER_APP_CONFIG");

    @FXML
    private AnchorPane sendMessagePane;

    @FXML
    private JFXTextArea txtContent;

    @FXML
    private JFXButton btnSend;

    @FXML
    private Label lblChatName;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private VBox vboxMessage;

    @FXML
    private VBox vboxChat;

    private User user;
    private ChatGroup group;
    private MessageController messageController = new MessageController();

    @FXML
    void sendMessage(ActionEvent event) throws ConnectionException {
        Objects.requireNonNull(State.getUser());
        try {
            if (user != null) {
                messageController.sendMessage(State.getUser(), user, txtContent.getText());
                updateMessagesPane(user);
            }
            else if (group != null) {
                messageController.sendMessage(State.getUser(), group, txtContent.getText());
                updateMessagesPane(group);
            }
            txtContent.setText("");
        }
        catch (ValidationException e) {
            //TO DO handle errors
        }
    }

    public void updateMessagesPane(User user) throws ConnectionException {
        this.user = user;
        this.group = null;
        sendMessagePane.setVisible(true);
        lblChatName.setText(user.getUsername());
        Objects.requireNonNull(State.getUser());
        ArrayList<Message> messages = context.messages.getAll(
                context.messages.getQueryBuilder().getTwoUserChats(State.getUser().id, user.id).getQuery()
        );
        vboxMessage.getChildren().clear();
        for (Message message: messages) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(messengerAppConfig.getProperty("MESSAGE_CARD_VIEW")));
                Pane pane = fxmlLoader.load();
                MessageBoxCardController controller = fxmlLoader.getController();
                controller.setMessage(message);
                vboxMessage.getChildren().add(pane);
            }
            catch (IOException e) {
                logger.error("failed to load view fxml file");
                e.printStackTrace();
            }
        }
        messageScrollPane.setVvalue(1.0);
    }

    public void updateMessagesPane(ChatGroup group) throws ConnectionException {
        this.group = group;
        this.user = null;
        sendMessagePane.setVisible(true);
        lblChatName.setText(group.getName());
        Objects.requireNonNull(State.getUser());
        ArrayList<Message> messages = context.messages.getAll(
                context.messages.getQueryBuilder().getByGroup(group.id).getQuery()
        );
        vboxMessage.getChildren().clear();
        for (Message message: messages) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(messengerAppConfig.getProperty("MESSAGE_CARD_VIEW")));
                Pane pane = fxmlLoader.load();
                MessageBoxCardController controller = fxmlLoader.getController();
                controller.setMessage(message);
                vboxMessage.getChildren().add(pane);
            }
            catch (IOException e) {
                logger.error("failed to load view fxml file");
                e.printStackTrace();
            }
        }
        messageScrollPane.setVvalue(1.0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblChatName.setText("");
        sendMessagePane.setVisible(false);
        TreeSet<Integer> users = new TreeSet<>();
        try {
            ArrayList<Message> dms = context.messages.getAll(
                    context.messages.getQueryBuilder()
                    .getRelatedToUser(Objects.requireNonNull(State.getUser()).id).getQuery()
            );
            for (Message message: dms) {
                users.add(context.users.get(message.getReceiver()).id);
                users.add(context.users.get(message.getSender()).id);
            }
            //TO DO Sort chats
            vboxChat.getChildren().clear();
            ArrayList<ChatGroup> chatGroups = context.chatGroups.getAll(
                    context.chatGroups.getQueryBuilder().getByMember(State.getCurrentUserId()).getQuery()
            );
            for (ChatGroup chatGroup: chatGroups) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(messengerAppConfig.getProperty("CARD_VIEW")));
                Pane pane = fxmlLoader.load();
                ChatGroupCardController controller = fxmlLoader.getController();
                controller.setGroup(chatGroup);
                controller.setOnClickListener(s -> {
                    try {
                        updateMessagesPane(chatGroup);
                    } catch (ConnectionException e) {
                        ViewManager.connectionFailed();
                    }
                });
                vboxMessage.getChildren().add(pane);
            }
            for (Integer userId: users) {
                User user = context.users.get(userId);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(messengerAppConfig.getProperty("CARD_VIEW")));
                Pane pane = fxmlLoader.load();
                ChatGroupCardController controller = fxmlLoader.getController();
                controller.setUser(user);
                controller.setOnClickListener(s -> {
                    try {
                        updateMessagesPane(user);
                    }
                    catch (ConnectionException e) {
                        ViewManager.connectionFailed();
                    }
                });
                vboxChat.getChildren().add(pane);
            }
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }
}
