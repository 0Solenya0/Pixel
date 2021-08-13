package shared.models;

import com.google.gson.annotations.Expose;
import shared.models.fields.AccessField;
import shared.models.fields.AccessLevel;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Account")
public class User extends Model {

    @Column(unique = true, nullable = false)
    @Expose
    private String username;

    @Column(unique = true, nullable = false)
    @Expose
    private AccessField<String> mail = new AccessField<>();

    @Expose
    private AccessField<String> phone = new AccessField<>();

    @Enumerated(EnumType.STRING)
    @Expose
    private AccessLevel visibility = AccessLevel.CONTACTS;

    @Column(nullable = false)
    @Expose
    private String password;

    @Column(nullable = false)
    @Expose
    private String name;

    @Column(nullable = false)
    @Expose
    private String surname;

    @Expose
    private String bio;

    @Column(nullable = false)
    @Expose
    private AccessField<LocalDateTime> lastSeen = new AccessField<>();

    @Column(nullable = false)
    @Expose
    private AccessField<LocalDate> birthdate = new AccessField<>();

    @Column(nullable = false)
    @Expose
    private boolean isEnabled = true;

    @Expose
    private byte[] photo;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "follow_table",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name="follower_id"))
    public List<User> followers = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "follow_table",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    public List<User> followings = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "block_table",
            joinColumns = @JoinColumn(name = "blocker_id"),
            inverseJoinColumns = @JoinColumn(name="blocked_id"))
    public List<User> blocked = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "block_table",
            joinColumns = @JoinColumn(name = "blocked_id"),
            inverseJoinColumns = @JoinColumn(name = "blocker_id"))
    public List<User> blockedBy = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "mute_table",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "muted_id"))
    public List<User> muted = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(
            name = "mute_table",
            joinColumns = @JoinColumn(name = "muted_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    public List<User> mutedBy = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<UserList> userLists = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Tweet> tweets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    private List<Notification> sentNotifications = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private List<Notification> receivedNotifications = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private List<Message> receivedMessages = new ArrayList<>();

    public AccessLevel getVisibility() {
        return visibility;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccessField<String> getMail() {
        return mail;
    }

    public void setMail(AccessField<String> mail) {
        this.mail = mail;
    }

    public void setMailAddress(String mailAddress) {
        if (mailAddress == null || mailAddress.equals(""))
            mail = null;
        else if (mail == null) {
            mail = new AccessField<>();
            mail.set(mailAddress);
            mail.setAccessLevel(AccessLevel.PRIVATE);
        }
        else
            mail.set(mailAddress);
    }

    public void setVisibility(AccessLevel visibility) {
        this.visibility = visibility;
    }

    public AccessField<String> getPhone() {
        return phone;
    }

    public void setPhone(AccessField<String> phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public AccessField<LocalDateTime> getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(AccessField<LocalDateTime> lastSeen) {
        this.lastSeen = lastSeen;
    }

    public AccessField<LocalDate> getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(AccessField<LocalDate> birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowings() {
        return followings;
    }

    public List<User> getBlocked() {
        return blocked;
    }

    public List<User> getBlockedBy() {
        return blockedBy;
    }

    public List<User> getMuted() {
        return muted;
    }

    public List<User> getMutedBy() {
        return mutedBy;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public void setFollowings(List<User> followings) {
        this.followings = followings;
    }

    public void setBlocked(List<User> blocked) {
        this.blocked = blocked;
    }

    public String getFullName() {
        return getName() + " " + getSurname();
    }

    public String toString() {
        return getUsername();
    }
}