package db.dbSet;

import apps.auth.model.User;
import apps.notification.model.Notification;
import apps.notification.model.field.NotificationType;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.NotificationQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationDBSet extends DBSet<Notification> {
    private static final Logger logger = LogManager.getLogger(NotificationDBSet.class);
    public NotificationDBSet() {
        super(Notification.class);
    }

    @Override
    public void validate(Notification model) throws ConnectionException, ValidationException {

    }

    @Override
    public NotificationQueryBuilder getQueryBuilder() {
        return new NotificationQueryBuilder();
    }

    public void silentRefuse(Notification notification) throws ConnectionException {
        if (notification.getType() != NotificationType.REQUEST)
            return;
        delete(notification);
    }

    public void refuse(Notification notification) throws ConnectionException {
        if (notification.getType() != NotificationType.REQUEST)
            return;
        UserDBSet userDBSet = new UserDBSet();
        try {
            Notification response = new Notification(0, notification.getSender(),
                    userDBSet.get(notification.getReceiver()).getUsername() + " has refused your request");
            save(response);
            delete(notification);
        }
        catch (ValidationException e) {
            logger.error("refuse request failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void accept(Notification notification) throws ConnectionException {
        UserDBSet userDBSet = new UserDBSet();
        System.out.println(notification.getSender() + " " + notification.getReceiver());
        try {
            broadcast("ACCEPT " + notification.getSender() + " " + notification.getReceiver());
            Notification response = new Notification(0, notification.getSender(),
                    userDBSet.get(notification.getReceiver()).getUsername() + " has accepted your request");
            save(response);
            delete(notification);
        }
        catch (ValidationException e) {
            logger.error("accept request failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }
}
