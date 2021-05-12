package view;

import apps.auth.State;
import controller.RelationController;
import db.Context;
import db.exception.ConnectionException;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import listener.ForwardListener;
import model.ChatGroup;
import model.Group;
import model.User;

import java.util.ArrayList;
import java.util.Objects;

public class ForwardListDialog {
    private static final RelationController relationController = new RelationController();
    private static final Context context = new Context();

    public static void show(ForwardListener listener) throws ConnectionException {
        Objects.requireNonNull(State.getUser());
        Pane rootPane = new Pane();
        HBox hBox = new HBox();
        ListView<String> usersList = new ListView<String>();
        ListView<String> groupsList = new ListView<String>();
        ListView<String> chatGroupsList = new ListView<String>();

        ArrayList<User> users = relationController.getFollowingAndFollower(State.getUser());
        for (User user: users)
            usersList.getItems().add(user.getUsername() + " - " + user.getFullName());

        ArrayList<Group> groups = context.groups.getAll(
                context.groups.getQueryBuilder().getByOwner(State.getCurrentUserId()).getQuery()
        );
        for (Group group: groups)
            groupsList.getItems().add("My group - " + group.getName());

        ArrayList<ChatGroup> chatGroups = context.chatGroups.getAll(
                context.chatGroups.getQueryBuilder().getByMember(State.getCurrentUserId()).getQuery()
        );
        for (ChatGroup chatGroup: chatGroups)
            chatGroupsList.getItems().add("Chat group - " + chatGroup.getName());

        rootPane.getChildren().add(hBox);
        hBox.getChildren().add(usersList);
        hBox.getChildren().add(groupsList);
        hBox.getChildren().add(chatGroupsList);

        Stage newDialog = new Stage(StageStyle.UTILITY);
        newDialog.initModality(Modality.APPLICATION_MODAL);
        newDialog.setTitle("Forward");
        Scene newDialogScene = new Scene(rootPane);
        newDialog.setScene(newDialogScene);
        newDialog.showAndWait();

        ArrayList<User> resUsers = new ArrayList<>();
        ArrayList<Group> resGroups = new ArrayList<>();
        ArrayList<ChatGroup> resChatGroups = new ArrayList<>();
        for (int item: chatGroupsList.getSelectionModel().getSelectedIndices())
            resChatGroups.add(chatGroups.get(item));
        for (int item: groupsList.getSelectionModel().getSelectedIndices())
            resGroups.add(groups.get(item));
        for (int item: usersList.getSelectionModel().getSelectedIndices())
            resUsers.add(users.get(item));
        listener.listen(resUsers, resGroups, resChatGroups);
    }
}
