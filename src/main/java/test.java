import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.cfg.Configuration;
import shared.exception.ValidationException;
import shared.models.User;
import shared.request.Packet;

public class test {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setImplicitNamingStrategy(
                ImplicitNamingStrategyComponentPathImpl.INSTANCE
        );
        SessionFactory sessionFactory = configuration.configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = new User();
        user.setUsername("mani");
        user.setPassword("123");
        Packet p = new Packet("");
        p.putObject("user", user);
        /* session.save(user);
        User user2 = new User();
        user2.setUsername("ghader");
        user2.setPassword("123");
        user.followers.add(user2);
        session.save(user2);*/
        session.getTransaction().commit();
    }
}
