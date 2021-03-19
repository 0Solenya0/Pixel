package Server.models;

import Server.Validators;
import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.ValidationException;
import Server.models.Fields.AccessLevel;
import Server.models.Fields.NotificationType;
import Server.models.Fields.RelType;
import Server.models.Fields.UserField;
import Server.models.Filters.UserFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class User extends Model {
    private static final Logger logger = LogManager.getLogger(User.class);
    public static final String datasrc = "./db/" + User.class.getName();

    public AccessLevel visibility;
    public String name, surname, username, bio;
    private UserField<String> mail, phone;
    private UserField<LocalDateTime> lastseen;
    private UserField<LocalDate> birthdate;
    private String password;
    public boolean isEnabled;

    public AccessLevel getVisibility() {
        return visibility;
    }
    public String getFullName() {
        return this.name + " " + this.surname;
    }
    public UserField<String> getMail() {
        return mail;
    }
    public UserField<String> getPhone() {
        return phone;
    }
    public UserField<LocalDateTime> getLastSeen() {
        return lastseen;
    }
    public UserField<LocalDate> getBirthdate() {
        return birthdate;
    }

    public User(String name, String surname, String username, String mail, String password) {
        super();
        this.birthdate = new UserField<>();
        this.lastseen = new UserField<>();
        this.phone = new UserField<>();
        this.mail = new UserField<>();

        /** default access levels and values **/
        this.mail.setAccessLevel(AccessLevel.PUBLIC);
        this.lastseen.setAccessLevel(AccessLevel.PUBLIC);
        this.lastseen.set(LocalDateTime.MIN);
        this.phone.setAccessLevel(AccessLevel.PRIVATE);
        this.phone.set("");
        this.birthdate.setAccessLevel(AccessLevel.PRIVATE);
        this.birthdate.set(LocalDate.MIN);

        this.bio = "";
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.mail.set(mail);
        this.password = password;
        this.isEnabled = true;
        this.visibility = AccessLevel.PUBLIC;
    }
    public User(int id) throws ConnectionException {
        this(
                loadJSON(id, datasrc).getString("name"),
                loadJSON(id, datasrc).getString("surname"),
                loadJSON(id, datasrc).getString("username"),
                "",
                loadJSON(id, datasrc).getString("password")
        );
        this.id = id;

        JSONObject user = loadJSON(id, datasrc);
        this.isDeleted = loadJSON(id, getDataSource()).getBoolean("isDeleted");
        this.bio = user.getString("bio");

        JSONObject obj = (JSONObject) user.get("phone");
        this.phone.set(obj.getString("value"));
        this.phone.setAccessLevel(AccessLevel.valueOf(obj.getString("access")));

        obj = (JSONObject) user.get("mail");
        this.mail.set(obj.getString("value"));
        this.mail.setAccessLevel(AccessLevel.valueOf(obj.getString("access")));

        obj = (JSONObject) user.get("lastseen");
        this.lastseen.set(LocalDateTime.parse(obj.getString("value")));
        this.lastseen.setAccessLevel(AccessLevel.valueOf(obj.getString("access")));

        obj = (JSONObject) user.get("birthdate");
        this.birthdate.set(LocalDate.parse(obj.getString("value")));
        this.birthdate.setAccessLevel(AccessLevel.valueOf(obj.getString("access")));

        this.visibility = AccessLevel.valueOf(user.getString("visibility"));
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    public void changePassword(String password) throws ValidationException, ConnectionException {
        this.password = password;
        this.save();
    }
    public void updateLastSeen() throws ValidationException, ConnectionException {
        lastseen.set(LocalDateTime.now());
        save();
        logger.info(String.format("LastSeen for user %s with id %s updated.", username, id));
    }

    public void follow(int id) throws Exception {
        if (getRel(id) != null && getRel(id).type == RelType.FOLLOW)
            return;
        resetRel(id);
        if (User.get(id).getVisibility() == AccessLevel.PRIVATE)
            (new Notification(this.id, id, NotificationType.REQUEST)).save();
        else {
            (new Relation(this.id, id, RelType.FOLLOW)).save();
            (new Notification(0, id, username + " has started following you")).save();
        }
    }
    public void block(int id) throws Exception {
        if (User.get(id).getRel(this.id) != null && User.get(id).getRel(this.id).type == RelType.FOLLOW)
            User.get(id).getRel(this.id).delete();
        resetRel(id);
        (new Relation(this.id, id, RelType.BLOCKED)).save();
    }
    public void resetRel(int id) throws Exception {
        if (getRel(id) != null) {
            if (getRel(id).type == RelType.FOLLOW)
                (new Notification(0, id, username + " has stopped following you")).save();
            getRel(id).delete();
        }
    }
    public Relation getRel(int id) throws Exception {
        return Relation.getFilter().getByTwoUser(this.id, id);
    }
    public ArrayList<User> getFollowers() throws Exception {
        ArrayList<Relation> rel = Relation.getFilter().userCustomFilter(relation -> relation.user2 == this.id && relation.type == RelType.FOLLOW).getList();
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(User.get(relation.user1));
        return res;
    }
    public ArrayList<User> getFollowings() throws Exception {
        ArrayList<Relation> rel = Relation.getFilter().userCustomFilter(relation -> relation.user1 == this.id && relation.type == RelType.FOLLOW).getList();
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(User.get(relation.user2));
        return res;
    }
    public ArrayList<User> getBlackList() throws Exception {
        ArrayList<Relation> rel = Relation.getFilter().userCustomFilter(relation -> relation.user1 == this.id && relation.type == RelType.BLOCKED).getList();
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(User.get(relation.user2));
        return res;
    }

    /** Must be in every model section **/
    public static User get(int id) throws ConnectionException {
        return new User(id);
    }
    public static UserFilter getFilter() throws ConnectionException {
        return new UserFilter();
    }

    /** Inherited **/
    public JSONObject getJSON() {
        JSONObject user = new JSONObject();
        user.put("name",  name);
        user.put("password", password);
        user.put("surname", surname);
        user.put("username", username);
        user.put("bio", bio);
        user.put("isEnabled", isEnabled);
        user.put("mail", mail.getJSON());
        user.put("phone", phone.getJSON());
        user.put("birthdate", birthdate.getJSON());
        user.put("lastseen", lastseen.getJSON());
        user.put("visibility", visibility);
        return user;
    }
    public void isValid() throws ValidationException, ConnectionException {
        if (mail.get() == null) {
            logger.debug(String.format("Email Validation Failed - mail is null. UserId: %d ", id));
            throw new ValidationException("mail", "User", "Email field is empty");
        }
        if (!Validators.isValidMail(mail.get())) {
            logger.debug(String.format("Email Validation Failed - bad format. UserId: %d , Input: %s", id, mail.get()));
            throw new ValidationException("mail", "User", "Email bad format");
        }
        if (!Validators.isValidPhone(phone.get())) {
            logger.debug("Phone number is not valid");
            throw new ValidationException("Phone Number", "User", "Phone number is not valid");
        }
        if (getFilter().getByMail(mail.get()) != null && getFilter().getByMail(mail.get()).id != this.id) {
            logger.debug("Email already exists");
            throw new ValidationException("mail", "User", "Email already exists");
        }
        if (getFilter().getByUsername(username) != null && getFilter().getByUsername(username).id != this.id) {
            logger.debug("User already exists");
            throw new ValidationException("user", "User", "User already exists");
        }
        if (!phone.get().equals("") && getFilter().getByPhone(phone.get()) != null && getFilter().getByPhone(phone.get()).id != this.id) {
            logger.debug("Phone number already exists");
            throw new ValidationException("Phone Number", "User", "Phone number already exists");
        }
    }
}