package Server.models.Filters;

import Server.models.User;

import java.util.ArrayList;

public class UserFilter {
    private ArrayList<User> list;
    public UserFilter(ArrayList<User> arrayList) {
        list = arrayList;
    }
    public UserFilter getActive() {
        for (int i = 0; i < list.size(); i++)
            if (!list.get(i).isActive) {
                list.remove(i);
                i--;
            }
        return this;
    }
    public User getUsername(String username) {
        for (int i = 0; i < list.size(); i++)
            if (!list.get(i).username.equals(username)) {
                list.remove(i);
                i--;
            }
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    public User getMail(String mail) {
        for (int i = 0; i < list.size(); i++)
            if (!list.get(i).mail.get().equals(mail)) {
                list.remove(i);
                i--;
            }
        if (list.isEmpty())
            return null;
        return list.get(0);
    }
    public User getPhone(String phone) {
        for (int i = 0; i < list.size(); i++)
            if (!list.get(i).phone.get().equals(phone)) {
                list.remove(i);
                i--;
            }
        if (list.isEmpty())
            return null;
        return list.get(0);
    }

    public ArrayList<User> getList() {
        return list;
    }
}