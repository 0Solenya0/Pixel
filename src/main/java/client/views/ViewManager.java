package client.views;

import client.controllers.LayoutController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import shared.util.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewManager extends Application {
    private static final Logger logger = LogManager.getLogger(ViewManager.class);
    private final static Config config = Config.getConfig("mainConfig");

    private static Stage window;
    private static LayoutController layoutController;
    private static ArrayList<Node> panels = new ArrayList<>();

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

    public static <T> Component<T> getComponent(String component) {
        FXMLLoader fxmlLoader = getComponentFXML(component);
        return new Component<>(fxmlLoader);
    }

    public static FXMLLoader getComponentFXML(String component) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ViewManager.class.getResource(config.getProperty(component + "_COMP")));
        return fxmlLoader;
    }

    public static <T> T showPanel(String panelName) {
        FXMLLoader loader = loadFXML(panelName + "_PANEL");
        try {
            Node pane = loader.load();
            panels.add(pane);
            removeOldPanels();
            layoutController.getPanel().setCenter(pane);
            return loader.getController();
        } catch (IOException e) {
            logger.fatal("Failed to find fxml file - " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T showPanel(FXMLLoader fxmlLoader) {
        try {
            Node pane = fxmlLoader.load();
            panels.add(pane);
            removeOldPanels();
            layoutController.getPanel().setCenter(pane);
            return fxmlLoader.getController();
        } catch (IOException e) {
            logger.fatal("Failed to find fxml file - " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void removeOldPanels() {
        while (panels.size() > config.getProperty(Integer.class, "PANEL_BACK_HISTORY"))
            panels.remove(0);
        while (AutoUpdate.getRunning().size() > config.getProperty(Integer.class, "PANEL_BACK_HISTORY"))
            AutoUpdate.getRunning().remove(0).stop();
        layoutController.getBtnBack().setVisible(panels.size() > 1);
    }

    public static void showView(String viewName) {
        loadPage(loadFXML(viewName + "_VIEW"));
    }

    public static void setScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }

    public static File showFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        return fileChooser.showOpenDialog(window);
    }

    public static void addStackPaneLayer(Node node) {
        Button btnClose = layoutController.getBtnClose();
        StackPane stackPane = layoutController.getStackPane();
        btnClose.setVisible(true);
        stackPane.getChildren().add(stackPane.getChildren().size() - 1, node);
        EventHandler<ActionEvent> handler = btnClose.getOnAction();
        btnClose.setOnAction(e -> {
            stackPane.getChildren().remove(node);
            if (stackPane.getChildren().size() == 2)
                btnClose.setVisible(false);
            if (handler != null)
                btnClose.setOnAction(handler);
        });
    }

    public static Stage getWindow() {
        return window;
    }

    public static void connectionError() {
        // TO DO
    }

    public static void previousPanel() {
        if (panels.size() <= 1)
            return;
        panels.remove(panels.size() - 1);
        layoutController.getPanel().setCenter(panels.get(panels.size() - 1));
        layoutController.getBtnBack().setVisible(panels.size() > 1);
    }

    public static class Component<T> {
        private Pane pane;
        private T controller;

        public Component(FXMLLoader loader) {
            try {
                pane = loader.load();
                controller = loader.getController();
            } catch (IOException e) {
                logger.error("Failed to load fxml file component");
                e.printStackTrace();
            }
        }

        public Pane getPane() {
            return pane;
        }

        public T getController() {
            return controller;
        }
    }
}