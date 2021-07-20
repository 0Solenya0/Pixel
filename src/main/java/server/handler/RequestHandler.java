package server.handler;

import server.config.Config;
import server.middlewares.Middleware;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.request.Packet;
import shared.request.StatusCode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RequestHandler {
    private static final Logger logger = LogManager.getLogger(RequestHandler.class);
    private int curMid;
    private final Packet req;

    public RequestHandler(Packet req) {
        this.req = req;
        curMid = -1;
    }

    public Packet next() {
        curMid++;
        if (curMid == Config.getMiddlewares().size())
            return null;
        Class<? extends Middleware> MiddlewareClass = Config.getMiddlewares().get(curMid);
        try {
            Constructor<? extends Middleware> constructor = MiddlewareClass
                    .getConstructor(Packet.class, RequestHandler.class);
            return constructor.newInstance(req, this).process();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.fatal("failed to instantiate and run server.middleware - " + e.getMessage());
            e.printStackTrace();
            return new Packet(StatusCode.NOT_FOUND);
        }
    }
}
