package server.models;

import javax.persistence.Column;
import java.lang.reflect.Field;

public abstract class Model {

    public boolean validate() {
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    if (column.unique()) {

                    }
                }
            }
        }
        catch (IllegalAccessException ignored) {

        }
        return true;
    }
}
