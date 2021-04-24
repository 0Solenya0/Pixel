package view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
        addScene(new Scene(root));
    }

    public void addScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }
}
