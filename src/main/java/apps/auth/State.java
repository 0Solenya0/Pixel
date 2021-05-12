package apps.auth;

import model.User;
import db.Context;
import db.exception.ConnectionException;

public class State {
    private static Context context = new Context();
    private static int currentUserId = 0;

    public static User getUser() throws ConnectionException {
        if (currentUserId == 0)
            return null;
        return context.users.get(currentUserId);
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void logout() {
        currentUserId = 0;
    }

    public static void updateUser(int userId) {
        currentUserId = userId;
    }
}
