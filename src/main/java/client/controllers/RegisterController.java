package client.controllers;

import client.views.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private Label lblName, lblSurname, lblPassword, lblRepeatPassword, lblEmail;

    @FXML
    private TextField txtUsername, txtName, txtSurname;

    @FXML
    private PasswordField txtPassword, txtPasswordRepeat;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnRegister, btnLogin;

    @FXML
    private Label emailErr, passwordErr, usernameErr, nameErr, surnameErr;

    @FXML
    void register() {

    }

    @FXML
    void switchToLogin() {
        ViewManager.showView("LOGIN");
    }

}
