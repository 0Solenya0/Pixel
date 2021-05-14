package apps.messenger.controller;

import apps.auth.State;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import controller.Controller;
import controller.MainPanelController;
import controller.MessageController;
import controller.RelationController;
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
import view.StringDialog;
import view.SuccessDialog;
import view.UserListDialog;
import view.ViewManager;

import javax.swing.text.View;
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
    private JFXButton btnSend, btnAddUser, btnUserList;

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
    private RelationController relationController = new RelationController();

    @FXML
    void addUser() throws ConnectionException {
        Objects.requireNonNull(State.getUser());
        ArrayList<User> users = relationController.getFollowing(State.getUser());
        ArrayList<User> res = new ArrayList<>();
        for (User user: users)
            if (!group.isMember(user.id))
                res.add(user);
        UserListDialog.show(res, selected -> {
            for (User user: selected)
                group.addMember(user);
            try {
                context.chatGroups.save(group);
            } catch (ConnectionException e) {
                ViewManager.connectionFailed();
            } catch (ValidationException e) {
                //TO DO
            }
        });
    }

    @FXML
    void createGroup() {
        StringDialog.show("Enter Group name", s -> {
            try {
                group = new ChatGroup(s, State.getCurrentUserId());
                context.chatGroups.save(group);
            } catch (ConnectionException e) {
                ViewManager.connectionFailed();
            } catch (ValidationException e) {
                //TO DO
            }
        });
        updatePage();
    }

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
            System.out.println(e.getLog());
        }
    }

    public void showUserList() throws ConnectionException {
        ArrayList<User> users = new ArrayList<>();
        for (int userId: group.getUsers())
            users.add(context.users.get(userId));
        UserListDialog.show(users, u -> { });
    }

    public void updateMessagesPane(User user) throws ConnectionException {
        this.user = user;
        this.group = null;
        btnUserList.setVisible(false);
        btnAddUser.setVisible(false);
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
                message.addView(State.getUser());
                context.messages.save(message);
                vboxMessage.getChildren().add(pane);
            }
            catch (IOException e) {
                logger.error("failed to load view fxml file");
                e.printStackTrace();
            }
            catch (ValidationException e) {
                //TO DO
            }
        }
        messageScrollPane.setVvalue(messageScrollPane.getVmax());
        updatePage();
    }

    public void updateMessagesPane(ChatGroup group) throws ConnectionException {
        this.group = group;
        this.user = null;
        btnUserList.setVisible(true);
        btnAddUser.setVisible(true);
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
                message.addView(State.getUser());
                context.messages.save(message);
                vboxMessage.getChildren().add(pane);
            }
            catch (IOException e) {
                logger.error("failed to load view fxml file");
                e.printStackTrace();
            }
            catch (ValidationException e) {
                //TO DO
            }
        }
        messageScrollPane.setVvalue(messageScrollPane.getVmax());
        updatePage();
    }

    public void addGroupToList(ChatGroup chatGroup) throws IOException, ConnectionException {
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
        vboxChat.getChildren().add(pane);
    }

    public void addUserToList(User user) throws IOException, ConnectionException {
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

    public void updatePage() {
        try {
            TreeSet<Integer> users = new TreeSet<>();
            ArrayList<Message> dms = context.messages.getAll(
                    context.messages.getQueryBuilder()
                            .getRelatedToUser(Objects.requireNonNull(State.getUser()).id).getQuery()
            );
            for (Message message: dms) {
                if (message.getReceiver() != 0) {
                    users.add(context.users.get(message.getReceiver()).id);
                    users.add(context.users.get(message.getSender()).id);
                }
            }
            //TO DO Sort chats
            vboxChat.getChildren().clear();
            ArrayList<ChatGroup> chatGroups = context.chatGroups.getAll(
                    context.chatGroups.getQueryBuilder().getByMember(State.getCurrentUserId()).getQuery()
            );
            for (ChatGroup chatGroup: chatGroups) {
                Message m = messageController.getLastMessage(chatGroup);
                if (m != null && !m.hasViewed(State.getUser()))
                    addGroupToList(chatGroup);
            }
            for (Integer userId: users) {
                User user = context.users.get(userId);
                Message m = messageController.getLastMessage(State.getUser(), user);
                if (m != null && !m.hasViewed(State.getUser()))
                    addUserToList(user);
            }
            for (ChatGroup chatGroup: chatGroups) {
                Message m = messageController.getLastMessage(chatGroup);
                if (m == null || m.hasViewed(State.getUser()))
                    addGroupToList(chatGroup);
            }
            for (Integer userId: users) {
                User user = context.users.get(userId);
                Message m = messageController.getLastMessage(State.getUser(), user);
                if (m == null || m.hasViewed(State.getUser()))
                    addUserToList(user);
            }
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updatePage();
        lblChatName.setText("");
        sendMessagePane.setVisible(false);
        btnAddUser.setVisible(false);
        btnUserList.setVisible(false);
    }
}
