package client.store;

import client.request.SocketHandler;
import com.google.gson.reflect.TypeToken;
import net.bytebuddy.description.method.MethodDescription;
import server.db.models.fields.AccessField;
import server.db.models.fields.AccessLevel;
import shared.request.Packet;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public void updateProfileData() {
        Packet packet = new Packet("my-profile");
        Packet res = SocketHandler.getSocketHandlerWithoutException().sendPacketAndGetResponse(packet);
        username = res.get("username");
        name = res.get("name");
        surname = res.get("surname");
        bio = res.get("bio");
        Type type = new TypeToken<AccessField<String>>(){}.getType();
        Type dateType = new TypeToken<AccessField<LocalDate>>(){}.getType();
        Type dateTimeType = new TypeToken<AccessField<LocalDateTime>>(){}.getType();
        Type accessLevelType = new TypeToken<AccessLevel>(){}.getType();
        AccessField<String> phone = res.getObject("phone", type);
        AccessField<String> mail = res.getObject("mail", type);
        AccessField<LocalDate> birthday = res.getObject("birthday", dateType);
        AccessField<LocalDateTime> lastSeen = res.getObject("last-seen", dateTimeType);
        phoneNumber = phone.get();
        this.birthday = birthday.get();
        mailPrivacy = mail.getAccessLevel();
        lastSeenPrivacy = lastSeen.getAccessLevel();
        visibilityPrivacy = res.getObject("visibility", accessLevelType);
        phonePrivacy = phone.getAccessLevel();
        birthdayPrivacy = birthday.getAccessLevel();
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
