package client.controllers.components;

import client.request.SocketHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import shared.request.Packet;

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
    }
}
