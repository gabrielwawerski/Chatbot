package bot.modules.gabe.point_system.util;

import bot.core.gabes_framework.core.database.User;

import java.util.*;

public class Ladder {
    private final String ladder;

    public static Ladder getLadder(ArrayList<User> users) {
        StringBuilder ladder = new StringBuilder();

        // sorts users by their points (ascending)
        users.sort(Comparator.comparing(User::getPoints));

        ladder.append("Ranking wg punktów:\n")
                .append("=================")
                .append("\n");

        for (int i = 0; i < users.size(); i++) {
            User currUser = users.get(users.size() - 1 - i);
            int currUserPts = currUser.getPoints();

            if (i + 1 == 10) {
                // do nothing - we don't need extra spaces, so ladder aligns
            } else if (currUserPts <= 9) {
                ladder.append("    ");
            } else if (currUserPts <= 99) {
                ladder.append("  ");
            }

            if (i != users.size() - 1) {
                ladder.append("(").append(currUserPts).append(") | ");
            } else {
                ladder.append("(").append(currUserPts).append(") | ");
            }

            ladder.append(currUser.getName())
                    .append("\n");

        }
        return new Ladder(ladder.toString());
    }

    public static Ladder getMsgLadder(ArrayList<User> users) {
        return generateLadder(users, LadderType.MESSAGES);
    }

    private static Ladder generateLadder(ArrayList<User> users, LadderType ladderType) {
        // TODO platform dependent ladders
        // if mobile - remove messenger ``` text ``` formatting - it only works on desktop
        if (ladderType == LadderType.POINTS) {

        } else if (ladderType == LadderType.MESSAGES) {
            StringBuilder ladder = new StringBuilder();
            users.sort(Comparator.comparing(User::getMessageCount));

            ladder.append("```")
                    .append("\nRanking wg wiadomości:\n")
                    .append("=================")
                    .append("\n");

            for (int i = 0; i < users.size(); i++) {
                User currUser = users.get(users.size() - 1 - i);
                int currUserMsgs = currUser.getMessageCount();

                ladder.append(i + 1);

                if (i + 1 < 10) {
                    ladder.append(". ")
                            .append("(")
                            .append(currUserMsgs)
                            .append(") ");

                    ladder.append(currUser.getName())
                            .append(System.lineSeparator());
                } else {
                    ladder.append(".")
                            .append("(")
                            .append(currUserMsgs)
                            .append(") ");

                    ladder.append(currUser.getName())
                            .append(System.lineSeparator())
                            .append("```");
                    return new Ladder(ladder.toString());
                }
            }
        }
        throw new IllegalArgumentException("Invalid ladder type. Found: " + ladderType.toString());
    }

    private Ladder(String ladder) {
        this.ladder = ladder;
    }

    @Override
    public String toString() {
        return ladder;
    }

    private enum LadderType {
        POINTS, MESSAGES
    }
}
