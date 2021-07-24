package shared.exception;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidationException extends Exception {
    private HashMap<String, ArrayList<String>> errors;

    public ValidationException() {
        super();
        errors = new HashMap<>();
    }

    public boolean hasError() {
        return errors.size() > 0;
    }

    public void addError(String field, String error) {
        if (!errors.containsKey(field))
            errors.put(field, new ArrayList<>());
        errors.get(field).add(error);
    }

    public String getLog() {
        if (!hasError())
            return "validation successful";
        StringBuilder builder = new StringBuilder();
        builder.append("validation failed \n");
        errors.forEach((field, e) -> {
            e.forEach(s -> {
                builder.append(field).append(" - ").append(s).append("\n");
            });
        });
        return builder.toString();
    }

    public ArrayList<String> getErrors(String field) {
        if (errors.containsKey(field))
            return errors.get(field);
        return null;
    }

    public ArrayList<String> getAllErrors() {
        ArrayList<String> errs = new ArrayList<>();
        errors.forEach((String s, ArrayList<String> es) -> errs.addAll(es));
        return errs;
    }
}