package db;

import model.User;
import model.Notification;
import model.field.NotificationType;
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
        notifications.addListener(s -> {
            try {
                Scanner scanner = new Scanner(s);
                if ("ACCEPT".equals(scanner.next())) {

                }
            }
            catch (Exception ignored) {

            }
        });
    }
}
