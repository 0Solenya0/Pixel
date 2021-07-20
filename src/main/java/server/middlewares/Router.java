package server.middlewares;

import server.config.Routes;
import server.db.exception.ConnectionException;
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
                    Method method = route.getTarget().getMethod("respond", Packet.class);
                    return (Packet) method.invoke(null, req);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    logger.fatal("view doesn't have the intended methods - "
                            + req.target + " - "
                            + e.getMessage());
                    e.printStackTrace();
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
