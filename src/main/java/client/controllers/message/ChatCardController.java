package client.controllers.message;

import client.store.MessageStore;
import client.store.MyProfileStore;
import client.utils.ImageUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.models.Group;
import shared.models.Message;
import shared.models.User;

import java.util.ArrayList;

public class ChatCardController {

    @FXML
    private ImageView imgAvatar;

    @FXML
    private Label lblChatGroupName, lblLastChat, lblUnseen;

    private Runnable clickListener;

    @FXML
    void itemClicked(ActionEvent event) {
        clickListener.run();
    }

    public void setClickListener(Runnable clickListener) {
        this.clickListener = clickListener;
    }

    public void setUser(User user) {
        lblChatGroupName.setText(user.getUsername());
        if (user.getPhoto() != null) {
            Thread thread = new Thread(() -> {
                Image image = ImageUtils.load(user.getPhoto());
                Platform.runLater(() -> imgAvatar.setImage(image));
            });
            thread.start();
        }
        ArrayList<Message> messages = MessageStore.getInstance().getByUser(user);
        if (messages.size() != 0) {
            User curUser = MyProfileStore.getInstance().getUser();
            lblLastChat.setText(messages.get(messages.size() - 1).getContent());
            lblUnseen.setText(String.valueOf(
                    messages.stream().filter((m) -> !m.getViewers().contains(curUser) && m.getSender().id != curUser.id).count()
            ));
        }
        if (lblUnseen.getText().equals("0"))
            lblUnseen.setText("");
    }

    public void setGroup(Group group) {
        lblChatGroupName.setText(group.getName());
        ArrayList<Message> messages = MessageStore.getInstance().getByGroup(group);
        if (messages.size() != 0) {
            User curUser = MyProfileStore.getInstance().getUser();
            lblLastChat.setText(messages.get(messages.size() - 1).getContent());
            lblUnseen.setText(String.valueOf(
                    messages.stream().filter((m) -> !m.getViewers().contains(curUser) && m.getSender() != curUser).count()
            ));
        }
        if (lblUnseen.getText().equals("0"))
            lblUnseen.setText("");
    }
}
