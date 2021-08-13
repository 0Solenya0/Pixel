package shared.models;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tweet extends Model {

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @Expose
    private Tweet parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Tweet> comments = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @Expose
    private Tweet retweet;

    @OneToMany(mappedBy = "retweet", cascade = CascadeType.ALL)
    private List<Tweet> retweets = new ArrayList<>();

    @ManyToOne(optional = false, cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @Expose
    private User author;

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "likes_table",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> likes = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "reports_table",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> reports = new ArrayList<>();

    @Expose
    private byte[] photo;

    @Expose
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

    public List<User> getReports() {
        return reports;
    }
}
