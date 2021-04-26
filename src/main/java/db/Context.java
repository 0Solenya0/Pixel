package db;

import db.dbSet.TweetDBSet;
import db.dbSet.UserDBSet;

public class Context {
    public UserDBSet users = new UserDBSet();
    public TweetDBSet tweets = new TweetDBSet();
}
