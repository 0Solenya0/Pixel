package server.middlewares;

import server.handler.RequestHandler;
import server.handler.SocketHandler;
import shared.request.Packet;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

public class Auth extends Middleware {
    private static final ConcurrentHashMap<String, Integer> authTokens = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Integer> currentUsers = new ConcurrentHashMap<>();

    public static String registerUser(int userId, int hId) {
        SecureRandom secureRandom = new SecureRandom();
        String tokenId = String.valueOf(secureRandom.nextLong());
        authTokens.put(tokenId, userId);
        currentUsers.put(hId, userId);
        SocketHandler.getSocketHandler(hId).addDisconnectListener(() -> {
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
