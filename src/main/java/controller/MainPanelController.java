package controller;


import apps.auth.State;
import apps.tweet.controller.TweetController;
import apps.tweet.controller.TweetListController;
import apps.tweet.model.Tweet;
import com.jfoenix.controls.JFXButton;
import config.Config;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPanelController extends Controller implements Initializable {
    private Config config = Config.getConfig("TWEET_APP_CONFIG");

    @FXML
    private JFXButton btnHome, btnPostTweet;

    @FXML
    private BorderPane borderPane;

    public void changeCenterPane(Pane pane) {
        borderPane.setCenter(pane);
    }

    public void btnPostTweetClicked() throws IOException, ConnectionException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(config.getProperty("TWEET_LIST_VIEW"))));
        Pane pane = fxmlLoader.load();
        TweetListController tweetListController = fxmlLoader.getController();
        tweetListController.addTweetList(
                context.tweets.getAll(
                        context.tweets.getQueryBuilder()
                                .getByAuthorId(Objects.requireNonNull(State.getUser()).id)
                                .getQuery()
                )
        );
        ViewManager.changeCenter(pane);
        //Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(config.getProperty("TWEET_INPUT_VIEW"))));
        //ViewManager.changeCenter(pane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
