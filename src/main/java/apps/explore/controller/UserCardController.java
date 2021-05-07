package apps.explore.controller;

import apps.auth.model.User;
import config.Config;
import controller.Controller;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import listener.StringListener;

public class UserCardController extends Controller {

    public static FXMLLoader getFxmlLoader() {
        Config config = Config.getConfig("EXPLORE_APP_CONFIG");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UserCardController.class.getResource(config.getProperty("USER_CARD_VIEW")));
        return fxmlLoader;
    }

    @FXML
    private ImageView imgAvatar; // TO DO

    @FXML
    private Label lblUsername, lblFullName;

    private StringListener onClickListener;
    User user;

    public void setOnClickListener(StringListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @FXML
    void cardClicked() {
        onClickListener.listen("Clicked");
    }

    public void updateCard() throws ConnectionException {
        user = context.users.get(user.id);
        lblUsername.setText("@" + user.getUsername());
        lblFullName.setText(user.getFullName());
    }

    public void setUser(User user) throws ConnectionException {
        this.user = user;
        updateCard();
    }
}
