package db.dbSet;

import apps.auth.model.User;
import apps.auth.model.fields.AccessLevel;
import apps.relation.model.Relation;
import apps.relation.model.field.RelStatus;
import db.dbSet.DBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.QueryBuilder;
import db.queryBuilder.UserQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validators.UserValidators;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDBSet extends DBSet<User> {
    private static final Logger logger = LogManager.getLogger(UserDBSet.class);
    public UserDBSet() {
        super(User.class);
    }

    @Override
    public void validate(User model) throws ConnectionException, ValidationException {
        ValidationException validationException = new ValidationException();
        if (model.getUsername().isEmpty())
            validationException.addError("Username", "Username is empty");
        if (model.getMail().get() == null)
            validationException.addError("Email", "Email field is empty");
        if (!UserValidators.isValidMail(model.getMail().get()))
            validationException.addError("Email", "Email bad format");
        if (!UserValidators.isValidPhone(model.getPhone().get()))
            validationException.addError("Phone", "Phone number is not valid");

        User tmp = getFirst(getQueryBuilder().getByMail(model.getMail().get()).getQuery());
        if (tmp != null && tmp.id != model.id)
            validationException.addError("Email", "Email already exists");
        tmp = getFirst(getQueryBuilder().getByUsername(model.getUsername()).getQuery());
        if (tmp != null && tmp.id != model.id)
            validationException.addError("Username", "Username already exists");
        tmp = getFirst(getQueryBuilder().getByPhone(model.getPhone().get()).getQuery());
        if (!model.getPhone().get().equals("") && tmp != null && tmp.id != model.id)
            validationException.addError("Phone", "Phone number already exists");

        if (validationException.hasError()) {
            logger.debug(validationException.getLog());
            throw validationException;
        }
    }

    public User getByAccess(User request, int id) throws ConnectionException {
        User user = get(id);
        RelationDBSet relationDBSet = new RelationDBSet();
        Relation relation = relationDBSet.getFirst(
                relationDBSet.getQueryBuilder()
                .getByTwoUser(request.id, id).getQuery()
        );
        AccessLevel level = AccessLevel.PUBLIC;
        if (relation != null && relation.getType() == RelStatus.FOLLOW)
            level = AccessLevel.CONTACTS;
        if (request.id == id)
            level = AccessLevel.PRIVATE;
        if (level != AccessLevel.PRIVATE) {
            if (user.getPhone().getAccessLevel() == AccessLevel.PRIVATE
                    || (user.getPhone().getAccessLevel() == AccessLevel.CONTACTS && level != AccessLevel.CONTACTS))
                user.getPhone().set("");
            if (user.getMail().getAccessLevel() == AccessLevel.PRIVATE
                    || (user.getMail().getAccessLevel() == AccessLevel.CONTACTS && level != AccessLevel.CONTACTS))
                user.getMail().set("");
            if (user.getBirthdate().getAccessLevel() == AccessLevel.PRIVATE
                    || (user.getBirthdate().getAccessLevel() == AccessLevel.CONTACTS && level != AccessLevel.CONTACTS))
                user.getBirthdate().set(LocalDate.MIN);
            if (user.getLastseen().getAccessLevel() == AccessLevel.PRIVATE
                    || (user.getLastseen().getAccessLevel() == AccessLevel.CONTACTS && level != AccessLevel.CONTACTS))
                user.getLastseen().set(LocalDateTime.MIN);
        }
        return user;
    }

    @Override
    public UserQueryBuilder getQueryBuilder() {
        return new UserQueryBuilder();
    }

    public void muteUser(User user, User user2) throws ConnectionException {
        user = get(user.id);
        user.muteUser(user2);
        try {
            save(user);
        }
        catch (ValidationException e) {
            logger.error("mute user failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

    public void unMuteUser(User user, User user2) throws ConnectionException {
        user = get(user.id);
        user.unMuteUser(user2);
        try {
            save(user);
        }
        catch (ValidationException e) {
            logger.error("mute user failed by unexpected validation error");
            logger.error(e.getLog());
        }
    }

}
