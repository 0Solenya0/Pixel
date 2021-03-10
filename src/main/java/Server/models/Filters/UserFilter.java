package Server.models.Filters;

import Server.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserFilter {
    private ArrayList<User> list;
    public UserFilter() throws IOException {
        list = new ArrayList<>();
        for (int i = 1; i <= User.getLastId(User.datasrc); i++)
            list.add(User.get(i));
    }
    public UserFilter userCustomFilter(Predicate<User> p) {
        list = (ArrayList<User>) list.stream().filter(p).collect(Collectors.toList());
        return this;
    }
    public UserFilter getActive() {
        return this.userCustomFilter(user -> user.isActive);
    }
    public User getUsername(String username) {
        this.userCustomFilter(user -> user.username.equals(username));
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    public User getMail(String mail) {
        this.userCustomFilter(user -> user.getMail().get().equals(mail));
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    public User getPhone(String phone) {
        this.userCustomFilter(user -> user.getPhone().get().equals(phone));
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    public UserFilter getUsernamePrefix(String username) {
        return this.userCustomFilter(user -> user.username.startsWith(username));
    }

    public ArrayList<User> getList() {
        return list;
    }
}