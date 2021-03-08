package Server.models.Filters;

import Server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserFilter {
    private ArrayList<User> list;
    public UserFilter() throws IOException {
        for (int i = 1; i <= User.getLastId(User.datasrc); i++)
            list.add(User.get(i));
    }
    public UserFilter userCustomFilter(Predicate<User> p) {
        list.stream().filter(p).collect(Collectors.toList());
        return this;
    }
    public UserFilter getActive() {
        return this.userCustomFilter(user -> user.isActive);
    }
    public User getUsername(String username) {
        this.userCustomFilter(user -> user.username == username);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    public User getMail(String mail) {
        this.userCustomFilter(user -> user.mail.get() == mail);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    public User getPhone(String phone) {
        this.userCustomFilter(user -> user.phone.get() == phone);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    public ArrayList<User> getList() {
        return list;
    }
}