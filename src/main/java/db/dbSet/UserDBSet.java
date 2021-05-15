package db.dbSet;

import model.User;
import model.field.AccessLevel;
import model.Relation;
import model.field.RelStatus;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import db.queryBuilder.UserQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.validator.UserValidators;

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
        if (model.getSurname().isEmpty())
            validationException.addError("Surname", "Surname is empty");
        if (model.getName().isEmpty())
            validationException.addError("Name", "Name is empty");
        if (model.getUsername().isEmpty())
            validationException.addError("Username", "Username is empty");
        if (model.getMail().get() == null)
            validationException.addError("Email", "Email field is empty");
        if (!UserValidators.isValidMail(model.getMail().get()))
            validationException.addError("Email", "Email is invalid");
        if (!UserValidators.isValidPhone(model.getPhone().get()))
            validationException.addError("Phone", "Phone number is invalid");

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
