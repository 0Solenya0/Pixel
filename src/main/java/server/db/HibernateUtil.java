package server.db;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static Configuration configuration = new Configuration();
    private static SessionFactory sessionFactory;

    static {
        configuration.setImplicitNamingStrategy(
                ImplicitNamingStrategyComponentPathImpl.INSTANCE
        );
        sessionFactory = configuration.configure().buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void save(Object object) {
        Session session = getSession();
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.getTransaction().commit();;
        session.close();
    }

    public static Object get(Class<?> claz, int id) {
        Session session = getSession();
        Object obj = session.createQuery("from " + claz.getName() + " where id = :i")
                .setParameter("i", id)
                .uniqueResult();
        return obj;
    }
}
