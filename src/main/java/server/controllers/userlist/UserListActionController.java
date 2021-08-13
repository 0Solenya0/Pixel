package server.controllers.userlist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.controllers.Controller;
import server.controllers.user.ActionController;
import shared.models.User;
import shared.models.UserList;
import shared.request.Packet;
import shared.request.StatusCode;

public class UserListActionController extends Controller {
    private static final Logger logger = LogManager.getLogger(UserListActionController.class);

    public Packet respond(Packet req) {
        User user = (User) session.get(User.class, req.getInt("user-id"));
        UserList list;
        User target;
        switch (req.get("type")) {
            case "create-list":
                list = new UserList();
                list.setName(req.get("name"));
                list.setOwner(user);
                session.save(list);
                logger.info("new user list " + list.id + " was created");
                break;
            case "delete-list":
                list = (UserList) session.get(UserList.class, req.getInt("list-id"));
                session.delete(list);
                logger.info("user list " + list.id + " was deleted");
                break;
            case "add-user":
                list = (UserList) session.get(UserList.class, req.getInt("list-id"));
                target = (User) session.get(User.class, req.getInt("target-id"));
                list.getUsers().add(target);
                session.save(list);
                logger.info("new user " + user.getUsername() + " added to user list " + list.id);
                break;
            case "delete-user":
                list = (UserList) session.get(UserList.class, req.getInt("list-id"));
                target = (User) session.get(User.class, req.getInt("target-id"));
                list.getUsers().remove(target);
                session.save(list);
                logger.info("user " + user.getUsername() + " deleted from user list " + list.id);
                break;
        }
        session.close();
        return new Packet(StatusCode.OK);
    }
}
