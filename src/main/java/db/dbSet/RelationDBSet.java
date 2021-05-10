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

    public ArrayList<User> getFollowers(User user) throws ConnectionException {
        UserDBSet userDBSet = new UserDBSet();
        ArrayList<Relation> rel = getAll(getQueryBuilder()
                .getByUser2(user).getByType(RelStatus.FOLLOW).getEnabled().getQuery());
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(userDBSet.get(relation.getSender()));
        return res;
    }

    public ArrayList<User> getFollowing(User user) throws ConnectionException {
        UserDBSet userDBSet = new UserDBSet();
        ArrayList<Relation> rel = getAll(getQueryBuilder()
                .getByUser1(user).getByType(RelStatus.FOLLOW).getEnabled().getQuery());
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(userDBSet.get(relation.getReceiver()));
        return res;
    }

    public ArrayList<User> getBlackList(User user) throws ConnectionException {
        UserDBSet userDBSet = new UserDBSet();
        ArrayList<Relation> rel = getAll(getQueryBuilder()
                .getByUser1(user).getByType(RelStatus.BLOCKED).getEnabled().getQuery());
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(userDBSet.get(relation.getReceiver()));
        return res;
    }

    public void follow(User user, User user2, boolean accepted) throws ConnectionException {
        Relation relation = getFirst(getQueryBuilder().getByTwoUser(user.id, user2.id).getQuery());
        try {
            if (relation != null && relation.getType() == RelStatus.FOLLOW)
                return;
            resetRel(user, user2);
            if (user2.getVisibility() == AccessLevel.PRIVATE && !accepted)
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
            if (relation2 != null && relation2.getType() == RelStatus.BLOCKED)
                return;
            resetRel(user, user2);
            Relation relation = new Relation(user, user2, RelStatus.BLOCKED);
            save(relation);
            broadcast("BLOCK " + user.id + " " + user2.id);
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
