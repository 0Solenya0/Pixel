package validators;

import java.util.regex.Pattern;

public class UserValidators {
    public static Boolean isValidMail(String mail) {
        Pattern p = Pattern.compile("^\\w*\\@\\w*\\.\\w*$");
        return p.matcher(mail).matches();
    }
    public static Boolean isValidPhone(String phone) {
        Pattern p = Pattern.compile("^\\d*$");
        return p.matcher(phone).matches();
    }
}
