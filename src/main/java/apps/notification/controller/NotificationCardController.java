package apps.notification.controller;

import apps.auth.model.User;
import apps.notification.model.Notification;
import apps.notification.model.field.NotificationType;
import com.jfoenix.controls.JFXButton;
import controller.Controller;
import db.exception.ConnectionException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.*;

public class NotificationCardController extends Controller {

    @FXML
    private JFXButton btnAccept, btnReject, btnSilentReject;

    @FXML
    private Label lblNotification;

    @FXML
    private FontAwesomeIconView iconNotificationType;

    @FXML
    private ImageView imgNotificationImage; //TO DO

    private Notification notification;

    public void setBtnVisibility(boolean visibility) {
        btnAccept.setVisible(visibility);
        btnReject.setVisible(visibility);
        btnSilentReject.setVisible(visibility);
    }

    @FXML
    void accept(ActionEvent event) throws ConnectionException {
        context.notifications.accept(notification);
        setBtnVisibility(false);
        lblNotification.setText("Accepted!");
    }

    @FXML
    void reject(ActionEvent event) throws ConnectionException {
        context.notifications.refuse(notification);
        setBtnVisibility(false);
        lblNotification.setText("Rejected");
    }

    @FXML
    void silentReject(ActionEvent event) throws ConnectionException {
        context.notifications.silentRefuse(notification);
        setBtnVisibility(false);
        lblNotification.setText("Rejected");
    }

    public void setNotification(Notification notification) throws ConnectionException {
        this.notification = notification;
        updateCard();
    }

    public void updateCard() throws ConnectionException {
        notification = context.notifications.get(notification.id);
        if (notification.getType() == NotificationType.REQUEST) {
            iconNotificationType.setGlyphName(String.valueOf(FontAwesomeIcon.QUESTION_CIRCLE));
            User sender = context.users.get(notification.getSender());
            lblNotification.setText(notification.getMessageRequests(sender.getUsername()));
        }
        else if (notification.getType() == NotificationType.INFO) {
            iconNotificationType.setGlyphName(String.valueOf(FontAwesomeIcon.INFO_CIRCLE));
            setBtnVisibility(false);
            lblNotification.setText(notification.getMessage());
        }
        else {
            iconNotificationType.setGlyphName(String.valueOf(FontAwesomeIcon.FLAG));
            setBtnVisibility(false);
            lblNotification.setText(notification.getMessage());
        }
    }

}
