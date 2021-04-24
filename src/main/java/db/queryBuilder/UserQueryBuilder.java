package db.queryBuilder;

import apps.auth.model.User;

public class UserQueryBuilder extends QueryBuilder<User> {

    public UserQueryBuilder() {
        super();
    }

    public UserQueryBuilder getByUsername(String username) {
        return (UserQueryBuilder) addCustomFilter(user -> user.getUsername().equals(username));
    }

    public UserQueryBuilder getByMail(String mail) {
        return (UserQueryBuilder) addCustomFilter(user -> user.getMail().get().equals(mail));
    }

    public UserQueryBuilder getByPhone(String phone) {
        return (UserQueryBuilder) addCustomFilter(user -> user.getPhone().get().equals(phone));
    }

    public UserQueryBuilder getByUsernamePrefix(String username) {
        return (UserQueryBuilder) addCustomFilter(user -> user.getUsername().startsWith(username));
    }

    public UserQueryBuilder getEnabled() {
        return (UserQueryBuilder) addCustomFilter(user -> user.isEnabled());
    }
}
