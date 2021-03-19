package Server.models.Filters;

import Server.models.Exceptions.ConnectionException;
import Server.models.Notification;
import Server.models.Relation;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NotificationFilter {
    private ArrayList<Notification> list;
    public NotificationFilter() throws ConnectionException {
        list = new ArrayList<>();
        for (int i = 1; i <= Notification.getLastId(Notification.datasrc); i++)
            if (!Notification.get(i).isDeleted)
                list.add(Notification.get(i));
    }
    public NotificationFilter userCustomFilter(Predicate<Notification> p) {
        list = (ArrayList<Notification>) list.stream().filter(p).collect(Collectors.toList());
        return this;
    }
    public NotificationFilter getByUser(int user1) {
        userCustomFilter(notif -> notif.user1 == user1);
        return this;
    }

    public ArrayList<Notification> getList() {
        return list;
    }
}
