package Server.models;

import Server.Validators;
import Server.models.Fields.AccessLevel;
import Server.models.Fields.RelType;
import Server.models.Fields.UserField;
import Server.models.Filters.UserFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User extends Model {
    private static final Logger logger = LogManager.getLogger(User.class);

    public static final String datasrc = "./db/Users";
    public String getdatasrc() {
        return datasrc;
    }

    public String name, surname, username, bio;
    private UserField<String> mail, phone;
    private UserField<LocalDateTime> lastseen;
    private UserField<LocalDate> birthdate;
    private String password;
    public boolean isEnabled;

    public String getPassword() {
        return password;
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
    public UserField<LocalDateTime> getLastseen() {
        return lastseen;
    }
    public UserField<LocalDate> getBirthdate() {
        return birthdate;
    }

    public User(String name, String surname, String username, String mail, String password) {
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
        this.isActive = true;
        this.isEnabled = true;
    }
    public User(int id) throws IOException {
        this(
                loadJSON(id, datasrc).getString("name"),
                loadJSON(id, datasrc).getString("surname"),
                loadJSON(id, datasrc).getString("username"),
                "",
                loadJSON(id, datasrc).getString("password")
        );
        JSONObject user = loadJSON(id, datasrc);
        this.id = id;
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

        this.isActive = Boolean.parseBoolean(user.get("isActive").toString());
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    public void updateLastSeen() throws Exception {
        lastseen.set(LocalDateTime.now());
        save();
        logger.info(String.format("Lastseen for user %s updated.", username));
    }

    public void follow(int id) throws Exception {
        resetRel(id);
        (new Relation(this.id, id, RelType.FOLLOW)).save();
    }
    public void block(int id) throws Exception {
        resetRel(id);
        (new Relation(this.id, id, RelType.BLOCKED)).save();
    }
    public void resetRel(int id) throws Exception {
        if (getRel(id) != null)
            getRel(id).delete();
    }
    public Relation getRel(int id) throws Exception {
        return Relation.getFilter().getByTwoUser(this.id, id);
    }

    /** Must be in every model section **/
    public static User get(int id) throws IOException {
        return new User(id);
    }
    public static UserFilter getFilter() throws IOException {
        return new UserFilter();
    }

    /** Inherited **/
    public JSONObject getJSON() {
        JSONObject user = new JSONObject();
        user.put("id", id);
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
        return user;
    }
    public boolean isValid() throws Exception {
        if (mail.get() == null) {
            logger.debug(String.format("Email Validation Failed - mail is null. UserId: %d ", id));
            throw new Exception("Email field is empty");
        }
        if (!Validators.isValidMail(mail.get())) {
            logger.debug(String.format("Email Validation Failed - bad format. UserId: %d , Input: %s", id, mail.get()));
            throw new Exception("Email bad format");
        }
        if (!Validators.isValidPhone(phone.get())) {
            logger.debug("Phone number is not valid");
            throw new Exception("Phone number is not valid");
        }
        if (getFilter().getByMail(mail.get()) != null && getFilter().getByMail(mail.get()).id != this.id) {
            logger.debug("Email already exists");
            throw new Exception("Email already exists");
        }
        if (getFilter().getByUsername(username) != null && getFilter().getByUsername(username).id != this.id) {
            logger.debug("User already exists");
            throw new Exception("User already exists");
        }
        if (!phone.get().equals("") && getFilter().getByPhone(phone.get()) != null && getFilter().getByPhone(phone.get()).id != this.id) {
            logger.debug("Phone number already exists");
            throw new Exception("Phone number already exists");
        }
        return true;
    }
}