package bot.core.gabes_framework.core.point_system;

import bot.core.gabes_framework.core.database.User;

import java.util.ArrayList;

public class Ladder {
    private final String ladder;

    static Ladder getLadder(ArrayList<User> users) {
        int maxPoints = 0;
        StringBuilder returnLadder = new StringBuilder();

        for (User user : users) {
            if (user.getPoints() > maxPoints) {
                returnLadder.insert(0, user.getName() + " " + user.getPoints() + "\n");
            }
        }
        return new Ladder(returnLadder.toString());
    }

    private Ladder(String ladder) {
        this.ladder = ladder;
    }

    @Override
    public String toString() {
        return ladder;
    }
}
