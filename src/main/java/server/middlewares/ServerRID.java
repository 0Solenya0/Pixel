package server.middlewares;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack;
import server.handler.RequestHandler;
import server.request.ridListener;
import shared.request.Packet;
import shared.request.StatusCode;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerRID extends Middleware {
    private static final ConcurrentHashMap<Integer, ridListener> ridListeners = new ConcurrentHashMap<>();
    private static final AtomicInteger lastRid = new AtomicInteger();

    public static int registerListener(ridListener listener) {
        int rid = lastRid.addAndGet(1);
        ridListeners.put(rid, listener);
        return rid;
    }

    public ServerRID(Packet req, RequestHandler p) {
        super(req, p);
    }

    @Override
    public Packet process() {
        Packet response = null;
        int rid = -1;
        if (req.hasKey("m-rid"))
            rid = req.getInt("m-rid");

        if (req.hasKey("rid") && !ridListeners.containsKey(req.getInt("rid")))
            response = new Packet(StatusCode.NOT_FOUND);
        else if (req.hasKey("rid"))
            response = ridListeners.get(req.getInt("rid")).listenPacket(req);
        else
            response = next();

        if (rid != -1)
            response.put("m-rid", rid);
        return response;
    }
}
