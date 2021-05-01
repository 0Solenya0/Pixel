package db.dbSet;

import apps.auth.model.User;
import apps.auth.model.fields.AccessLevel;
import apps.relation.model.Relation;
import apps.relation.model.field.RelStatus;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import listener.ListenableClass;
import listener.StringListener;
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
        if (validationException.hasError())
            throw validationException;
    }

    @Override
    public RelationQueryBuilder getQueryBuilder() {
        return new RelationQueryBuilder();
    }

    public void follow(User user, User user2) throws ConnectionException {
        Relation relation = getFirst(getQueryBuilder().getByTwoUser(user.id, user2.id).getQuery());
        try {
            if (relation != null && relation.getType() == RelStatus.FOLLOW)
                return;
            resetRel(user, user2);
            if (user2.getVisibility() == AccessLevel.PRIVATE)
                broadcast("REQUEST " + user.id + " " + user2.id);
            else {
                relation = new Relation(user, user2, RelStatus.FOLLOW);
                save(relation);
                broadcast("FOLLOW " + user.id + " " + user2.id);
            }
        }
        catch (ValidationException e) {
            logger.error("follow user failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void block(User user, User user2) throws ConnectionException {
        Relation relation2 = getFirst(getQueryBuilder().getByTwoUser(user2.id, user.id).getQuery());
        try {
            if (relation2 != null && relation2.getType() == RelStatus.FOLLOW)
                resetRel(user2, user);
            resetRel(user, user2);
            Relation relation = new Relation(user, user2, RelStatus.BLOCKED);
            save(relation);
        }
        catch (ValidationException e) {
            logger.error("block user failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void resetRel(User user, User user2) throws ConnectionException {
        Relation relation = getFirst(getQueryBuilder().getByTwoUser(user.id, user2.id).getQuery());
        if (relation != null) {
            if (relation.getType() == RelStatus.FOLLOW)
                broadcast("UNFOLLOW " + user.id + " " + user2.id);
            delete(relation);
        }
    }
}