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

    public void showExplorerTweets() {
        Packet packet = new Packet("tweet-list-explorer");
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        containerPane.getChildren().clear();
        Type listType = new TypeToken<ArrayList<Tweet>>(){}.getType();
        ArrayList<Tweet> tweets = res.getObject("tweets", listType);

        lblMessage.setVisible(tweets.isEmpty());

        for (Tweet tweet: tweets) {
            ViewManager.Component<TweetCardController> tweetCard = ViewManager.getComponent("TWEET_CARD");
            containerPane.getChildren().add(tweetCard.getPane());
            tweetCard.getController().setTweetId(tweet.id);
        }
    }
}
