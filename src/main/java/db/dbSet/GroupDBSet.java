package db.dbSet;

import apps.messenger.model.Group;
import apps.messenger.model.Message;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.GroupQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupDBSet extends DBSet<Group> {
    private static final Logger logger = LogManager.getLogger(GroupDBSet.class);
    public GroupDBSet() {
        super(Group.class);
    }

    @Override
    public void validate(Group model) throws ConnectionException, ValidationException {

    }

    @Override
    public GroupQueryBuilder getQueryBuilder() {
        return new GroupQueryBuilder();
    }
}
