package Server.models.Filters;

import Server.models.Exceptions.ConnectionException;
import Server.models.Model;
import Server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserFilter extends ModelFilter<User> {

    public UserFilter() throws ConnectionException {
        super(User.class);
    }

    public User getByUsername(String username) {
        return get(user -> user.username.equals(username));
    }
    public User getByMail(String mail) {
        return get(user -> user.getMail().get().equals(mail));
    }
    public User getByPhone(String phone) {
        return get(user -> user.getPhone().get().equals(phone));
    }
    public UserFilter getByUsernamePrefix(String username) {
        customFilter(user -> user.username.startsWith(username));
        return this;
    }
    public UserFilter getEnabled() {
        customFilter(user -> user.isEnabled());
        return this;
    }
}