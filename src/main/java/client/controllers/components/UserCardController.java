package client.controllers.components;

import client.utils.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import shared.models.User;

public class UserCardController {

    @FXML
    private ImageView imgAvatar;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblFullName;

    private Runnable listener;
    private int userId;

    @FXML
    void cardClicked(ActionEvent event) {
        listener.run();
    }

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    public void setUser(User user) {
        userId = user.id;
        if (user.getPhoto() != null)
            imgAvatar.setImage(ImageUtils.load(user.getPhoto()));
        lblUsername.setText(user.getUsername());
        lblFullName.setText(user.getFullName());
    }
}
