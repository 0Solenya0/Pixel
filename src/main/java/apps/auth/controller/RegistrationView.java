package apps.auth.controller;

import apps.auth.event.RegistrationEvent;
import apps.auth.listener.RegistrationListener;
import util.Config;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import view.InfoDialog;
import view.ViewManager;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationView implements Initializable {
    private final Config languageConfig = Config.getLanguageConfig();

    @FXML
    private TextField txtEmail, txtSurname, txtName, txtUsername;

    @FXML
    private PasswordField txtPasswordRepeat, txtPassword;

    @FXML
    private Button btnRegister, btnLogin;

    @FXML
    private Label emailErr, passwordErr, usernameErr, lblName, lblSurname, lblPassword, lblRepeatPassword,
        lblEmail, surnameErr, nameErr;

    private RegistrationListener registrationListener;

    public void switchToLogin() {
        ViewManager.setScene(ViewManager.loginView);
    }

    public void showErrors(ValidationException e) {
        resetErrorFields();
        if (e.getErrors("Email") != null)
            emailErr.setText(e.getErrors("Email").get(0));
        if (e.getErrors("PasswordRepeat") != null)
            passwordErr.setText(e.getErrors("PasswordRepeat").get(0));
        if (e.getErrors("Username") != null)
            usernameErr.setText(e.getErrors("Username").get(0));
        if (e.getErrors("Name") != null)
            nameErr.setText(e.getErrors("Name").get(0));
        if (e.getErrors("Surname") != null)
            surnameErr.setText(e.getErrors("Surname").get(0));
    }

    public void register() throws ConnectionException {
        resetErrorFields();
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

        try {
            RegistrationEvent event = new RegistrationEvent(
                    txtName.getText(),
                    txtSurname.getText(),
                    txtPassword.getText(),
                    txtUsername.getText(),
                    txtEmail.getText()
            );
            registrationListener.registrationEventOccurred(event);
        }
        catch (ValidationException e) {
            showErrors(e);
            return;
        }
        InfoDialog.showSuccess("Registered successfully!");
        switchToLogin();
    }

    public void resetErrorFields() {
        emailErr.setText("");
        passwordErr.setText("");
        usernameErr.setText("");
        nameErr.setText("");
        surnameErr.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registrationListener = new RegistrationListener();
        resetErrorFields();
        btnRegister.setText(languageConfig.getProperty("REGISTER_BTN_TEXT"));
        btnLogin.setText(languageConfig.getProperty("LOGIN_BTN_TEXT"));
        lblName.setText(languageConfig.getProperty("NAME"));
        lblSurname.setText(languageConfig.getProperty("SURNAME"));
        lblPassword.setText(languageConfig.getProperty("PASSWORD"));
        lblRepeatPassword.setText(languageConfig.getProperty("REPEAT_PASSWORD"));
        lblEmail.setText(languageConfig.getProperty("EMAIL"));
        emailErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
        usernameErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
        passwordErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
        surnameErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
        nameErr.setTextFill(Paint.valueOf(languageConfig.getProperty("ERROR_COLOR")));
    }
}
