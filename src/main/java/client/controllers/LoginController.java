package client.controllers;

import client.request.SocketHandler;
import client.store.Index;
import client.views.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import shared.request.Packet;
import shared.request.StatusCode;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblErr;


    public void login() {
        lblErr.setText("");
        Packet packet = new Packet("login");
        packet.put("username", txtUsername.getText());
        packet.put("password", txtPassword.getText());
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        Index.getInstance().setAuthToken(response.get("auth-token", null));
        if (response.getStatus() != StatusCode.OK)
            lblErr.setText("Username or password is wrong.");
        else
            ViewManager.loadLayout();
    }

    public void switchToRegister() {
        ViewManager.showView("REGISTER");
    }
}
