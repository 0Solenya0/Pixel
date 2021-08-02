package client.views;

import client.listeners.UserListListener;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import shared.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserListDialog {
    public static void show(List<User> usersList, UserListListener listener) {
        Pane rootPane = new Pane();
        ArrayList<User> users = (ArrayList<User>) usersList;
        ListView<String> listView = new ListView<String>();
        for (User user: users)
            listView.getItems().add(user.getUsername() + " - " + user.getFullName());
        rootPane.getChildren().add(listView);
        Stage newDialog = new Stage(StageStyle.UTILITY);
        newDialog.initModality(Modality.APPLICATION_MODAL);
        newDialog.setTitle("Users");
        Scene newDialogScene = new Scene(rootPane);
        newDialog.setScene(newDialogScene);
        newDialog.showAndWait();
        ArrayList<User> res = new ArrayList<>();
        for (int item: listView.getSelectionModel().getSelectedIndices())
            res.add(users.get(item));
        listener.listen(res);
    }
}
