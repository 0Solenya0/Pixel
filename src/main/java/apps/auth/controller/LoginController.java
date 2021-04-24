package apps.auth.controller;

import apps.auth.State;
import apps.auth.model.User;
import controller.Controller;
import db.exception.ConnectionException;
import db.queryBuilder.QueryBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
        if (!user.checkPassword(textPassword.getText()))
            wrongPassword();
        else  {
            State.updateUser(user.id);
        }
    }

    public void wrongPassword() {

    }
}
