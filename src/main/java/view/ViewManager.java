package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManager extends Application {

    private static Stage window;
    public static Scene loginView, registrationView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        try {
            loginView =
                    new Scene(FXMLLoader.load(getClass().getResource("/apps/auth/LoginView.fxml")));
            registrationView =
                    new Scene(FXMLLoader.load(getClass().getResource("/apps/auth/RegistrationView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addScene(loginView);
    }

    public static void addScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }
}
