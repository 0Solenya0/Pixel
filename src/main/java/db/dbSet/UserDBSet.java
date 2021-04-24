package db.dbSet;

import apps.auth.model.User;
import db.dbSet.DBSet;
import db.queryBuilder.QueryBuilder;
import db.queryBuilder.UserQueryBuilder;

public class UserDBSet extends DBSet<User> {
    public UserDBSet() {
        super(User.class);
    }

    @Override
    public UserQueryBuilder getQueryBuilder() {
        return new UserQueryBuilder();
    }
}
