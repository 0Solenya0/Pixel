package server.middlewares;

import server.handler.RequestHandler;
import shared.request.Packet;

public abstract class Middleware {
    protected Packet req;
    private RequestHandler handler;

    public Middleware(Packet req, RequestHandler p) {
        this.req = req;
        this.handler = p;
    }

    protected Packet next() {
        return handler.next();
    }

    public abstract Packet process();

}
