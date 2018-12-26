package bot.core.gabes_framework.core.point_system;

import java.util.List;

public class UserEntry {
    private String userName;
    private String fileName;

    public UserEntry(String userName, String fileName) {
        this.userName = userName;
        this.fileName = fileName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFileName() {
        return fileName;
    }

    public static UserEntry init(String userName, String fileName) {
        return new UserEntry(userName, fileName);
    }

    public static final UserEntry Gabe = init("Gabriel Wawerski", "gabe.txt");
    public static final UserEntry Bot = init("Ez El", "bot.txt");
    public static final UserEntry Hube = init("Hubert Hubert", "hube.txt");
    public static final UserEntry Leze = init("Jakub Smolak", "leze.txt");
    public static final UserEntry Mege = init("Mikołaj Batyra", "mege.txt");
    public static final UserEntry Jakubow = init("Kamil Jakubowski", "jakubow.txt");
    public static final UserEntry Melchior = init("Michał Melchior", "melchior.txt");
    public static final UserEntry Kaspe = init("Kamil Kasprzak", "kaspe.txt");
    public static final UserEntry Petek = init("Piotr Bartoszek", "petek.txt");
    public static final UserEntry Wiesio = init("Jarek Rodak", "wiesio.txt");

    public static List<UserEntry> getUserList() {
        return List.of(Gabe, Bot, Leze, Hube, Mege, Jakubow, Kaspe, Wiesio, Melchior, Petek);
    }
}
