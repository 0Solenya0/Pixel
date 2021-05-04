package apps.auth.controller;

import apps.auth.State;
import apps.auth.model.User;
import config.Config;
import controller.Controller;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import view.ViewManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class LoginController extends Controller implements Initializable {
    private Config config = Config.getLanguageConfig();

    @FXML
    private Label lblPassword, lblUsername;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin, btnRegister;

    public void login() throws ConnectionException {
        Predicate<User> q = context.users.getQueryBuilder()
                .getByUsername(txtUsername.getText()).getQuery();
        User user = context.users.getFirst(q);
        if (user == null || !user.checkPassword(txtPassword.getText()))
            wrongInput();
        else  {
            State.updateUser(user.id);
            ViewManager.mainPanelController.showHomePage();
            ViewManager.setScene(ViewManager.mainView);
        }
    }

    public void switchToRegister() {
        ViewManager.setScene(ViewManager.registrationView);
    }

    public void wrongInput() {
        // TO DO
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUsername.setPromptText(config.getProperty("USERNAME_PROMPT"));
        txtPassword.setPromptText(config.getProperty("PASSWORD_PROMPT"));
        btnLogin.setText(config.getProperty("LOGIN_BTN_TEXT"));
        btnRegister.setText(config.getProperty("REGISTER_BTN_TEXT"));
        lblPassword.setText(config.getProperty("PASSWORD_LABEL"));
        lblUsername.setText(config.getProperty("USERNAME_LABEL"));
    }
}
