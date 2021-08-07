package server.middlewares;

import server.config.Routes;
import server.controllers.Controller;
import server.handler.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.request.Packet;
import shared.request.StatusCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Router extends Middleware {
    private static final Logger logger = LogManager.getLogger(Router.class);
    public Router(Packet req, RequestHandler p) {
        super(req, p);
    }

    @Override
    public Packet process() {
        for (Routes.Route route: Routes.getRoutes())
            if (route.isMatched(req.target)) {
                try {
                    Controller controller = route.getTarget().getConstructor().newInstance();
                    return controller.respond(req);
                } catch (Exception e) {
                    logger.error("Connection to database failed");
                    e.printStackTrace();
                    return new Packet(StatusCode.INTERNAL_SERVER_ERROR);
                }
            }
        // No routes matched returning null
        return next();
    }
}
