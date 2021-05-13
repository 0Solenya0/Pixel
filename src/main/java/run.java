import apps.auth.State;
import db.Context;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.User;
import view.ViewManager;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class run {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Context context = new Context();
                User user = null;
                try {
                    user = State.getUser();
                    if (user != null) {
                        user.getLastseen().set(LocalDateTime.now());
                        context.users.save(user);
                    }
                } catch (ConnectionException | ValidationException e) {
                    e.printStackTrace();
                }
            }
        }, 10000, 10000);
        ViewManager.main(args);
    }
}
