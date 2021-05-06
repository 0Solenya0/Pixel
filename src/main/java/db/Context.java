package db;

import apps.auth.model.User;
import apps.notification.model.Notification;
import apps.notification.model.field.NotificationType;
import db.dbSet.*;

import java.util.Scanner;

public class Context {
    public UserDBSet users = new UserDBSet();
    public TweetDBSet tweets = new TweetDBSet();
    public RelationDBSet relations = new RelationDBSet();
    public NotificationDBSet notifications = new NotificationDBSet();
    public MessageDBSet messages = new MessageDBSet();
    public GroupDBSet groups = new GroupDBSet();

    public Context() {
        relations.addListener(s -> {
            try {
                Scanner scanner = new Scanner(s);
                String type = scanner.next();
                User user1 = users.get(scanner.nextInt());
                User user2 = users.get(scanner.nextInt());
                switch (type) {
                    case "REQUEST":
                        Notification notification = new Notification(user1.id, user2.id, NotificationType.REQUEST);
                        notifications.save(notification);
                        break;
                    case "FOLLOW":
                        Notification notification1 = new Notification(0, user2.id,
                                user1.getUsername() + " has started following you");
                        notifications.save(notification1);
                        break;
                    case "UNFOLLOW":
                        Notification notification2 = new Notification(0, user2.id,
                                user1.getUsername() + " has stopped following you");
                        notifications.save(notification2);
                        break;
                    case "BLOCK":
                        Notification tmp = notifications.getFirst(
                                notifications.getQueryBuilder()
                                        .getByTwoUser(user1, user2)
                                        .getByType(NotificationType.REQUEST)
                                        .getQuery()
                        );
                        if (tmp != null)
                            notifications.delete(tmp);
                        tmp = notifications.getFirst(
                                notifications.getQueryBuilder()
                                        .getByTwoUser(user2, user1)
                                        .getByType(NotificationType.REQUEST)
                                        .getQuery()
                        );
                        if (tmp != null)
                            notifications.delete(tmp);
                        break;
                }
            }
            catch (Exception ignored) {

            }
        });
        notifications.addListener(s -> {
            try {
                Scanner scanner = new Scanner(s);
                if ("ACCEPT".equals(scanner.next())) {
                    relations.follow(users.get(scanner.nextInt()), users.get(scanner.nextInt()));
                }
            }
            catch (Exception ignored) {

            }
        });
    }
}
