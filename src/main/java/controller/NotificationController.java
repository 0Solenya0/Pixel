package controller;

import db.dbSet.NotificationDBSet;
import db.dbSet.UserDBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.Notification;
import model.Relation;
import model.field.NotificationType;
import model.field.RelStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationController extends Controller {
    private static final Logger logger = LogManager.getLogger(NotificationController.class);

    public void silentRefuse(Notification notification) throws ConnectionException {
        if (notification.getType() != NotificationType.REQUEST)
            return;
        context.notifications.delete(notification);
    }

    public void refuse(Notification notification) throws ConnectionException {
        if (notification.getType() != NotificationType.REQUEST)
            return;
        UserDBSet userDBSet = new UserDBSet();
        try {
            Notification response = new Notification(0, notification.getSender(),
                    userDBSet.get(notification.getReceiver()).getUsername() + " has refused your request");
            context.notifications.save(response);
            context.notifications.delete(notification);
        }
        catch (ValidationException e) {
            logger.error("refuse request failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void accept(Notification notification) throws ConnectionException {
        UserDBSet userDBSet = new UserDBSet();
        try {
            Relation relation = new Relation(
                    context.users.get(notification.getSender()),
                    context.users.get(notification.getReceiver()),
                    RelStatus.FOLLOW);
            context.relations.save(relation);
            Notification response = new Notification(0, notification.getSender(),
                    userDBSet.get(notification.getReceiver()).getUsername() + " has accepted your request");
            context.notifications.save(response);
            context.notifications.delete(notification);
        }
        catch (ValidationException e) {
            logger.error("accept request failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }
}
