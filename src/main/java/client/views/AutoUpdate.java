package client.views;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AutoUpdate {
    private static ArrayList<AutoUpdate> running = new ArrayList<>();

    private Timer task = new Timer();

    public AutoUpdate() {
        running.add(this);
    }

    public void setTask(Runnable runnable, int interval) {
        stop();
        task.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(runnable);
            }
        }, interval, interval);
    }

    public void stop() {
        task.purge();
        task.cancel();
        task = new Timer();
    }

    public static ArrayList<AutoUpdate> getRunning() {
        return running;
    }
}
