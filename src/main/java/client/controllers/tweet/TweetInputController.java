package client.controllers.tweet;

import client.request.SocketHandler;
import client.views.InfoDialog;
import client.views.ViewManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import shared.exception.ValidationException;
import shared.request.Packet;
import shared.util.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TweetInputController {

    private Config config = Config.getLanguageConfig();

    @FXML
    private JFXTextArea txtTweet;

    @FXML
    private Label lblInputTweet, lblGlobalErr;

    @FXML
    private JFXButton btnTweet;

    private boolean retweet;
    private int parent;
    private byte[] photo;

    private Runnable onSendListener;

    public void setOnSendListener(Runnable onSendListener) {
        this.onSendListener = onSendListener;
    }

    @FXML
    void attachPhoto(ActionEvent event) {
        File photo = ViewManager.showFileDialog();
        try {
            this.photo = Files.readAllBytes(photo.toPath());
        } catch (IOException e) {
            InfoDialog.showFailed(config.getProperty("IMAGE_LOAD_FAILED"));
        }
    }

    @FXML
    void tweet(ActionEvent event) {
        Packet packet = new Packet("tweet");
        packet.put("parent", parent);
        packet.put("retweet", retweet);
        packet.putObject("photo", photo);
        packet.put("content", txtTweet.getText());
        Packet response = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        switch (response.getStatus()) {
            case CREATED:
                if (onSendListener != null)
                    onSendListener.run();
                InfoDialog.showSuccess(config.getProperty("TWEET_SUCCESS_DIALOG"));
                break;
            case BAD_REQUEST:
                lblGlobalErr.setText(response.getObject("error", ValidationException.class).getAllErrors().get(0));
                break;
            case INTERNAL_SERVER_ERROR:
            case BAD_GATEWAY:
                InfoDialog.showFailed(config.getProperty("TWEET_FAILED_DIALOG"));
                break;
        }
    }

    public void setRetweet(boolean retweet) {
        this.retweet = retweet;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}
