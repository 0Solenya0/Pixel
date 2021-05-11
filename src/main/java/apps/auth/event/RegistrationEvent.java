package apps.auth.event;

import event.Event;

public class RegistrationEvent extends Event {
    String name, surname, password, username, email;

    public RegistrationEvent(String name, String surname, String password, String username, String email) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.username = username;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
