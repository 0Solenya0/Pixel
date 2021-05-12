package view;

import listener.UserListListener;
import model.User;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class UserListDialog {
    public static void show(ArrayList<User> users, UserListListener listener) {
        Pane rootPane = new Pane();
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
