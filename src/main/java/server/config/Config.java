package server.config;

import server.middlewares.*;

import java.util.ArrayList;

public class Config {
    private static ArrayList<Class<? extends Middleware>> middlewares = new ArrayList<>();

    public static void initiate() {
        //Initiate app configs
        Routes.initiate();
        //Initiate middlewares
        middlewares.add(ClientRID.class);
        middlewares.add(Auth.class);
        middlewares.add(ServerRID.class);
        middlewares.add(Router.class);
    }

    public static ArrayList<Class<? extends Middleware>> getMiddlewares() {
        return middlewares;
    }
}
