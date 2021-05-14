package apps.messenger.controller;

import apps.auth.State;
import controller.Controller;
import controller.RelationController;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Group;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Config;
import view.StringDialog;
import view.UserListDialog;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class GroupManagementController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(GroupManagementController.class);
    private final Config messengerAppConfig = Config.getConfig("MESSENGER_APP_CONFIG");

    @FXML
    private VBox vboxGroupList;

    @FXML
    private AnchorPane groupPane;

    @FXML
    private ListView<String> listViewUsers;

    private ArrayList<User> currentGroupUsers;
    private Group group;

    @FXML
    void addUser(ActionEvent event) throws ConnectionException {
        RelationController relationController = new RelationController();
        UserListDialog.show(relationController.getFollowingAndFollower(State.getUser()), users ->{
            for (User user: users)
                group.addUser(user.id);
            try {
                context.groups.save(group);
                showGroup(group);
            }
            catch (ConnectionException e) {
                ViewManager.connectionFailed();
            }
            catch (ValidationException e) {
                logger.error("validation error while adding a user to a group");
                logger.error(e.getLog());
            }
        });
    }

    @FXML
    void createGroup(ActionEvent event) {
        StringDialog.show("Enter group name", s -> {
            Group group = new Group(State.getCurrentUserId(), s);
            try {
                context.groups.save(group);
                updateCard();
            }
            catch (ConnectionException e) {
                ViewManager.connectionFailed();
            }
            catch (ValidationException e) {
                logger.error("validation error while creating a group");
                logger.error(e.getLog());
            }
        });
    }

    @FXML
    void deleteGroup(ActionEvent event) throws ConnectionException {
        context.groups.delete(group);
        updateCard();
    }

    @FXML
    void deleteUser(ActionEvent event) throws ConnectionException {
        for (int item: listViewUsers.getSelectionModel().getSelectedIndices())
            group.deleteUser(currentGroupUsers.get(item).id);
        try {
            context.groups.save(group);
            showGroup(group);
        }
        catch (ValidationException e) {
            logger.error("validation error while deleting user from group");
            logger.error(e.getLog());
        }
    }

    public void showGroup(Group group) throws ConnectionException {
        groupPane.setVisible(true);
        this.group = group;
        currentGroupUsers = new ArrayList<>();
        for (int userId: group.getUsers()) {
            User user = context.users.get(userId);
            currentGroupUsers.add(user);
        }
        listViewUsers.getItems().clear();
        for (User user: currentGroupUsers)
            listViewUsers.getItems().add(user.getUsername());
    }

    public void updateCard() throws ConnectionException {
        groupPane.setVisible(false);
        vboxGroupList.getChildren().clear();
        for (Group group:
                context.groups.getAll(context.groups.getQueryBuilder().getByOwner(State.getCurrentUserId()).getQuery())) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(messengerAppConfig.getProperty("CARD_VIEW")));
            try {
                Pane pane = fxmlLoader.load();
                Button tmp = new Button(group.getName());
                tmp.setPrefSize(323, 50);
                tmp.setOnAction(event -> {
                    try {
                        showGroup(group);
                    }
                    catch (ConnectionException e) {
                        ViewManager.connectionFailed();
                    }
                });
                vboxGroupList.getChildren().add(tmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            updateCard();
        }
        catch (ConnectionException e) {
            ViewManager.connectionFailed();
        }
    }
}
