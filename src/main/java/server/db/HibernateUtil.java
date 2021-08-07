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

    public static HibernateSession getHibernateSession() {
        return new HibernateSession();
    }

    public static class HibernateSession {
        private final Session session;

        public HibernateSession() {
            session = getSession();
        }

        public void save(Object object) {
            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();
        }

        public Object get(Class<?> claz, int id) {
            Object obj = session.createQuery("from " + claz.getName() + " where id = :i")
                    .setParameter("i", id)
                    .uniqueResult();
            return obj;
        }

        public void close() {
            session.close();
        }
    }
}
