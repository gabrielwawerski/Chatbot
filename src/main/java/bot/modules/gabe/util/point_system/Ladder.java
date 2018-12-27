package bot.modules.gabe.util.point_system;

import bot.core.gabes_framework.core.database.User;

import java.util.*;

class Ladder {
    private final String ladder;

    static Ladder getMsgLadder(ArrayList<User> users) {
        StringBuilder ladder = new StringBuilder();
        users.sort(Comparator.comparing(User::getMessageCount));

        ladder.append("Ranking wiadomości:\n");
        for (int i = 0; i < users.size(); i++) {
            User currUser = users.get(users.size() - 1 - i);
            int currUserMsgs = currUser.getMessageCount();

            if (i + 1 == 1) {
                ladder.append("1\ufe0f\u20e3 ");
            } else if (i + 1 == 2) {
                ladder.append("2\ufe0f\u20e3 ");
            } else if (i + 1 == 3) {
                ladder.append("3\ufe0f\u20e3 ");
            } else if (i + 1 == 4) {
                ladder.append("4\ufe0f\u20e3 ");
            } else if (i + 1 == 5) {
                ladder.append("5\ufe0f\u20e3 ");
            } else if (i + 1 == 6) {
                ladder.append("6\ufe0f\u20e3 ");
            } else if (i + 1 == 7) {
                ladder.append("7\ufe0f\u20e3 ");
            } else if (i + 1 == 8) {
                ladder.append("8\ufe0f\u20e3 ");
            } else if (i + 1 == 9) {
                ladder.append("9\ufe0f\u20e3 ");
            } else if (i + 1 == 10) {
                ladder.append("1\ufe0f\u20e3").append("0\ufe0f\u20e3");
            }
            if (i + 1 == 10) {
                // do nothing - we don't need extra spaces, so ladder aligns
            } else if (currUserMsgs <= 9) {
                ladder.append("    ");
            } else if (currUserMsgs <= 99) {
                ladder.append("  ");
            }

            if (i != 10) {
                ladder.append("(").append(currUserMsgs).append(") | ");
            } else {
                ladder.append("(").append(currUserMsgs).append(") | ");
            }

            ladder.append(currUser.getName())
                    .append("\n");
        }
        return new Ladder(ladder.toString());
    }

    static Ladder getLadder(ArrayList<User> users) {
        StringBuilder ladder = new StringBuilder();

        // sorts users by their points (ascending)
        users.sort(Comparator.comparing(User::getPoints));

        ladder.append("Ranking:")
                .append("\n");

        for (int i = 0; i < users.size(); i++) {
            User currUser = users.get(users.size() - 1 - i);
            int currUserPts = currUser.getPoints();

            if (i + 1 == 1) {
                ladder.append("1\ufe0f\u20e3 ");
            } else if (i + 1 == 2) {
                ladder.append("2\ufe0f\u20e3 ");
            } else if (i + 1 == 3) {
                ladder.append("3\ufe0f\u20e3 ");
            } else if (i + 1 == 4) {
                ladder.append("4\ufe0f\u20e3 ");
            } else if (i + 1 == 5) {
                ladder.append("5\ufe0f\u20e3 ");
            } else if (i + 1 == 6) {
                ladder.append("6\ufe0f\u20e3 ");
            } else if (i + 1 == 7) {
                ladder.append("7\ufe0f\u20e3 ");
            } else if (i + 1 == 8) {
                ladder.append("8\ufe0f\u20e3 ");
            } else if (i + 1 == 9) {
                ladder.append("9\ufe0f\u20e3 ");
            } else if (i + 1 == 10) {
                ladder.append("1\ufe0f\u20e3").append("0\ufe0f\u20e3");
            }

//            // simple ordering. USE IF YOU FUCK SHIT UP WITH EMOJI ORDERING!!!
//            if (i < 9) {
//                ladder.append(i + 1).append(". ");
//            } else {
//                ladder.append(i + 1).append(".");
//            }

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

    private Ladder(String ladder) {
        this.ladder = ladder;
    }

    @Override
    public String toString() {
        return ladder;
    }
}
