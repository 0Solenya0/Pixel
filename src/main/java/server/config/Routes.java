package server.config;

import server.controllers.Controller;
import server.controllers.LoginController;
import server.controllers.ProfileDataController;
import server.controllers.RegisterController;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Routes {
    private static ArrayList<Route> routes = new ArrayList<>();

    public static void initiate() {
        routes.add(new Route("login", LoginController.class));
        routes.add(new Route("register", RegisterController.class));
        routes.add(new Route("my-profile", ProfileDataController.class));
    }

    public static ArrayList<Route> getRoutes() {
        return routes;
    }

    public static class Route {
        private final Pattern pattern;
        private final Class<? extends Controller> target;

        public Route(String regex, Class<? extends Controller> target) {
            this.pattern = Pattern.compile(regex);
            this.target = target;
        }

        public boolean isMatched(String url) {
            if (url == null)
                return false;
            return pattern.matcher(url).matches();
        }

        public Class<? extends Controller> getTarget() {
            return target;
        }
    }
}
