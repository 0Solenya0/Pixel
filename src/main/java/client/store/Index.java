package client.store;

import server.db.models.fields.AccessLevel;

import java.time.LocalDate;

public class Index extends Store {
    private static Index instance;

    private String authToken;
    private String username;
    private String name, surname;
    private String phoneNumber;
    private String bio;
    private LocalDate birthday;

    private AccessLevel mailPrivacy, lastSeenPrivacy,
            visibilityPrivacy, phonePrivacy, birthdayPrivacy;

    public static Index getInstance() {
        if (instance == null)
            instance = new Index();
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getBio() {
        return bio;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public AccessLevel getMailPrivacy() {
        return mailPrivacy;
    }

    public AccessLevel getLastSeenPrivacy() {
        return lastSeenPrivacy;
    }

    public AccessLevel getVisibilityPrivacy() {
        return visibilityPrivacy;
    }

    public AccessLevel getPhonePrivacy() {
        return phonePrivacy;
    }

    public AccessLevel getBirthdayPrivacy() {
        return birthdayPrivacy;
    }
}
