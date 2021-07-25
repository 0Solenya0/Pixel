package client.views;

import client.controllers.LayoutController;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import shared.util.Config;

import java.io.IOException;
import java.util.Objects;

public class ViewManager extends Application {
    private static final Logger logger = LogManager.getLogger(ViewManager.class);
    private final static Config config = Config.getConfig("mainConfig");

    private static Stage window;
    private static LayoutController layoutController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;
        showView("LOGIN");
    }

    public static void loadPage(FXMLLoader fxmlLoader) {
        try {
            Pane pane = fxmlLoader.load();
            Platform.runLater(() -> {
                setScene(new Scene(pane));
            });
        } catch (IOException e) {
            logger.fatal("Failed to find fxml file - " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void loadLayout() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ViewManager.class.getResource(config.getProperty("DEFAULT_LAYOUT")));
        loadPage(fxmlLoader);
        layoutController = fxmlLoader.getController();
    }

    private static FXMLLoader loadFXML(String configPath) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ViewManager.class.getResource(config.getProperty(configPath)));
        return fxmlLoader;
    }

    public static void showPanel(String panelName) {
        FXMLLoader loader = loadFXML(panelName + "_PANEL");
        try {
            layoutController.getPanel().setCenter(loader.load());
        } catch (IOException e) {
            logger.fatal("Failed to find fxml file - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showView(String viewName) {
        loadPage(loadFXML(viewName + "_VIEW"));
    }

    public static void setScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }

    public static Stage getWindow() {
        return window;
    }

    public static void connectionError() {
        // TO DO
    }
}