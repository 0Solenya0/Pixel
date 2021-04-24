package db.dbSet;

import apps.auth.model.User;
import db.dbSet.DBSet;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.QueryBuilder;
import db.queryBuilder.UserQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import validators.UserValidators;

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

    @Override
    public UserQueryBuilder getQueryBuilder() {
        return new UserQueryBuilder();
    }
}
