package model;

import model.field.AccessLevel;
import model.field.LockedField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class User extends Model {
    private static final Logger logger = LogManager.getLogger(User.class);

    private AccessLevel visibility;
    private String name, surname;
    private String username, bio;
    private LockedField<String> mail, phone;
    private LockedField<LocalDateTime> lastseen;
    private LockedField<LocalDate> birthdate;
    private String password;
    private TreeSet<Integer> muted;
    private boolean isEnabled;
    private String photo;

    public User(String name, String surname, String username, String mail, String password) {
        super();
        this.birthdate = new LockedField<>();
        this.lastseen = new LockedField<>();
        this.phone = new LockedField<>();
        this.mail = new LockedField<>();
        this.muted = new TreeSet<>();


        /** default access levels and values **/
        this.mail.setAccessLevel(AccessLevel.PRIVATE);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LockedField<String> getMail() {
        return mail;
    }

    public void setMail(LockedField<String> mail) {
        this.mail = mail;
    }

    public LockedField<String> getPhone() {
        return phone;
    }

    public LockedField<LocalDateTime> getLastseen() {
        return lastseen;
    }

    public LockedField<LocalDate> getBirthdate() {
        return birthdate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPhone(LockedField<String> phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void muteUser(User user) {
        muted.add(user.id);
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void unMuteUser(User user) {
        muted.remove(user.id);
    }

    public boolean isMuted(User user) {
        return muted.contains(user.id);
    }

    public AccessLevel getVisibility() {
        return visibility;
    }

    public String getFullName() {
        return getName() + " " + getSurname();
    }

    public void setVisibility(AccessLevel visibility) {
        this.visibility = visibility;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;

        User that = (User) obj;
        return id == that.id;
    }
}