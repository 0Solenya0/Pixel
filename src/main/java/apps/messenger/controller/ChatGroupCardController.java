package apps.messenger.controller;

import apps.auth.State;
import controller.Controller;
import db.exception.ConnectionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import listener.StringListener;
import model.ChatGroup;
import model.Message;
import model.User;

import java.util.ArrayList;
import java.util.Objects;

public class ChatGroupCardController extends Controller {

    @FXML
    private ImageView imgAvatar;

    @FXML
    private Label lblLastChat, lblUnseen, lblChatGroupName;

    private StringListener onClickListener;
    private User user;
    private ChatGroup group;

    public void setUser(User user) throws ConnectionException {
        this.user = user;
        this.group = null;
        updateCard();
    }

    public void setOnClickListener(StringListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setGroup(ChatGroup group) throws ConnectionException {
        this.group = group;
        this.user = null;
        updateCard();
    }

    public void updateCard() throws ConnectionException {
        if (user != null)
            lblChatGroupName.setText(user.getUsername());
        else
            lblChatGroupName.setText(group.getName());
        ArrayList<Message> messages;
        if (user != null) {
            messages = context.messages.getAll(
                    context.messages.getQueryBuilder()
                            .getTwoUserChats(Objects.requireNonNull(State.getUser()).id, user.id)
                            .getQuery()
            );
        }
        else {
            //update group messages
            messages = context.messages.getAll(
                    context.messages.getQueryBuilder()
                            .getByGroup(group.id)
                            .getQuery()
            );
        }
        if (messages.size() > 0)
            lblLastChat.setText(messages.get(messages.size() - 1).getContent());
        int unseen = 0;
        for (Message message: messages) {
            if (!message.hasViewed(Objects.requireNonNull(State.getUser())))
                unseen++;
        }
        lblUnseen.setVisible(unseen != 0);
        lblUnseen.setText(String.valueOf(unseen));
    }

    @FXML
    void itemClicked(ActionEvent event) {
        if (onClickListener != null)
            onClickListener.listen("CLICK");
    }

}
