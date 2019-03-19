package bot.core.gabes_framework.core.database;

import java.util.List;

/**
 * Dummy objects, representing only the full name of each group member. TODO should also have a list of users nicknames
 */
public class Users {
    private String name;

    private Users(String name) {
        this.name = name;
    }

    private static Users init(String userName) {
        return new Users(userName);
    }

    public String name() {
        return name;
    }

    public static final Users Gabe = init("Gabriel Wawerski");
    public static final Users Bot = init("Ez El");
    public static final Users Hube = init("Hubert Hubert");
    public static final Users Leze = init("Jakub Smolak");
    public static final Users Mege = init("Mikołaj Batyra");
    public static final Users Jakubow = init("Kamil Jakubowski");
    public static final Users Melchior = init("Michał Melchior");
    public static final Users Kaspe = init("Kamil Kasprzak");
    public static final Users Petek = init("Piotr Bartoszek");
    public static final Users Wiesio = init("Jarek Rodak");

    public static List<Users> getUserList() {
        return List.of(Gabe, Bot, Leze, Hube, Mege, Jakubow, Kaspe, Wiesio, Melchior, Petek);
//        return List.of(Users.class.getDeclaredFields()); // todo
    }
}
