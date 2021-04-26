package apps.auth.controller;

import apps.auth.State;
import apps.auth.model.User;
import controller.Controller;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import view.ViewManager;

import java.util.function.Predicate;

public class LoginController extends Controller {

    @FXML
    private TextField textUsername;

    @FXML
    private TextField textPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    public void login() throws ConnectionException {
        Predicate<User> q = context.users.getQueryBuilder()
                .getByUsername(textUsername.getText()).getQuery();
        User user = context.users.getFirst(q);
        if (user == null || !user.checkPassword(textPassword.getText()))
            wrongInput();
        else  {
            State.updateUser(user.id);
            ViewManager.setScene(ViewManager.mainView);
        }
    }

    public void switchToRegister() {
        ViewManager.setScene(ViewManager.registrationView);
    }

    public void wrongInput() {
        // TO DO
    }
}
