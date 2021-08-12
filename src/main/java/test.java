import bots.calculator.store.BotStore;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.cfg.Configuration;
import org.mariuszgromada.math.mxparser.Expression;
import server.db.HibernateUtil;
import shared.exception.ValidationException;
import shared.models.Group;
import shared.models.User;
import shared.request.Packet;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.ArrayList;


public class test {
    public static void main(String[] args) throws ScriptException {
        Expression expression = new Expression("2 + 2");
        System.out.println(expression.calculate());
        /*Configuration configuration = new Configuration();
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
        System.out.println(packet.getJson());*/
        /* session.save(user);
        User user2 = new User();
        user2.setUsername("ghader");
        user2.setPassword("123");
        user.followers.add(user2);
        session.save(user2);*/
    }
}
