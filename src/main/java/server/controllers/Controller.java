package server.controllers;

import server.db.HibernateUtil;

public abstract class Controller {
    protected HibernateUtil.HibernateSession session = new HibernateUtil.HibernateSession();
}
