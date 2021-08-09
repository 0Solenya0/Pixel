package server.config;

import server.controllers.*;
import server.controllers.auth.LoginController;
import server.controllers.auth.RegisterController;
import server.controllers.auth.ResetPasswordController;
import server.controllers.message.MessageListController;
import server.controllers.notification.NotificationListController;
import server.controllers.notification.PendingListController;
import server.controllers.profile.ProfileChangeController;
import server.controllers.profile.ProfileController;
import server.controllers.profile.ProfileDataController;
import server.controllers.tweet.TweetController;
import server.controllers.tweet.TweetGetController;
import server.controllers.tweet.TweetListController;
import server.controllers.user.ActionController;
import server.controllers.user.UserSearchController;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Routes {
    private static ArrayList<Route> routes = new ArrayList<>();

    public static void initiate() {
        routes.add(new Route("login", LoginController.class));
        routes.add(new Route("register", RegisterController.class));
        routes.add(new Route("my-profile", ProfileDataController.class));
        routes.add(new Route("update-profile", ProfileChangeController.class));
        routes.add(new Route("reset-pass", ResetPasswordController.class));
        routes.add(new Route("tweet", TweetController.class));
        routes.add(new Route("get-tweet", TweetGetController.class));
        routes.add(new Route("profile", ProfileController.class));
        routes.add(new Route("action", ActionController.class));
        routes.add(new Route("tweet-action", server.controllers.tweet.ActionController.class));
        routes.add(new Route("notification-action", server.controllers.notification.ActionController.class));
        routes.add(new Route("search-user", UserSearchController.class));
        routes.add(new Route("tweet-list-[a-z]*", TweetListController.class));
        routes.add(new Route("notification-list", NotificationListController.class));
        routes.add(new Route("pending-list", PendingListController.class));
        routes.add(new Route("message-list", MessageListController.class));
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
