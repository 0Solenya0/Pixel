package Server.models.Filters;

import Server.models.Exceptions.ConnectionException;
import Server.models.Group;

public class GroupFilter extends ModelFilter<Group> {

    public GroupFilter() throws ConnectionException {
        super(Group.class);
    }

    public GroupFilter getByOwner(int user) {
        customFilter(group -> group.owner == user);
        return this;
    }
}
