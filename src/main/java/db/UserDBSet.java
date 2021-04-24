package db;

import apps.auth.model.User;

public class UserDBSet extends DBSet<User> {
    public UserDBSet() {
        super(User.class);
    }
}
