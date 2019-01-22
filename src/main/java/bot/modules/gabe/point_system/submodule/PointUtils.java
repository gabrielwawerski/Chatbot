package bot.modules.gabe.point_system.submodule;

import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;

public final class PointUtils {
    private static final DBConnection db = DBConnection.getInstance();
    private static ArrayList<User> users = db.getUsers();

    public static User getUser(Message message) {
        String sender = message.getSender().getName();

        for (User user : users) {
            if (user.getName().equals(sender)) {
                return user;
            }
        }
        System.out.println("user not found");
        return User.INVALID_USER;
    }

    public static ArrayList<User> usersFromDatabase() {
        return users;
    }

    public static String format(int points) {
        return "(" + points + ")";
    }
}
