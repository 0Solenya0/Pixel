package shared.models;

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
    private String username;

    @Column(unique = true, nullable = false)
    private AccessField<String> mail = new AccessField<>();

    private AccessField<String> phone = new AccessField<>();

    @Enumerated(EnumType.STRING)
    private AccessLevel visibility = AccessLevel.PRIVATE;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    private String bio;

    @Column(nullable = false)
    private AccessField<LocalDateTime> lastSeen = new AccessField<>();

    @Column(nullable = false)
    private AccessField<LocalDate> birthdate = new AccessField<>();

    @Column(nullable = false)
    private boolean isEnabled = true;

    private byte[] photo;

    @ManyToMany
    @JoinTable(
            name = "follow_table",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name="follower_id"))
    public List<User> followers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "follow_table",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    public List<User> followings = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "block_table",
            joinColumns = @JoinColumn(name = "blocker_id"),
            inverseJoinColumns = @JoinColumn(name="blocked_id"))
    public List<User> blocked = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "block_table",
            joinColumns = @JoinColumn(name = "blocked_id"),
            inverseJoinColumns = @JoinColumn(name = "blocker_id"))
    public List<User> blocked_by = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "mute_table",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "muted_id"))
    public List<User> muted = new ArrayList<>();

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
}