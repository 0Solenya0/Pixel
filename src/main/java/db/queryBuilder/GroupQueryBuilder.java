package db.queryBuilder;

import model.Group;

public class GroupQueryBuilder extends QueryBuilder<Group> {

    public GroupQueryBuilder() {
        super();
    }

    public GroupQueryBuilder getByOwner(int user) {
        addCustomFilter(group -> group.getOwner() == user);
        return this;
    }
}
