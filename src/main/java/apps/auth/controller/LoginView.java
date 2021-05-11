package apps.auth.controller;

import apps.auth.State;
import apps.auth.event.LoginEvent;
import apps.auth.listener.LoginListener;
import util.Config;
import controller.Controller;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import view.ViewManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginView extends Controller implements Initializable {
    private final Config languageConfig = Config.getLanguageConfig();

    @FXML
    private Label lblPassword, lblUsername, lblErr;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin, btnRegister;

    private LoginListener loginListener;

    public void login() throws ConnectionException {
        LoginEvent event = new LoginEvent(txtUsername.getText(), txtPassword.getText());
        int res = loginListener.loginEventOccurred(event);
        lblErr.setText("");
        if (res == 0)
            lblErr.setText("Wrong username or password!");
        else  {
            State.updateUser(res);
            ViewManager.mainPanelController.showHomePage();
            ViewManager.setScene(ViewManager.mainView);
        }
    }

    public void switchToRegister() {
        ViewManager.setScene(ViewManager.registrationView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginListener = new LoginListener();

        txtUsername.setPromptText(languageConfig.getProperty("USERNAME_PROMPT"));
        txtPassword.setPromptText(languageConfig.getProperty("PASSWORD_PROMPT"));
        btnLogin.setText(languageConfig.getProperty("LOGIN_BTN_TEXT"));
        btnRegister.setText(languageConfig.getProperty("REGISTER_BTN_TEXT"));
        lblPassword.setText(languageConfig.getProperty("PASSWORD"));
        lblUsername.setText(languageConfig.getProperty("USERNAME"));
        lblErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
        lblErr.setText("");
    }
}
