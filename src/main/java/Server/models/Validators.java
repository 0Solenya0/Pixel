package Server.models;

import java.util.regex.*;

public class Validators {
    public static Boolean isValidMail(String mail) {
        Pattern p = Pattern.compile("^\\w*\\@\\w*\\.\\w*$");
        return p.matcher(mail).matches();
    }
}
