package client.controllers.components;

import client.utils.ImageUtils;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import shared.models.Notification;
import shared.models.fields.NotificationType;

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
        // TO DO
    }

    @FXML
    void reject(ActionEvent event) {
        // TO DO
    }

    @FXML
    void silentReject(ActionEvent event) {
        // TO DO
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
