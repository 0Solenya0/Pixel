package client.views;

import client.request.SocketHandler;
import client.store.MessageStore;
import client.store.MyProfileStore;
import com.google.gson.reflect.TypeToken;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import shared.models.Group;
import shared.models.Message;
import shared.models.User;
import shared.models.UserList;
import shared.request.Packet;
import shared.request.StatusCode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class ForwardDialog {

    public static void show(String message) {
        Pane rootPane = new Pane();
        HBox hBox = new HBox();
        ListView<User> userListView = new ListView<User>();
        ListView<Group> groupListView = new ListView<Group>();
        ListView<UserList> listView = new ListView<UserList>();

        MyProfileStore.getInstance().updateUserProfile();
        userListView.getItems().addAll(MyProfileStore.getInstance().getUser().getFollowings());
        MyProfileStore.getInstance().getUser().getFollowers().forEach((u) -> {
            if (!MyProfileStore.getInstance().getUser().getFollowings().contains(u))
                userListView.getItems().add(u);
        });

        groupListView.getItems().addAll(MessageStore.getInstance().getGroups());

        Packet packet = new Packet("user-list-list");
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() == StatusCode.OK) {
            Type listType = new TypeToken<ArrayList<UserList>>(){}.getType();
            ArrayList<UserList> userLists = res.getObject("list", listType);
            listView.getItems().addAll(userLists);
        }

        rootPane.getChildren().add(hBox);
        hBox.getChildren().add(userListView);
        hBox.getChildren().add(groupListView);
        hBox.getChildren().add(listView);

        Stage newDialog = new Stage(StageStyle.UTILITY);
        newDialog.initModality(Modality.APPLICATION_MODAL);
        newDialog.setTitle("Forward");
        Scene newDialogScene = new Scene(rootPane);
        newDialog.setScene(newDialogScene);
        newDialog.showAndWait();

        for (User user: userListView.getSelectionModel().getSelectedItems())
            sendMessage(message, user);
        for (Group group: groupListView.getSelectionModel().getSelectedItems())
            for (User user: group.getUsers())
                sendMessage(message, user);
        for (UserList list: listView.getSelectionModel().getSelectedItems())
            for (User user: list.getUsers())
                sendMessage(message, user);
    }

    public static void sendMessage(String messageContent, User user) {
        Message message = new Message();
        message.setContent(messageContent);
        message.setSender(MyProfileStore.getInstance().getUser());
        message.setReceiver(user);
        MessageStore.getInstance().sendMessage(message);
        MessageStore.getInstance().commitChanges();
    }
}
