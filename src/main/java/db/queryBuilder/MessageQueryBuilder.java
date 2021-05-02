package db.queryBuilder;

import apps.messenger.model.Message;

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

    public MessageQueryBuilder getBySeen(boolean seen) {
        addCustomFilter(message -> message.isSeen() == seen);
        return this;
    }

    public MessageQueryBuilder getRelatedToUser(int user) {
        addCustomFilter(message -> message.getReceiver() == user || message.getSender() == user);
        return this;
    }
}
