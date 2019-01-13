package bot.core.gabes_framework.core.database;

import java.util.List;

public class Users {
    private String userName;
    private String fileName;

    public Users(String userName, String fileName) {
        this.userName = userName;
        this.fileName = fileName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFileName() {
        return fileName;
    }

    public static Users init(String userName, String fileName) {
        return new Users(userName, fileName);
    }

    public static final Users Gabe = init("Gabriel Wawerski", "gabe.txt");
    public static final Users Bot = init("Ez El", "bot.txt");
    public static final Users Hube = init("Hubert Hubert", "hube.txt");
    public static final Users Leze = init("Jakub Smolak", "leze.txt");
    public static final Users Mege = init("Mikołaj Batyra", "mege.txt");
    public static final Users Jakubow = init("Kamil Jakubowski", "jakubow.txt");
    public static final Users Melchior = init("Michał Melchior", "melchior.txt");
    public static final Users Kaspe = init("Kamil Kasprzak", "kaspe.txt");
    public static final Users Petek = init("Piotr Bartoszek", "petek.txt");
    public static final Users Wiesio = init("Jarek Rodak", "wiesio.txt");

    public static List<Users> getUserList() {
        return List.of(Gabe, Bot, Leze, Hube, Mege, Jakubow, Kaspe, Wiesio, Melchior, Petek);
    }
}
