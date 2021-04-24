package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewManager extends Application {

    private static Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
    }

    public void addScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }
}
