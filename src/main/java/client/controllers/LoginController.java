package client.controllers;

import client.request.SocketHandler;
import client.store.Auth;
import client.views.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack;
import shared.request.Packet;

import java.util.Objects;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblErr;


    public void login() {
        Packet packet = new Packet("login");
        packet.put("username", txtUsername.getText());
        packet.put("password", txtPassword.getText());
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        Auth.authToken = response.get("auth-token", null);
    }

    public void switchToRegister() {
        ViewManager.showView("LOGIN");
    }
}
