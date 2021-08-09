package shared.models;

import com.google.gson.annotations.Expose;
import org.hibernate.Session;
import server.db.HibernateUtil;
import shared.exception.ValidationException;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
public abstract class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    public int id;

    @Expose
    private LocalDateTime createdAt, lastModified;

    private boolean checkUniqueConstraint(Field field) {
        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();
            Model m = (Model) session.createQuery(
                    String.format("from %s where %s = :v", this.getClass().getName(), field.getName()))
                    .setParameter("v", field.get(this)).uniqueResult();
            if (m != null && m.id != id)
                return false;
        }
        catch (IllegalAccessException e) {
            return false;
        }
        return true;
    }

    private boolean checkNullableConstraint(Field field) {
        try {
            return field.get(this) != null && !field.get(this).equals("");
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public void validate() throws ValidationException {
        ValidationException validationException = new ValidationException();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column != null && !column.nullable() && !checkNullableConstraint(field))
                validationException.addError(field.getName(),
                        field.getName() + " can't be empty");
            if (column != null && column.unique() && !checkUniqueConstraint(field))
                validationException.addError(field.getName(),
                        field.getName() + " is used before");
        }
        if (validationException.hasError())
            throw validationException;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return id == model.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
