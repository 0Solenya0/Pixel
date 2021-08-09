package client.controllers.notification;

import client.request.SocketHandler;
import client.utils.ImageUtils;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import shared.models.Notification;
import shared.request.Packet;
import shared.request.StatusCode;

public class NotificationCardController {

    @FXML
    private Label lblNotification;

    @FXML
    private FontAwesomeIconView iconNotificationType;

    @FXML
    private ImageView imgNotificationImage;

    @FXML
    private JFXButton btnAccept, btnReject, btnSilentReject;

    private Notification notification;

    @FXML
    void accept(ActionEvent event) {
        Packet packet = new Packet("notification-action");
        packet.put("type", "accept");
        packet.put("req-id", notification.id);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() == StatusCode.OK) {
            setButtonsVisibility(false);
            lblNotification.setText("Accepted!");
        }
    }

    @FXML
    void reject(ActionEvent event) {
        Packet packet = new Packet("notification-action");
        packet.put("type", "reject");
        packet.put("req-id", notification.id);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() == StatusCode.OK) {
            setButtonsVisibility(false);
            lblNotification.setText("Rejected");
        }
    }

    @FXML
    void silentReject(ActionEvent event) {
        Packet packet = new Packet("notification-action");
        packet.put("type", "silent-reject");
        packet.put("req-id", notification.id);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        if (res.getStatus() == StatusCode.OK) {
            setButtonsVisibility(false);
            lblNotification.setText("Rejected");
        }
    }

    private void setButtonsVisibility(boolean visibility) {
        btnAccept.setVisible(visibility);
        btnReject.setVisible(visibility);
        btnSilentReject.setVisible(visibility);
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
        setButtonsVisibility(false);
        switch (notification.getType()) {
            case INFO:
                iconNotificationType.setIcon(FontAwesomeIcon.INFO);
                break;
            case REPORT:
                iconNotificationType.setIcon(FontAwesomeIcon.FLAG);
                break;
            case REQUEST:
                iconNotificationType.setIcon(FontAwesomeIcon.QUESTION_CIRCLE);
                setButtonsVisibility(true);
                if (notification.getSender().getPhoto() != null)
                    imgNotificationImage.setImage(ImageUtils.load(notification.getSender().getPhoto()));
                break;
        }
        lblNotification.setText(notification.getMessage());
    }
}
