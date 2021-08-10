package server.controllers.userlist;

import server.controllers.Controller;
import shared.models.User;
import shared.models.UserList;
import shared.request.Packet;
import shared.request.StatusCode;

public class UserListActionController extends Controller {

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
                break;
            case "delete-list":
                list = (UserList) session.get(UserList.class, req.getInt("list-id"));
                session.delete(list);
                break;
            case "add-user":
                list = (UserList) session.get(UserList.class, req.getInt("list-id"));
                target = (User) session.get(User.class, req.getInt("target-id"));
                list.getUsers().add(target);
                session.save(list);
                break;
            case "delete-user":
                list = (UserList) session.get(UserList.class, req.getInt("list-id"));
                target = (User) session.get(User.class, req.getInt("target-id"));
                list.getUsers().remove(target);
                session.save(list);
                break;
        }
        return new Packet(StatusCode.OK);
    }
}
