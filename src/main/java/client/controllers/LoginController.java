package client.controllers;

import client.request.SocketHandler;
import client.store.MessageStore;
import client.store.MyProfileStore;
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
        MyProfileStore.getInstance().setAuthToken(response.get("auth-token", null));
        if (response.getStatus() == StatusCode.BAD_GATEWAY)
            lblErr.setText("No connection");
        else if (response.getStatus() != StatusCode.OK)
            lblErr.setText("Username or password is wrong.");
        else {
            ViewManager.loadLayout();
            MyProfileStore.getInstance().updateUserProfile();
            MessageStore.getInstance().refreshAllData();
        }
    }

    public void switchToRegister() {
        ViewManager.showView("REGISTER");
    }
}
