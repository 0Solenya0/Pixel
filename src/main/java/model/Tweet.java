package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeSet;

public class Tweet extends Model {
    private static final Logger logger = LogManager.getLogger(Tweet.class);

    private int parentTweet, reTweet;
    private TreeSet<Integer> likes = new TreeSet<>();
    private int author;
    private String content;
    private TreeSet<Integer> reports = new TreeSet<>();

    public Tweet(User author, String text) {
        super();
        this.author = author.id;
        this.content = text;
    }

    public int getParentTweet() {
        return parentTweet;
    }

    public void setParentTweet(int parentTweet) {
        this.parentTweet = parentTweet;
    }

    public int getReTweet() {
        return reTweet;
    }

    public void setReTweet(int reTweet) {
        this.reTweet = reTweet;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void addLike(User user) {
        likes.add(user.id);
    }

    public void removeLike(User user) {
        likes.remove(user.id);
    }

    public boolean containsLike(User user) {
        return likes.contains(user.id);
    }

    public TreeSet<Integer> getLikes() {
        return likes;
    }

    public TreeSet<Integer> getReports() {
        return reports;
    }

    public void addReport(User user) {
        reports.add(user.id);
    }
}
