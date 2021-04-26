package apps.tweet.controller;

import apps.tweet.model.Tweet;

public class TweetController {
    private Tweet tweet;



    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
        updateCard();
    }

    public void updateCard() {

    }
}
