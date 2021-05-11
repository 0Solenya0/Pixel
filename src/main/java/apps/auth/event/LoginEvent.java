package apps.auth.event;

import event.Event;

public class LoginEvent extends Event {
    private String username;
    private String password;

    public LoginEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
