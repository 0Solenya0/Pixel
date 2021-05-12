package db.dbSet;

import model.Message;
import model.Relation;
import model.field.RelStatus;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.MessageQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageDBSet extends DBSet<Message> {
    private static final Logger logger = LogManager.getLogger(MessageDBSet.class);
    public MessageDBSet() {
        super(Message.class);
    }

    @Override
    public void validate(Message model) throws ConnectionException, ValidationException {
        ValidationException validationException = new ValidationException();
        RelationDBSet relationDBSet = new RelationDBSet();
        Relation relation = relationDBSet.getFirst(relationDBSet.getQueryBuilder()
                .getByTwoUser(model.getSender(), model.getReceiver()).getQuery());
        Relation relationR = relationDBSet.getFirst(relationDBSet.getQueryBuilder()
                .getByTwoUser(model.getReceiver(), model.getSender()).getQuery());

        if (model.getReceiver() != 0 && model.getSender() != model.getReceiver()
            && !((relation != null && relation.getType() == RelStatus.FOLLOW)
                || (relationR != null && relationR.getType() == RelStatus.FOLLOW)))
                validationException.addError("User", "Sender and receiver doesn't follow each other");
        if ((model.getContent() == null || model.getContent().isBlank()) && model.getTweetId() == 0)
            validationException.addError("Content", "Message does not have any content");

        if (validationException.hasError()) {
            logger.debug(validationException.getLog());
            throw validationException;
        }
    }

    @Override
    public MessageQueryBuilder getQueryBuilder() {
        return new MessageQueryBuilder();
    }
}
