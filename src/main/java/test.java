import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.cfg.Configuration;
import server.db.HibernateUtil;
import shared.exception.ValidationException;
import shared.models.User;
import shared.request.Packet;

public class test {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setImplicitNamingStrategy(
                ImplicitNamingStrategyComponentPathImpl.INSTANCE
        );
        HibernateUtil.HibernateSession session = new HibernateUtil.HibernateSession();
        System.out.println(session.getInnerSession().createQuery(
                "SELECT message FROM Message AS message " +
                        "WHERE message.receiver.id = 1 OR message.sender.id = 1"
        ).list());
        /* session.save(user);
        User user2 = new User();
        user2.setUsername("ghader");
        user2.setPassword("123");
        user.followers.add(user2);
        session.save(user2);*/
    }
}
