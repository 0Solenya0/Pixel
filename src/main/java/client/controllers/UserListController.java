package client.controllers;

import client.request.SocketHandler;
import client.store.MyProfileStore;
import client.views.AutoUpdate;
import client.views.StringDialog;
import client.views.UserListDialog;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import shared.models.User;
import shared.models.UserList;
import shared.request.Packet;
import shared.request.StatusCode;
import shared.util.Config;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserListController implements Initializable {

    private Config config = Config.getConfig("mainConfig");

    @FXML
    private VBox vboxGroupList;

    @FXML
    private AnchorPane groupPane;

    @FXML
    private ListView<User> listViewUsers;

    private UserList userList;
    private AutoUpdate updater = new AutoUpdate();

    @FXML
    void addUser(ActionEvent event) {
        MyProfileStore.getInstance().updateUserProfile();
        UserListDialog.show(MyProfileStore.getInstance().getUser().getFollowings(), (users) -> {
            for (User user: users) {
                Packet packet = new Packet("user-list-action");
                packet.put("type", "add-user");
                packet.put("list-id", userList.id);
                packet.put("target-id", user.id);
                Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
                if (res.getStatus() == StatusCode.OK)
                    updateData();
            }
        });
    }

    @FXML
    void createGroup(ActionEvent event) {
        Packet packet = new Packet("user-list-action");
        packet.put("type", "create-list");
        packet.put("name", StringDialog.show("List name"));
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() == StatusCode.OK)
            updateData();
    }

    @FXML
    void deleteGroup(ActionEvent event) {
        if (userList == null)
            return;
        Packet packet = new Packet("user-list-action");
        packet.put("type", "delete-list");
        packet.put("list-id", userList.id);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() == StatusCode.OK) {
            userList = null;
            updateData();
        }
    }

    @FXML
    void deleteUser(ActionEvent event) {
        MyProfileStore.getInstance().updateUserProfile();

        for (User user: listViewUsers.getSelectionModel().getSelectedItems()) {
            Packet packet = new Packet("user-list-action");
            packet.put("type", "delete-user");
            packet.put("list-id", userList.id);
            packet.put("target-id", user.id);
            Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        }
        updateData();
    }

    public void showUserList(UserList userList) {
        groupPane.setVisible(true);
        this.userList = userList;
        listViewUsers.getItems().clear();
        listViewUsers.getItems().addAll(userList.getUsers());
    }

    public void updateData() {
        if (userList == null)
            groupPane.setVisible(false);
        vboxGroupList.getChildren().clear();
        Packet packet = new Packet("user-list-list");
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() != StatusCode.OK)
            return;
        Type listType = new TypeToken<ArrayList<UserList>>() {}.getType();
        ArrayList<UserList> userLists = res.getObject("list", listType);


        int id = 0;
        if (userList != null)
            id = userList.id;
        userList = null;

        vboxGroupList.getChildren().clear();
        for (UserList list: userLists) {
            if (list.id == id)
                showUserList(list);
            Button tmp = new Button(list.getName());
            tmp.setPrefSize(323, 50);
            tmp.setOnAction(event -> showUserList(list));
            vboxGroupList.getChildren().add(tmp);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateData();
        updater.setTask(this::updateData, config.getProperty(Integer.class, "LIST_REFRESH_RATE"));
    }
}