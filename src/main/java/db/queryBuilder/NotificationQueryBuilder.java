package db.queryBuilder;

import apps.auth.model.User;
import apps.notification.model.Notification;
import apps.notification.model.field.NotificationType;

public class NotificationQueryBuilder extends QueryBuilder<Notification> {

    public NotificationQueryBuilder() {
        super();
    }

    public NotificationQueryBuilder getByType(NotificationType t) {
        addCustomFilter(notification -> notification.getType() == t);
        return this;
    }

    public NotificationQueryBuilder getByUser1(User user1) {
        addCustomFilter(notification -> notification.getSender() == user1.id);
        return this;
    }

    public NotificationQueryBuilder getByUser2(User user2) {
        addCustomFilter(notification -> notification.getReceiver() == user2.id);
        return this;
    }

    public NotificationQueryBuilder getByTwoUser(User user1, User user2) {
        addCustomFilter(notification -> notification.getSender() == user1.id && notification.getReceiver() == user2.id);
        return this;
    }

    public NotificationQueryBuilder getEnabled() {
        addCustomFilter(notification -> {
            try {
                return context.users.get(notification.getSender()).isEnabled()
                        && context.users.get(notification.getReceiver()).isEnabled();
            }
            catch (Exception e) {
                return false;
            }
        });
        return this;
    }
}