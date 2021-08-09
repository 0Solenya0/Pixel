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
        ArrayList<Group> groups = (ArrayList<Group>) session.getInnerSession().createQuery(
                "SELECT g FROM Group AS g " +
                        "LEFT JOIN g.users as us " +
                        "WHERE us.id = :u"
        ).setParameter("u", 1).list();
        packet.putObject("list", groups);
        System.out.println(packet.getJson());
        /* session.save(user);
        User user2 = new User();
        user2.setUsername("ghader");
        user2.setPassword("123");
        user.followers.add(user2);
        session.save(user2);*/
    }
}
