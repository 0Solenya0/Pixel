package server.controllers;

import server.db.HibernateUtil;

public abstract class Controller {
    HibernateUtil.HibernateSession session = new HibernateUtil.HibernateSession();
}
