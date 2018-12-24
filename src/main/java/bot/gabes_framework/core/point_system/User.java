package bot.gabes_framework.core.point_system;

import java.util.List;

public class User {
    private String name;

    public static final User PCIONBOT = new User("Ez El");
    public static final User GABE = new User("Gabriel Wawerski");
    public static final User LEZE = new User("Jakub Smolak");
    public static final User HUBE = new User("Hubert Hubert");
    public static final User KASPE = new User("Kamil Kasprzak");
    public static final User WIESIO = new User("Jarek Rodak");
    public static final User JAKUBOWSKI = new User("Kamil Jakubowski");
    public static final User MELCHIOR = new User("Michał Melchior");
    public static final User MEGE = new User("Mikołaj Batyra");
    public static final User PETEK = new User("Piotr Bartoszek");

    public String name() {
        return name;
    }

    public static final List<User> USERS
            = List.of(PCIONBOT, GABE, LEZE, HUBE, KASPE, WIESIO, JAKUBOWSKI, MELCHIOR, MEGE, PETEK);

    private User(String name) {
        this.name = name;
    }
}
