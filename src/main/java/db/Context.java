package db;

import db.dbSet.*;

public class Context {
    public UserDBSet users = new UserDBSet();
    public TweetDBSet tweets = new TweetDBSet();
    public RelationDBSet relations = new RelationDBSet();
    public NotificationDBSet notifications = new NotificationDBSet();
    public MessageDBSet messages = new MessageDBSet();
    public GroupDBSet groups = new GroupDBSet();
    public ChatGroupDBSet chatGroups = new ChatGroupDBSet();
    public ImageDBSet images = new ImageDBSet();
}
