package client.controllers.components;

import client.request.SocketHandler;
import client.views.ViewManager;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import shared.models.Tweet;
import shared.models.User;
import shared.request.Packet;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TweetListController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox containerPane;

    @FXML
    private Label lblMessage;

    public void setMessage(String message) {
        lblMessage.setText(message);
    }

    public void setTweetList(ArrayList<Tweet> tweets) {
        containerPane.getChildren().clear();

        lblMessage.setVisible(tweets.isEmpty());

        for (Tweet tweet: tweets) {
            ViewManager.Component<TweetCardController> tweetCard = ViewManager.getComponent("TWEET_CARD");
            containerPane.getChildren().add(tweetCard.getPane());
            tweetCard.getController().setTweetId(tweet.id);
        }
    }

    public void showExplorerTweets() {
        Packet packet = new Packet("tweet-list-explorer");
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        Type listType = new TypeToken<ArrayList<Tweet>>(){}.getType();
        ArrayList<Tweet> tweets = res.getObject("tweets", listType);
        setTweetList(tweets);
    }

    public void showUserTweets(int userId) {
        Packet packet = new Packet("tweet-list-user");
        packet.put("target", userId);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        Type listType = new TypeToken<ArrayList<Tweet>>(){}.getType();
        ArrayList<Tweet> tweets = res.getObject("tweets", listType);
        setTweetList(tweets);
    }

    public void showComments(int tweetId) {
        Packet packet = new Packet("tweet-list-comment");
        packet.put("tweet-id", tweetId);
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        Type listType = new TypeToken<ArrayList<Tweet>>(){}.getType();
        ArrayList<Tweet> tweets = res.getObject("tweets", listType);
        setTweetList(tweets);
        lblMessage.setVisible(false);

        ViewManager.Component<TweetInputController> tweetInput = ViewManager.getComponent("TWEET_INPUT");
        containerPane.getChildren().add(0, tweetInput.getPane());
        tweetInput.getController().setParent(tweetId);

        ViewManager.Component<TweetCardController> tweetCard = ViewManager.getComponent("TWEET_CARD");
        containerPane.getChildren().add(0, tweetCard.getPane());
        tweetCard.getController().setTweetId(tweetId);
    }

    public void setHeight(int height) {
        scrollPane.setPrefHeight(height);
    }
}
