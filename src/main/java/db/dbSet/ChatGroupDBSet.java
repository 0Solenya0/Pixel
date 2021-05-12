package db.dbSet;

import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.ChatGroupQueryBuilder;
import db.queryBuilder.GroupQueryBuilder;
import model.ChatGroup;
import model.Group;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatGroupDBSet extends DBSet<ChatGroup> {
    private static final Logger logger = LogManager.getLogger(ChatGroupDBSet.class);
    public ChatGroupDBSet() {
        super(ChatGroup.class);
    }

    @Override
    public void validate(ChatGroup model) throws ConnectionException, ValidationException {
        ValidationException validationException = new ValidationException();
        if (model.getOwner() == 0)
            validationException.addError("Owner", "Group has no owner");
        if (model.getName() == null || model.getName().isEmpty())
            validationException.addError("Name", "Group has no name");
        if (validationException.hasError())
            throw validationException;
    }

    @Override
    public ChatGroupQueryBuilder getQueryBuilder() {
        return new ChatGroupQueryBuilder();
    }
}
