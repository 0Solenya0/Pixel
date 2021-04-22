import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hi");

        Button button = new Button();
        Button button1 = new Button();
        button.setText("Mani");
        button1.setText("Sup");
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(button);
        borderPane.setLeft(button1);

        Scene scene = new Scene(borderPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
