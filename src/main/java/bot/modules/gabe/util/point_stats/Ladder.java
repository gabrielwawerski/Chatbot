package bot.modules.gabe.util.point_stats;

import bot.core.gabes_framework.core.database.User;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Ladder {
    private final String ladder;

    static Ladder getLadder(ArrayList<User> users) {
        StringBuilder ladder = new StringBuilder();
        int spacing = 20;
        int userSpacing;

        // sorts users by their points (ascending)
        users.sort(Comparator.comparing(User::getPoints));

        ladder.append("Ladder:")
                .append("\n");

        for (int i = 0; i < users.size(); i++) {
            User currUser = users.get(users.size() - 1 - i);
            int currUserPts = currUser.getPoints();

            if (i < 9) {
                ladder.append(i + 1).append(". ");
            } else {
                ladder.append(i + 1).append(".");
            }

            if (currUserPts <= 9) {
                ladder.append("    ");
            } else if (currUserPts <= 99) {
                ladder.append("  ");
            }

            if (i != users.size() - 1) {
                ladder.append(" (").append(currUserPts).append(") | ");
            } else {
                ladder.append("(").append(currUserPts).append(") | ");
            }

            ladder.append(currUser.getName())
                    .append("\n");

        }
        return new Ladder(ladder.toString());
    }

    private Ladder(String ladder) {
        this.ladder = ladder;
    }

    @Override
    public String toString() {
        return ladder;
    }
}
