package bot.core.gabes_framework.core.point_system;

import java.util.List;

public class Users {
    public static final Users BOT = new Users("Ez El");
    public static final Users GABE = new Users("Gabriel Wawerski");
    public static final Users LEZE = new Users("Jakub Smolak");
    public static final Users HUBE = new Users("Hubert Hubert");
    public static final Users KASPE = new Users("Kamil Kasprzak");
    public static final Users WIESIO = new Users("Jarek Rodak");
    public static final Users JAKUBOW = new Users("Kamil Jakubowski");
    public static final Users MELCHIOR = new Users("Michał Melchior");
    public static final Users MEGE = new Users("Mikołaj Batyra");
    public static final Users PETEK = new Users("Piotr Bartoszek");

    private String name;

    private Users(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
