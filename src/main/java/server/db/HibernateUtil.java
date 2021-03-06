package server.db;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.cfg.Configuration;
import shared.models.Model;

import java.time.LocalDateTime;

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
        private Session session;

        public HibernateSession() {
            session = getSession();
        }

        public void save(Model model) {
            if (model.getCreatedAt() == null)
                model.setCreatedAt(LocalDateTime.now());
            model.setLastModified(LocalDateTime.now());
            session.beginTransaction();
            session.saveOrUpdate(model);
            session.getTransaction().commit();
        }

        public void refresh(Model model) {
            if (model == null || model.getCreatedAt() == null)
                return;
            session.refresh(model);
        }

        public Session getInnerSession() {
            return session;
        }

        public void delete(Model model) {
            if (model == null)
                return;
            session.beginTransaction();
            session.delete(model);
            session.getTransaction().commit();
        }

        public Object get(Class<?> claz, int id) {
            Object obj = session.createQuery("from " + claz.getName() + " where id = :i")
                    .setParameter("i", id)
                    .uniqueResult();
            return obj;
        }

        public void reset() {
            session = getSession();
        }

        public void close() {
            session.close();
        }
    }
}
