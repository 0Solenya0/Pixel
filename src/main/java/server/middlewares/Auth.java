package server.middlewares;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import server.db.HibernateUtil;
import server.handler.RequestHandler;
import server.handler.SocketHandler;
import shared.models.User;
import shared.request.Packet;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class Auth extends Middleware {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final ConcurrentHashMap<String, Integer> authTokens = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Integer> currentUsers = new ConcurrentHashMap<>();

    public static String registerUser(int userId, int hId) {
        SecureRandom secureRandom = new SecureRandom();
        String tokenId = String.valueOf(secureRandom.nextLong());
        authTokens.put(tokenId, userId);
        currentUsers.put(hId, userId);
        logger.info("user with userid " + userId + " has been registered with auth token " + tokenId);
        SocketHandler.getSocketHandler(hId).addDisconnectListener(() -> {
            HibernateUtil.HibernateSession session = new HibernateUtil.HibernateSession();
            User user = (User) session.get(User.class, userId);
            user.getLastSeen().set(LocalDateTime.now());
            session.save(user);
            removeSocket(hId);
        });
        return tokenId;
    }

    public static boolean isUserOnline(int userId) {
        return currentUsers.containsValue(userId);
    }

    private static void removeSocket(int hId) {
        currentUsers.remove(hId);
    }

    public Auth(Packet req, RequestHandler p) {
        super(req, p);
    }

    @Override
    public Packet process() {
        if (req.hasKey("auth-token")) {
            String token = req.get("auth-token", null);
            if (authTokens.containsKey(token))
                req.put("user-id", String.valueOf(authTokens.get(token)));
        }
        return next();
    }
}
