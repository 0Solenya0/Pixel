package server.controllers;

import server.db.HibernateUtil;
import shared.request.Packet;

public abstract class Controller {
    protected HibernateUtil.HibernateSession session = new HibernateUtil.HibernateSession();

    public abstract Packet respond(Packet req);

    public HibernateUtil.HibernateSession getSession() {
        return session;
    }
}
