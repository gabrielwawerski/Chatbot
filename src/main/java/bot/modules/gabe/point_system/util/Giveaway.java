package bot.modules.gabe.point_system.util;

import bot.core.gabes_framework.core.database.User;

public class Giveaway {
    private User giver;
    private int points;

    public static final Giveaway NO_GIVEAWAY = new Giveaway();
    public static Giveaway PUBLIC_GIVEAWAY = NO_GIVEAWAY;

    private Giveaway() {
        giver = User.EMPTY_USER;
        points = 0;
    }

    private Giveaway(User user, int points) {
        giver = user;
        this.points = points;
    }

    public static void newGiveaway(User user, int points) {
        PUBLIC_GIVEAWAY = new Giveaway(user, points);
    }

    public static void reset() {
        PUBLIC_GIVEAWAY = NO_GIVEAWAY;
    }

    public User getGiver() {
        return giver;
    }

    public int getPoints() {
        return points;
    }
}
