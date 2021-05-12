package db.queryBuilder;

import model.Message;
import model.User;

public class MessageQueryBuilder extends QueryBuilder<Message> {

    public MessageQueryBuilder() {
        super();
    }

    public MessageQueryBuilder getByUser1(int user) {
        addCustomFilter(message -> message.getSender() == user);
        return this;
    }

    public MessageQueryBuilder getByUser2(int user) {
        addCustomFilter(message -> message.getReceiver() == user);
        return this;
    }

    public MessageQueryBuilder getByTwoUser(int user1, int user2) {
        addCustomFilter(message -> message.getReceiver() == user2 && message.getSender() == user1);
        return this;
    }

    public MessageQueryBuilder getTwoUserChats(int user1, int user2) {
        addCustomFilter(message -> (message.getReceiver() == user2 && message.getSender() == user1)
                                    || (message.getReceiver() == user1 && message.getSender() == user2));
        return this;
    }

    public MessageQueryBuilder getByGroup(int group) {
        addCustomFilter(message -> message.getChatGroupId() == group);
        return this;
    }

    public MessageQueryBuilder getBySeen(User user, boolean seen) {
        addCustomFilter(message -> message.hasViewed(user) == seen);
        return this;
    }

    public MessageQueryBuilder getRelatedToUser(int user) {
        addCustomFilter(message -> message.getReceiver() == user || message.getSender() == user);
        return this;
    }
}
