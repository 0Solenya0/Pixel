package Server.models;

import Server.Validators;
import Server.models.Fields.AccessLevel;
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

    public static final String datasrc = "./User";
    public String getdatasrc() {
        return datasrc;
    }

    public int id;
    public String name, surname, username, bio;
    public UserField<String> mail, phone;
    public UserField<LocalDateTime> lastseen;
    public UserField<LocalDate> birthdate;
    private String password;
    public boolean isActive;

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
        logger.debug(String.format("new user instance created - %s", getJSON().toString()));
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public JSONObject getJSON() {
        JSONObject user = new JSONObject();
        user.put("id", id);
        user.put("name",  name);
        user.put("password", password);
        user.put("surname", surname);
        user.put("username", username);
        user.put("bio", bio);
        user.put("isActive", isActive);
        user.put("mail", mail.getJSON());
        user.put("phone", phone.getJSON());
        user.put("birthdate", birthdate.getJSON());
        user.put("lastseen", lastseen.getJSON());
        return user;
    }
    public boolean isValid() throws Exception {
        if (mail.get() == null) {
            logger.debug(String.format("Email Validation Failed - mail is null. UserId: %d ", id));
            throw new Exception("Email field is empty.");
        }
        if (!Validators.isValidMail(mail.get())) {
            logger.debug(String.format("Email Validation Failed - bad format. UserId: %d , Input: %s", id, mail.get()));
            throw new Exception("Email bad format.");
        }
        if (getFilter().getMail(mail.get()) != null) {
            logger.debug("Email already exists.");
            throw new Exception("Email already exists");
        }
        if (getFilter().getUsername(username) != null) {
            logger.debug("User already exists.");
            throw new Exception("User already exists.");
        }
        if (!phone.get().equals("") && getFilter().getPhone(phone.get()) != null) {
            logger.debug("Phone number already exists.");
            throw new Exception("Phone number already exists");
        }
        if (!Validators.isValidPhone(phone.get())) {
            logger.debug("Phone number already exists.");
            throw new Exception("Phone number is not valid.");
        }
        return true;
    }
    public void updateLastSeen() throws Exception {
        lastseen.set(LocalDateTime.now());
        save();
        logger.info(String.format("Lastseen for user %s updated.", username));
    }

    public static User get(int id) throws IOException {
        JSONObject user = loadJSON(id, datasrc);
        User u = new User(
                user.getString("name"),
                user.getString("surname"),
                user.getString("username"),
                "",
                user.getString("password")
                );
        u.id = id;
        u.bio = user.getString("bio");

        JSONObject obj = (JSONObject) user.get("phone");
        u.phone.set(obj.getString("value"));
        u.phone.setAccessLevel(AccessLevel.valueOf(obj.getString("access")));

        obj = (JSONObject) user.get("mail");
        u.mail.set(obj.getString("value"));
        u.mail.setAccessLevel(AccessLevel.valueOf(obj.getString("access")));

        obj = (JSONObject) user.get("lastseen");
        u.lastseen.set(LocalDateTime.parse(obj.getString("value")));
        u.lastseen.setAccessLevel(AccessLevel.valueOf(obj.getString("access")));

        obj = (JSONObject) user.get("birthdate");
        u.birthdate.set(LocalDate.parse(obj.getString("value")));
        u.birthdate.setAccessLevel(AccessLevel.valueOf(obj.getString("access")));

        logger.info(String.format("UserId %s fetched successfully. %s", id, u.getJSON()));
        return u;
    }
    public static UserFilter getFilter() throws IOException {
        ArrayList<User> res = new ArrayList<>();
        for (int i = 1; i <= getLastId(datasrc); i++)
            res.add(get(i));
        return new UserFilter(res);
    }
}