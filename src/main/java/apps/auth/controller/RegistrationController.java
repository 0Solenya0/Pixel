package apps.auth.controller;

import apps.auth.model.User;
import controller.Controller;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import view.SuccessDialog;
import view.ViewManager;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController extends Controller implements Initializable {

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPasswordRepeat;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label emailErr;

    @FXML
    private Label passwordErr;

    @FXML
    private Label usernameErr;

    public void switchToLogin() {
        ViewManager.setScene(ViewManager.loginView);
    }

    public void showErrors(ValidationException e) {
        emailErr.setText("");
        passwordErr.setText("");
        usernameErr.setText("");
        if (e.getErrors("Email") != null)
            emailErr.setText(e.getErrors("Email").get(0));
        if (e.getErrors("PasswordRepeat") != null)
            passwordErr.setText(e.getErrors("PasswordRepeat").get(0));
        if (e.getErrors("Username") != null)
            usernameErr.setText(e.getErrors("Username").get(0));
    }

    public void register() throws ConnectionException {
        emailErr.setText("");
        passwordErr.setText("");
        usernameErr.setText("");
        ValidationException validationException = new ValidationException();
        if (!txtPassword.getText().equals(txtPasswordRepeat.getText())) {
            validationException.addError("PasswordRepeat", "Passwords doesn't match");
            showErrors(validationException);
            return;
        }
        if (txtPassword.getText().length() == 0) {
            validationException.addError("PasswordRepeat", "Enter a password");
            showErrors(validationException);
            return;
        }
        User user = new User(
                txtName.getText(),
                txtSurname.getText(),
                txtUsername.getText(),
                txtEmail.getText(),
                txtPassword.getText()
        );

        try {
            context.users.save(user);
        }
        catch (ValidationException e) {
            showErrors(e);
            return;
        }
        SuccessDialog.show("Registered successfully!");
        switchToLogin();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailErr.setText("");
        passwordErr.setText("");
        usernameErr.setText("");
    }
}
