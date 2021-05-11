package db.dbSet;

import model.User;
import model.field.AccessLevel;
import model.Relation;
import model.field.RelStatus;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.RelationQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class RelationDBSet extends DBSet<Relation> {
    private static final Logger logger = LogManager.getLogger(RelationDBSet.class);
    public RelationDBSet() {
        super(Relation.class);
    }

    @Override
    public void validate(Relation model) throws ConnectionException, ValidationException {
        ValidationException validationException = new ValidationException();
        if (model.getSender() == model.getReceiver())
            validationException.addError("User", "User can't have relation with itself");
        Relation relation = getFirst(getQueryBuilder().getByTwoUser(model.getSender(), model.getReceiver()).getQuery());
        if (relation != null && model.id != relation.id)
            validationException.addError("Relation","Relationship already exists");
        if (validationException.hasError()) {
            logger.debug(validationException.getLog());
            throw validationException;
        }
    }

    @Override
    public RelationQueryBuilder getQueryBuilder() {
        return new RelationQueryBuilder();
    }
}
