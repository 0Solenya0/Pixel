package apps.notification.controller;

import apps.auth.State;
import apps.notification.model.Notification;
import apps.tweet.controller.TweetController;
import com.jfoenix.controls.JFXListView;
import config.Config;
import controller.Controller;
import db.exception.ConnectionException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.ViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class NotificationController extends Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(NotificationController.class);
    private Config languageConfig = Config.getLanguageConfig();
    private Config notificationAppConfig = Config.getConfig("NOTIFICATION_APP_CONFIG");

    @FXML
    private Label lblPageTitle, lblPendingRequests;

    @FXML
    private VBox vboxContainer;

    @FXML
    private JFXListView<String> listPendingRequests;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblPageTitle.setText(languageConfig.getProperty("NOTIFICATION_PAGE_TITLE"));
        lblPendingRequests.setText(languageConfig.getProperty("PENDING_REQUEST_TITLE"));
        try {
            ArrayList<Notification> notifications = context.notifications.getAll(
                    context.notifications.getQueryBuilder()
                    .getByUser2(State.getUser()).getEnabled().getQuery()
            );
            Collections.reverse(notifications);
            for (Notification notification: notifications) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(notificationAppConfig.getProperty("NOTIFICATION_CARD_VIEW")));
                Pane pane = fxmlLoader.load();
                NotificationCardController controller = fxmlLoader.getController();
                controller.setNotification(notification);
                vboxContainer.getChildren().add(pane);
            }
        } catch (ConnectionException e) {
            ViewManager.connectionFailed();
        } catch (IOException e) {
            logger.error("failed to load view fxml file");
            e.printStackTrace();
        }
        //TO DO PENDING REQUESTS
        listPendingRequests.getItems().add("Hi");
    }
}