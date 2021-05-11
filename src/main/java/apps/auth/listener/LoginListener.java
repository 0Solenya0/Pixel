package apps.auth.listener;

import controller.AuthController;
import apps.auth.event.LoginEvent;
import db.exception.ConnectionException;

public class LoginListener {
    private final AuthController authController = new AuthController();

    public int loginEventOccurred(LoginEvent event) throws ConnectionException {
        return authController.checkLogin(event.getUsername(), event.getPassword());
    }
}
