import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.cfg.Configuration;
import server.db.HibernateUtil;
import shared.exception.ValidationException;
import shared.models.Group;
import shared.models.User;
import shared.request.Packet;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setImplicitNamingStrategy(
                ImplicitNamingStrategyComponentPathImpl.INSTANCE
        );
        HibernateUtil.HibernateSession session = new HibernateUtil.HibernateSession();
        Packet packet = new Packet("");
        packet.putObject("list", session.getInnerSession().createQuery(
                "SELECT message FROM Message AS message " +
                        "JOIN message.receiverGroup as g \n" +
                        "LEFT JOIN g.users as us \n" +
                        "WHERE us.id = :u AND message.sender != :u"
        ).setParameter("u", 1).list().size());
        System.out.println(packet.getJson());
        /* session.save(user);
        User user2 = new User();
        user2.setUsername("ghader");
        user2.setPassword("123");
        user.followers.add(user2);
        session.save(user2);*/
    }
}
