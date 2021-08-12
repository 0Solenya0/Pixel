package client.views;

import client.controllers.MessageController;
import client.controllers.tweet.TweetCardController;
import client.request.SocketHandler;
import client.store.MyProfileStore;
import javafx.application.Platform;
import javafx.scene.control.Hyperlink;
import shared.models.Group;
import shared.models.Tweet;
import shared.models.User;
import shared.request.Packet;
import shared.request.StatusCode;
import shared.util.Config;

public class HyperLinkTranslator {

    private static Config config = Config.getLanguageConfig();

    public static Hyperlink getHyperLinkForGroup(String tag) {
        Hyperlink hyperlink = new Hyperlink(tag);
        Thread thread = new Thread(() -> {
            int id = Integer.parseInt(tag);
            Packet packet = new Packet("get-group");
            packet.put("group-id", id);
            Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
            if (res.getStatus().equals(StatusCode.OK)) {
                Group group = res.getObject("group", Group.class);
                Platform.runLater(() -> {
                    hyperlink.setText(group.getName());
                    hyperlink.setOnAction((action) -> {
                        MessageController controller = ViewManager.showPanel("MESSAGE");
                        assert controller != null;
                        controller.setGroupTarget(group);
                    });
                });
            }
        });
        thread.start();
        return hyperlink;
    }

    public static Hyperlink getHyperLinkForUser(String tag) {
        Hyperlink hyperlink = new Hyperlink(tag);
        Thread thread = new Thread(() -> {
            Packet packet = new Packet("profile");
            packet.put("username", tag);
            Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
            if (res.getStatus().equals(StatusCode.OK)) {
                User user = res.getObject("user", User.class);
                Platform.runLater(() -> {
                    hyperlink.setText(user.getUsername());
                    hyperlink.setOnAction((action) -> {
                        MessageController controller = ViewManager.showPanel("MESSAGE");
                        assert controller != null;
                        controller.setUserTarget(user);
                    });
                });
            }
        });
        thread.start();
        return hyperlink;
    }

    public static Hyperlink getHyperLinkForInvitation(String tag) {
        Hyperlink hyperlink = new Hyperlink(tag);
        Thread thread = new Thread(() -> {
            int id = Integer.parseInt(tag);
            Packet packet = new Packet("get-group");
            packet.put("group-id", id);
            Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
            if (res.getStatus().equals(StatusCode.OK)) {
                Group group = res.getObject("group", Group.class);
                Platform.runLater(() -> {
                    hyperlink.setText(group.getName());
                    hyperlink.setOnAction((action) -> {
                        if (ConfirmationDialog.show("Join " + group.getName() + "?")) {
                            Packet p = new Packet("group-action");
                            p.put("type", "add-user");
                            p.put("group-id", id);
                            p.put("target-id", MyProfileStore.getInstance().getUser().id);
                            Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(p);
                            if (response.getStatus() == StatusCode.OK) {
                                MessageController controller = ViewManager.showPanel("MESSAGE");
                                assert controller != null;
                                controller.setGroupTarget(group);
                            }
                        }
                    });
                });
            }
        });
        thread.start();
        return hyperlink;
    }

    public static Hyperlink getHyperLinkForTweet(String tag) {
        Hyperlink hyperlink = new Hyperlink("Tweet");
        Thread thread = new Thread(() -> {
            int id = Integer.parseInt(tag);
            Packet packet = new Packet("get-tweet");
            packet.put("id", id);
            Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
            Platform.runLater(() -> {
                if (res.getStatus() != StatusCode.OK)
                    hyperlink.setOnAction((action) -> InfoDialog.showFailed(config.getProperty("TWEET_ACCESS_FAILED")));
                else {
                    Tweet tweet = res.getObject("tweet", Tweet.class);
                    hyperlink.setText("Tweet from " + tweet.getAuthor().getUsername());
                    hyperlink.setOnAction((action) -> {
                        ViewManager.Component<TweetCardController> component = ViewManager.getComponent("TWEET_CARD");
                        component.getController().setTweetId(id);
                        ViewManager.addStackPaneLayer(component.getPane());
                    });
                }
            });
        });
        thread.start();
        return hyperlink;
    }

    public static Hyperlink getHyperLink(String tag) {
        if (tag.startsWith("group:"))
            return getHyperLinkForGroup(tag.substring(6));
        else if (tag.startsWith("user:"))
            return getHyperLinkForUser(tag.substring(5));
        else if (tag.startsWith("invite:"))
            return getHyperLinkForInvitation(tag.substring(7));
        else if (tag.startsWith("tweet:"))
            return getHyperLinkForTweet(tag.substring(6));
        return new Hyperlink();
    }
}
