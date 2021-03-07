package Client;

import Server.Requests;

public class RequestSender {
    private static RequestSender instance;
    String username, password;

    public static RequestSender getInstance() {
        if (instance == null)
            instance = new RequestSender();
        return instance;
    }
    public static void config(String username, String password) {
        getInstance().username = username;
        getInstance().password = password;
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
}
