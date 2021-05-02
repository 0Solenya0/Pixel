package db.queryBuilder;

import apps.messenger.model.Group;

public class GroupQueryBuilder extends QueryBuilder<Group> {

    public GroupQueryBuilder() {
        super();
    }

    public GroupQueryBuilder getByOwner(int user) {
        addCustomFilter(group -> group.getOwner() == user);
        return this;
    }
}
