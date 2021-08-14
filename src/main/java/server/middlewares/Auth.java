package server.middlewares;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.db.HibernateUtil;
import server.handler.BotHandler;
import server.handler.RequestHandler;
import server.handler.SocketHandler;
import shared.models.User;
import shared.request.Packet;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Auth extends Middleware {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final ConcurrentHashMap<String, Integer> authTokens = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Integer> currentUsers = new ConcurrentHashMap<>();

    public static String registerUser(int userId, int hId) {
        SecureRandom secureRandom = new SecureRandom();
        // String tokenId = String.valueOf(secureRandom.nextLong());
        String tokenId = String.valueOf((userId + 1000) * 7);
        authTokens.put(tokenId, userId);
        currentUsers.put(hId, userId);
        logger.info("user with userid " + userId + " has been registered with auth token " + tokenId);
        if (hId > 0)
            SocketHandler.getSocketHandler(hId).addDisconnectListener(() -> disconnectListener(userId, hId));
        return tokenId;
    }

    private static void disconnectListener(int userId, int hId) {
        HibernateUtil.HibernateSession session = new HibernateUtil.HibernateSession();
        User user = (User) session.get(User.class, userId);
        user.getLastSeen().set(LocalDateTime.now());
        session.save(user);
        removeHandler(hId);
    }

    public static ArrayList<Integer> getUserHandlerIds(int userId) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int hId: currentUsers.keySet())
            if (currentUsers.get(hId) == userId)
                res.add(hId);
        return res;
    }

    public static boolean isUserOnline(int userId) {
        return currentUsers.containsValue(userId);
    }

    private static void removeHandler(int hId) {
        currentUsers.remove(hId);
    }

    public Auth(Packet req, RequestHandler p) {
        super(req, p);
    }

    @Override
    public Packet process() {
        if (req.hasKey("auth-token")) {
            String token = req.get("auth-token");
            int userId = Integer.parseInt(token) / 7 - 1000;
            if (!authTokens.containsKey(token))
                registerUser(userId, req.getInt("handler"));
            req.put("user-id", String.valueOf(authTokens.get(token)));
        }
        return next();
    }
}
