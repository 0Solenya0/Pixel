package Server.models;

import Client.CLI.UserUtility;
import Server.Validators;
import Server.models.Exceptions.ConnectionException;
import Server.models.Exceptions.InvalidRequestException;
import Server.models.Exceptions.ValidationException;
import Server.models.Fields.*;
import Server.models.Filters.UserFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public class User extends Model {
    private static final Logger logger = LogManager.getLogger(User.class);

    public AccessLevel visibility;
    private String name, surname;
    public String username, bio;
    private UserField<String> mail, phone;
    private UserField<LocalDateTime> lastseen;
    private UserField<LocalDate> birthdate;
    private String password;
    private TreeSet<Integer> muted;
    private boolean isEnabled;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

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
        this.muted = new TreeSet<>();


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

    public void follow(int id) throws ConnectionException {
        try {
            if (getRel(id) != null && getRel(id).getType() == RelType.FOLLOW)
                return;
            resetRel(id);
            if (User.get(id).getVisibility() == AccessLevel.PRIVATE)
                (new Notification(this.id, id, NotificationType.REQUEST)).save();
            else {
                (new Relation(this.id, id, RelType.FOLLOW)).save();
                (new Notification(0, id, username + " has started following you")).save();
            }
        }
        catch (ValidationException e) { }
    }
    public void block(int id) throws ConnectionException {
        try {
            if (User.get(id).getRel(this.id) != null && User.get(id).getRel(this.id).getType() == RelType.FOLLOW)
                User.get(id).getRel(this.id).delete();
            resetRel(id);
            (new Relation(this.id, id, RelType.BLOCKED)).save();
        }
        catch (ValidationException e) { }
    }
    public void resetRel(int id) throws ConnectionException {
        try {
            if (getRel(id) != null) {
                if (getRel(id).getType() == RelType.FOLLOW)
                    (new Notification(0, id, username + " has stopped following you")).save();
                getRel(id).delete();
            }
        }
        catch (ValidationException e) { }
    }
    public Relation getRel(int id) throws ConnectionException {
        return Relation.getFilter().getByTwoUser(this.id, id);
    }
    public RelStatus getRelationStatus(int id) throws ConnectionException {
        Notification x = Notification.getFilter().getByType(NotificationType.REQUEST).getByTwoUser(this.id, id);
        Relation y = Relation.getFilter().getByTwoUser(this.id, id);
        if (y != null)
            return RelStatus.valueOf(y.getType().toString());
        if (x != null)
            return RelStatus.REQUESTED;
        return RelStatus.NORELATION;
    }
    public ArrayList<User> getFollowers() throws ConnectionException {
        ArrayList<Relation> rel = Relation.getFilter().getByUser2(this.id).getByType(RelType.FOLLOW).getEnabled().getList();
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(User.get(relation.getSender()));
        return res;
    }
    public ArrayList<User> getFollowings() throws ConnectionException {
        ArrayList<Relation> rel = Relation.getFilter().getByUser1(this.id).getByType(RelType.FOLLOW).getEnabled().getList();
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(User.get(relation.getReceiver()));
        return res;
    }
    public ArrayList<User> getBlackList() throws ConnectionException {
        ArrayList<Relation> rel = Relation.getFilter().getByUser1(this.id).getByType(RelType.BLOCKED).getEnabled().getList();
        ArrayList<User> res = new ArrayList<>();
        for (Relation relation : rel)
            res.add(User.get(relation.getReceiver()));
        return res;
    }

    public boolean canMessage(int user) throws ConnectionException {
        if (!User.get(user).isEnabled)
            return false;
        return getRelationStatus(user) == RelStatus.FOLLOW
                || User.get(user).getRelationStatus(id) == RelStatus.FOLLOW;
    }
    public void sendMessage(int user, Message message) throws ValidationException, ConnectionException {
        message.setSender(this.id);
        message.setReceiver(user);
        message.id = 0;
        message.save();
        if (user == this.id)
            message.see();
    }
    public void sendGroupMessage(int groupId, Message message) throws InvalidRequestException, ConnectionException, ValidationException {
        for (User user : getGroup(groupId).getUsers()) {
            try {
                sendMessage(user.id, message);
            }
            catch (ValidationException e) { }
        }
    }

    public ArrayList<Group> getGroups() throws ConnectionException {
        return Group.getFilter().getByOwner(this.id).getList();
    }
    public Group makeNewGroup(String name) throws ValidationException, ConnectionException {
        Group p = new Group(this.id, name);
        p.save();
        return p;
    }
    public void addToGroup(int groupid, int userid) throws ConnectionException, ValidationException, InvalidRequestException {
        getGroup(groupid).addUser(userid);
    }
    public Group getGroup(int groupid) throws InvalidRequestException, ConnectionException {
        for (int i = 0; i < getGroups().size(); i++) {
            if (getGroups().get(i).id == groupid)
                return Group.get(groupid);
        }
        logger.debug("Invalid group was requested for user " + this.id + " and group " + groupid);
        throw new InvalidRequestException("Chosen group does not exists");
    }
    public Group getGroup(String groupName) throws InvalidRequestException, ConnectionException {
        for (int i = 0; i < getGroups().size(); i++) {
            if (getGroups().get(i).name.equals(groupName))
                return getGroups().get(i);
        }
        logger.debug("Invalid group was requested for user " + this.id + " and group " + groupName);
        throw new InvalidRequestException("Chosen group does not exists");
    }

    public void muteUser(int user) throws ConnectionException {
        muted.add(user);
        try {
            save();
        }
        catch (ValidationException e) { }
    }
    public void unMuteUser(int user) throws ConnectionException {
        muted.remove(user);
        try {
            save();
        }
        catch (ValidationException e) { }
    }
    public boolean isMuted(int user) {
        return muted.contains(user);
    }

    public void reportUser(int user) throws ConnectionException {
        if (user == id)
            return;
        try {
            (new Notification(id, user, NotificationType.REPORT)).save();
        }
        catch (ValidationException e) { }
    }

    public void like(int tweet) throws ConnectionException {
        Tweet.get(tweet).like(id);
        try {
            save();
        }
        catch (ValidationException e) { }
    }
    public void disLike(int tweet) throws ConnectionException {
        Tweet.get(tweet).disLike(id);
        try {
            save();
        }
        catch (ValidationException e) { }
    }

    public void deleteUserDependencies() throws ConnectionException {
        for (Relation rel : Relation.getFilter().getByUser1(id).getList())
            rel.delete();
        for (Relation rel : Relation.getFilter().getByUser2(id).getList())
            rel.delete();
        for (Notification notification : Notification.getFilter().getByUser1(id).getList())
            notification.delete();
        for (Notification notification : Notification.getFilter().getByUser2(id).getList())
            notification.delete();
        for (Tweet tweet : Tweet.getFilter().getByUser(username).getList())
            tweet.delete();
    }

    /** Must be in every model section **/
    public static User get(int id) throws ConnectionException {
        return (User) loadObj(id, User.class);
    }
    public static UserFilter getFilter() throws ConnectionException {
        return new UserFilter();
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