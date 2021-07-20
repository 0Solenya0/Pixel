package server.middlewares;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.handler.RequestHandler;
import shared.request.Packet;

public class ClientRID extends Middleware {
    private static final Logger logger = LogManager.getLogger(ClientRID.class);
    public ClientRID(Packet req, RequestHandler p) {
        super(req, p);
    }

    @Override
    public Packet process() {
        int rid = -1;
        if (req.hasKey("m-rid"))
            rid = req.getInt("m-rid");
        Packet response = next();
        if (rid != -1)
            response.put("m-rid", rid);
        return response;
    }
}
