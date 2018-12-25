package bot.core.gabes_framework.core.point_system;

public class User implements DataContainer {
    private final String name;

    public User(Users user) {
        this.name = user.name();
    }
}
