package client.controllers;

import client.controllers.components.NotificationCardController;
import client.request.SocketHandler;
import client.views.AutoUpdate;
import client.views.ViewManager;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import shared.models.Notification;
import shared.models.User;
import shared.request.Packet;
import shared.util.Config;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {

    private final Config config = Config.getConfig("mainConfig");

    @FXML
    private VBox vboxContainer;

    @FXML
    private JFXListView<String> listPendingRequests;

    AutoUpdate autoUpdate = new AutoUpdate();

    void updateData() {
        Packet packet = new Packet("notification-list");
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        Type listType = new TypeToken<ArrayList<Notification>>() {}.getType();
        ArrayList<Notification> notifications = res.getObject("notifications", listType);

        System.out.println(notifications);
        vboxContainer.getChildren().clear();
        for (Notification notification: notifications) {
            ViewManager.Component<NotificationCardController> component = ViewManager.getComponent("NOTIFICATION_CARD");
            component.getController().setNotification(notification);
            vboxContainer.getChildren().add(component.getPane());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateData();
        autoUpdate.setTask(this::updateData, config.getProperty(Integer.class, "NOTIFICATION_REFRESH_RATE"));
    }
}
