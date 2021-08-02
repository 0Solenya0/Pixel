package client.controllers;

import client.controllers.components.UserCardController;
import client.request.SocketHandler;
import client.views.ViewManager;
import com.google.gson.reflect.TypeToken;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import shared.models.User;
import shared.request.Packet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ExplorerController {

    @FXML
    private Tab tabExploreUser;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblUsername, lblErr;

    @FXML
    private VBox vBoxContainer;

    @FXML
    private FontAwesomeIconView iconErr;

    @FXML
    private Tab tabExploreFeed;

    @FXML
    private AnchorPane explorerFeedPane;

    private Timer timer = new Timer();

    @FXML
    void switchToExploreFeed(ActionEvent event) {
        // TO DO
    }

    private void updateUserList(ArrayList<User> users) {
        lblErr.setVisible(users.isEmpty());
        iconErr.setVisible(users.isEmpty());

        vBoxContainer.getChildren().clear();
        for (User user: users) {
            FXMLLoader loader = ViewManager.getComponent("USER_CARD");
            try {
                vBoxContainer.getChildren().add(loader.load());
                UserCardController controller = loader.getController();
                controller.setUser(user);
            } catch (IOException e) {
                // TO DO log error
                e.printStackTrace();
            }
        }
    }

    @FXML
    void usernameChange(KeyEvent event) {
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Packet packet = new Packet("search-user");
                    packet.put("username", txtUsername.getText());
                    Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
                    System.out.println(res.getJson());
                    Type listType = new TypeToken<ArrayList<User>>() {
                    }.getType();
                    updateUserList(res.getObject("users", listType));
                });
            }
        }, 500);
    }

}
