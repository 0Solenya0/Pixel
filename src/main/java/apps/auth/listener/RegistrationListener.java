package apps.auth.listener;

import controller.AuthController;
import apps.auth.event.RegistrationEvent;
import db.exception.ConnectionException;
import db.exception.ValidationException;
import model.User;

public class RegistrationListener {
    private final AuthController authController = new AuthController();

    public void registrationEventOccurred(RegistrationEvent event) throws ConnectionException, ValidationException {
        User user = new User(
                event.getName(),
                event.getSurname(),
                event.getUsername(),
                event.getEmail(),
                event.getPassword()
        );
        user.setPhoto("AnonUser");
        authController.register(user);
    }
}
