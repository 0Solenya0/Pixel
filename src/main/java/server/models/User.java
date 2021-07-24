package server.models;

import server.models.fields.AccessField;
import server.models.fields.AccessLevel;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Account")
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private AccessField<String> mail, phone;

    @Enumerated(EnumType.STRING)
    private AccessLevel visibility;

    @Column(nullable = false)
    private String password;

    private String name, surname, bio;
    private AccessField<LocalDateTime> lastSeen;
    private AccessField<LocalDate> birthdate;

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

    @ManyToMany
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

    public int getId() {
        return id;
    }
}