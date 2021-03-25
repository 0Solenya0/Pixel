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
        customFilter(message -> message.getSender() == user);
        return this;
    }
    public MessageFilter getByUser2(int user) {
        customFilter(message -> message.getReceiver() == user);
        return this;
    }
    public MessageFilter getByTwoUser(int user1, int user2) {
        customFilter(message -> message.getReceiver() == user2 && message.getSender() == user1);
        return this;
    }
    public MessageFilter getBySeen(boolean seen) {
        customFilter(message -> message.isSeen() == seen);
        return this;
    }
    public MessageFilter getRelatedToUser(int user) {
        customFilter(message -> message.getReceiver() == user || message.getSender() == user);
        return this;
    }

    /** Newest messages are at start of list **/
    public ArrayList<User> getUserList() throws ConnectionException {
        TreeSet<Integer> tmp = new TreeSet<>();
        ArrayList<User> res = new ArrayList<>();
        ArrayList<Message> m = getList();
        for (int i = m.size() - 1; i >= 0; i--) {
            if (!tmp.contains(m.get(i).getSender())) {
                tmp.add(m.get(i).getSender());
                res.add(User.get(m.get(i).getSender()));
            }
            if (!tmp.contains(m.get(i).getReceiver())) {
                tmp.add(m.get(i).getReceiver());
                res.add(User.get(m.get(i).getReceiver()));
            }
        }
        return res;
    }
}
