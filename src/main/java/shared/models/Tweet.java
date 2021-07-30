package shared.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tweet extends Model {

    @ManyToOne
    private Tweet parent;

    @OneToMany(mappedBy = "parent")
    private List<Tweet> comments = new ArrayList<>();

    @ManyToOne
    private Tweet retweet;

    @OneToMany(mappedBy = "retweet")
    private List<Tweet> retweets = new ArrayList<>();

    @ManyToOne(optional = false)
    private User author;

    @ManyToMany
    @JoinTable(
            name = "likes_table",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> likes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "reports_table",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> reports = new ArrayList<>();

    private byte[] photo;

    private String content;

    public Tweet getParent() {
        return parent;
    }

    public void setParent(Tweet parent) {
        this.parent = parent;
    }
}
