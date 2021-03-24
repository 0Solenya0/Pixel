package Server.models.Filters;

import Server.models.Exceptions.ConnectionException;
import Server.models.Message;
import Server.models.User;
import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class MessageFilter extends ModelFilter<Message> {
    public MessageFilter() throws ConnectionException {
        super(Message.class);
    }

    public MessageFilter getByUser1(int user) {
        customFilter(message -> message.user1 == user);
        return this;
    }
    public MessageFilter getByUser2(int user) {
        customFilter(message -> message.user2 == user);
        return this;
    }
    public MessageFilter getByTwoUser(int user1, int user2) {
        customFilter(message -> message.user2 == user2 && message.user1 == user1);
        return this;
    }
    public MessageFilter getBySeen(boolean seen) {
        customFilter(message -> message.seen == seen);
        return this;
    }
    public MessageFilter getRelatedToUser(int user) {
        customFilter(message -> message.user2 == user || message.user1 == user);
        return this;
    }

    /** Newest messages are at start of list **/
    public ArrayList<User> getUserList() throws ConnectionException {
        TreeSet<Integer> tmp = new TreeSet<>();
        ArrayList<User> res = new ArrayList<>();
        ArrayList<Message> m = getList();
        for (int i = m.size() - 1; i >= 0; i--) {
            if (!tmp.contains(m.get(i).user1)) {
                tmp.add(m.get(i).user1);
                res.add(User.get(m.get(i).user1));
            }
            if (!tmp.contains(m.get(i).user2)) {
                tmp.add(m.get(i).user2);
                res.add(User.get(m.get(i).user2));
            }
        }
        return res;
    }
}
