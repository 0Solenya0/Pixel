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
public class User {

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

    @OneToMany(mappedBy = "user")
    private List<MuteRelation> muteRelations = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<FollowRelation> followings = new ArrayList<>();

    @OneToMany(mappedBy = "target")
    private List<FollowRelation> followers = new ArrayList<>();

    public ArrayList<User> getFollowings() {
        ArrayList<User> res = new ArrayList<>();
        followings.forEach((followRelation -> res.add(followRelation.getTarget())));
        return res;
    }

    public ArrayList<User> getFollowers() {
        ArrayList<User> res = new ArrayList<>();
        followers.forEach((followRelation -> res.add(followRelation.getUser())));
        return res;
    }

    public ArrayList<User> getMuted() {
        ArrayList<User> res = new ArrayList<>();
        muteRelations.forEach((muteRelation -> res.add(muteRelation.getTarget())));
        return res;
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

    public AccessField<String> getPhone() {
        return phone;
    }

    public void setPhone(AccessField<String> phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}