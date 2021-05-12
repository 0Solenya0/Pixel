package db.queryBuilder;

import model.ChatGroup;

public class ChatGroupQueryBuilder extends QueryBuilder<ChatGroup> {
    public ChatGroupQueryBuilder() {
        super();
    }

    public ChatGroupQueryBuilder getByOwner(int user) {
        addCustomFilter(group -> group.getOwner() == user);
        return this;
    }

    public ChatGroupQueryBuilder getByMember(int user) {
        addCustomFilter(group -> group.isMember(user));
        return this;
    }
}
