package db.dbSet;

import model.Notification;
import model.field.NotificationType;
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
}
