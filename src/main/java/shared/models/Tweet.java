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

    public List<User> getLikes() {
        return likes;
    }

    public Tweet getParent() {
        return parent;
    }

    public void setParent(Tweet parent) {
        this.parent = parent;
    }

    public User getAuthor() {
        return author;
    }

    public Tweet getRetweet() {
        return retweet;
    }

    public void setRetweet(Tweet retweet) {
        this.retweet = retweet;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}