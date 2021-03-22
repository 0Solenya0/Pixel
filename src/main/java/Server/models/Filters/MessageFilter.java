package Server.models.Filters;

import Server.models.Exceptions.ConnectionException;
import Server.models.Message;

import java.util.ArrayList;
import java.util.TreeMap;

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
}
