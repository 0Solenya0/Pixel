package apps.explore.controller;

import db.dbSet.ImageDBSet;
import javafx.fxml.Initializable;
import model.User;
import util.Config;
import controller.Controller;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import listener.StringListener;
import view.ViewManager;

import java.net.URL;
import java.util.ResourceBundle;

public class UserCardController extends Controller implements Initializable {

    public static FXMLLoader getFxmlLoader() {
        Config config = Config.getConfig("EXPLORE_APP_CONFIG");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(UserCardController.class.getResource(config.getProperty("USER_CARD_VIEW")));
        return fxmlLoader;
    }

    @FXML
    private ImageView imgAvatar;

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
        new Thread(() -> {
            ImageDBSet imageDBSet = new ImageDBSet();
            if (user.getPhoto() != null) {
                try {
                    imgAvatar.setImage(imageDBSet.load(user.getPhoto()));
                    imgAvatar.setVisible(true);
                } catch (ConnectionException e) {
                    ViewManager.connectionFailed();
                }
            } else
                imgAvatar.setVisible(false);
        }).start();

    }

    public void setUser(User user) throws ConnectionException {
        this.user = user;
        updateCard();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgAvatar.setVisible(false);
    }
}
