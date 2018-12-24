package bot.gabes_framework.core.libs;

import java.util.List;

public class User {
    public static final String PCIONBOT = "Ez El";
    public static final String GABE = "Gabriel Wawerski";
    public static final String LEZE = "Jakub Smolak";
    public static final String HUBE = "Hubert Hubert";
    public static final String KASPE = "Kamil Kasprzak";
    public static final String WIESIO = "Jarek Rodak";
    public static final String JAKUBOWSKI = "Kamil Jakubowski";
    public static final String MELCHIOR = "Michał Melchior";
    public static final String MEGE = "Mikołaj Batyra";
    public static final String PETEK = "Piotr Bartoszek";

    private static List<String> USERS
            = List.of(PCIONBOT, GABE, LEZE, HUBE, KASPE, WIESIO, JAKUBOWSKI, MELCHIOR, MEGE, PETEK);

    private User() {
        // TODO
    }

    // TODO
    public static User getUser(String USER) {
        for (String user : USERS) {
            if (user.equals(USER)) {
            }
        }
        return null;
    }
}
