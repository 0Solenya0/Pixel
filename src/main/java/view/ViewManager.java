package view;

import util.Config;
import controller.MainPanelController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewManager extends Application {
    private final Config authConfig = Config.getConfig("AUTH_APP_CONFIG");
    private final Config config = Config.getConfig("mainConfig");

    private static Stage window;
    public static Scene loginView, registrationView, mainView;
    public static MainPanelController mainPanelController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        try {
            loginView =
                    new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(authConfig.getProperty("LOGIN_VIEW")))));
            registrationView =
                    new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(authConfig.getProperty("REGISTRATION_VIEW")))));
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Objects.requireNonNull(getClass().getResource(config.getProperty("MAIN_PANEL"))));
            Pane pane = fxmlLoader.load();
            mainView = new Scene(pane);
            mainPanelController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setScene(loginView);
    }

    public static void setScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }

    public static void changeCenter(Pane pane) {
        mainPanelController.changeCenterPane(pane);
    }

    public static void connectionFailed() {

    }
}
