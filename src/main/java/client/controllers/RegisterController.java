package client.controllers;

import client.request.SocketHandler;
import client.views.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import shared.exception.ValidationException;
import shared.request.Packet;
import shared.request.StatusCode;

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

    private void clearErrors() {
        emailErr.setText("");
        passwordErr.setText("");
        usernameErr.setText("");
        nameErr.setText("");
        surnameErr.setText("");
    }

    private void setErrors(ValidationException e) {
        if (e.getErrors("mail") != null)
            emailErr.setText(e.getErrors("mail").get(0));
        if (e.getErrors("password") != null)
            passwordErr.setText(e.getErrors("password").get(0));
        if (e.getErrors("username") != null)
            usernameErr.setText(e.getErrors("username").get(0));
        if (e.getErrors("name") != null)
            nameErr.setText(e.getErrors("name").get(0));
        if (e.getErrors("surname") != null)
            surnameErr.setText(e.getErrors("surname").get(0));
    }

    @FXML
    void register() {
        clearErrors();
        if (!txtPassword.getText().equals(txtPasswordRepeat.getText())) {
            passwordErr.setText("Passwords doesn't match");
            return;
        }
        Packet packet = new Packet("register");
        packet.put("username", txtUsername.getText());
        packet.put("name", txtName.getText());
        packet.put("surname", txtSurname.getText());
        packet.put("mail", txtEmail.getText());
        packet.put("password", txtPassword.getText());
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (response.getStatus() == StatusCode.BAD_REQUEST) {
            ValidationException e = response.getObject("error", ValidationException.class);
            setErrors(e);
        }
    }

    @FXML
    void switchToLogin() {
        ViewManager.showView("LOGIN");
    }

}
