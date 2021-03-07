package Client;

import Server.Requests;
import Server.models.User;

public class RequestSender {
    private static RequestSender instance;
    private String username, password;

    public static RequestSender getInstance() {
        if (instance == null)
            instance = new RequestSender();
        return instance;
    }
    public static void config(String username, String password) {
        getInstance().username = username;
        getInstance().password = password;
    }

    public String getUsername() {
        return username;
    }

    public static void login() throws Exception {
        Server.Requests.login(getInstance().username, getInstance().password);
    }
    public static void register(String username, String password, String mail, String name, String surname) throws Exception {
        if (Requests.register(username, password, mail, name, surname)) {
            getInstance().username = username;
            getInstance().password = password;
        }
    }

    public static User getProfile(String targetuser) {
        try {
            return Server.Requests.getProfile(getInstance().username, getInstance().password, targetuser);
        }
        catch (Exception e) {
            return null;
        }
    }
}
