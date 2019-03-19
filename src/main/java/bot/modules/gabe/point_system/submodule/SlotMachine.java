package bot.modules.gabe.point_system.submodule;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.util.Utils.TO_REGEX;

/**
 * @since 0.3313
 */
public class SlotMachine extends ModuleBase {
    private static final int MAX_BET = 1000000;
    private DBConnection db;
    private ArrayList<String> emojis;
    private static int combo = 0;

    private static final double MUL_MAX = 100;
    private static final double MUL_4 = 1.5;
    private static final double MUL_3 = 0.35;
    private static final double MUL_2 = 0.1;

    private static final double MUL_FILLED = 0.15;

    private static final double MUL_EXTRA_0 = 0.25;
    private static final double MUL_EXTRA_1 = 1.25;
    private static final double MUL_EXTRA_3 = 5;
    private static final double MUL_EXTRA_4 = 10;
    private static final double MUL_EXTRA_5 = 1000;

    private static final double MULL_LOSS = 1;

    private static final String REGEX_PULL_ANY = TO_REGEX("pull (\\d+)");
    private static final String REGEX_PULL_ALL = TO_REGEX("pull all");
    private static final String REGEX_INFO = TO_REGEX("pull");

    // TODO pull <x> <points> - pulls x amount of times for points.
    // if points go to 0 - stop and give feedback

    public SlotMachine(Chatbot chatbot) {
        super(chatbot);
        db = DBConnection.getInstance();
        emojis = Emoji.getAll();
        resetCombo();
    }

    @Override
    public boolean process(Message message) {
        updateMatch(message);

        if (is(REGEX_PULL_ANY)) {
            user = db.getUser(message);

            if (user == User.EMPTY_USER) {
                System.out.println("invalid user!");
                return false;
            }
            int bet;
            int size = emojis.size();
            String roll_1 = emojis.get(Utils.getRandom(size));
            String roll_2 = emojis.get(Utils.getRandom(size));
            String roll_3 = emojis.get(Utils.getRandom(size));
            String roll_4 = emojis.get(Utils.getRandom(size));
            String roll_5 = emojis.get(Utils.getRandom(size));

            Matcher matcher = Pattern.compile(REGEX_PULL_ANY).matcher(message.getMessage());

            if (matcher.find()) {
                if (matcher.group(1).length() > 7) {
                    chatbot.sendMessage("Nieprawidłowy zakład. Min: 1, Max: 1,000,000.\nTwoje punkty: " + user.getPoints());
                    return false;
                } else {
                    bet = Integer.parseInt(matcher.group(1));
                    if (bet > MAX_BET) {
                        chatbot.sendMessage("Nieprawidłowy zakład. Min: 1, Max: 1,000,000.\nTwoje punkty: " + user.getPoints());
                        return false;
                    }
                }

                if (bet > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów! (" + user.getPoints() + ")");
                    return false;
                } else if (user.getPoints() == 0) {
                    chatbot.sendMessage("Nie masz punktów!");
                    return false;
                }

                String msg;
                msg = roll_1 + " " + roll_2 + " " + roll_3 + " " + roll_4 + " " + roll_5;
                msg += processRoll(List.of(roll_1, roll_2, roll_3, roll_4, roll_5), bet);
                chatbot.sendMessage(msg);
                return true;
            }
            return false;

        } else if (is(REGEX_PULL_ALL)) {
            user = db.getUser(message);

            if (user == User.EMPTY_USER) {
                System.out.println("invalid user!");
                return false;
            }
            int bet;
            int size = emojis.size();
            String roll_1 = emojis.get(Utils.getRandom(size));
            String roll_2 = emojis.get(Utils.getRandom(size));
            String roll_3 = emojis.get(Utils.getRandom(size));
            String roll_4 = emojis.get(Utils.getRandom(size));
            String roll_5 = emojis.get(Utils.getRandom(size));

            if (user.getPoints() == 0) {
                chatbot.sendMessage("Nie masz punktów!");
                return false;
            }

            if (user.getPoints() > MAX_BET) {
                bet = MAX_BET;
            } else {
                bet = user.getPoints();
            }

            String msg;
            msg = roll_1 + " " + roll_2 + " " + roll_3 + " " + roll_4 + " " + roll_5;
            msg += processRoll(List.of(roll_1, roll_2, roll_3, roll_4, roll_5), bet);
            chatbot.sendMessage(msg);
            return true;

        } else if (is(REGEX_INFO)) {
            user = db.getUser(message);
            // TODO add points
            chatbot.sendMessage("!pull <stawka> !pull all\nIm więcej wygranych pod rząd tym więcej punktów!\nWięcej " + Emoji._100 + " = więcej extra pkt!");
            return true;
        }
        return false;
    }

    private String processRoll(List<String> rolls, int bet) {
        //region init
        String[] emotes = new String[5];
        int[] combos = new int[5];
        int points = bet;

        emotes[0] = rolls.get(0);
        emotes[1] = rolls.get(1);
        emotes[2] = rolls.get(2);
        emotes[3] = rolls.get(3);
        emotes[4] = rolls.get(4);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                combos[i] = 0;
            }
        }
        int counter100 = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (emotes[i].equals(emotes[j]) && i != j) {
                    combos[i]++;
                    if (emotes[j].equals(emotes[i]) && combos[j] != 0) {
                        combos[i]++;
                        combos[j] = 0;
                    }
                }
            }
            if (emotes[i].equals(Emoji._100)) {
                System.out.println("found 100");
                counter100++;
            }
        }
        //endregion

        System.out.println("points (before) = " + points);
        int temp;
        for (int i = 0; i < 5; i++) {
            temp = 0;
            if (combos[i] == 5) {
                temp += (int) (double) (bet * (MUL_MAX * getCombo()));
            } else if (combos[i] == 4) {
                temp += (int) (double) (bet * (MUL_4 * getCombo()));
            } else if (combos[i] == 3) {
                temp += (int) (double) (bet * (MUL_3 * getCombo()));
            } else if (combos[i] == 2) {
                temp += (int) (double) (bet * (MUL_2 * getCombo()));
            }
            points += temp;
        }


        System.out.println("points (after) = " + points);

        // TODO add removing points * times 100 emote gets randomed!
        if (counter100 == 1) {
            points += bet * MUL_EXTRA_0;
        } else if (counter100 == 2) {
            points += bet * MUL_EXTRA_1;
        } else if (counter100 == 3) {
            points += bet * MUL_EXTRA_3;
        } else if (counter100 == 4) {
            points += bet * MUL_EXTRA_4;
        } else if (counter100 == 5) {
            points += bet * MUL_EXTRA_5;
        }
        int totalCombo = combos[0] + combos[1] + combos[2] + combos[3] + combos[4];
        System.out.println("counter100 = " + counter100);
        System.out.println("combos: " + combos[0] + combos[1] + combos[2] + combos[3] + combos[4] + " " + PointUtils.format(totalCombo));


        String msg;
        if (totalCombo <= 1) {
            if (counter100 == 1) {
                if (getCombo() > 1) {
                    pushPoints(points + (int)(double)((bet * MUL_EXTRA_0)));
                    increaseCombo();
                    msg = "\n +" + points + "pkt! (" + user.getPoints() + ")\n Mnożnik pkt++!";
                } else {
                    pushPoints(points + (int)(double)((bet * MUL_EXTRA_0)));
                    System.out.println("current combo: " + getCombo());
                    msg = "\n+" + points + "pkt! (" + user.getPoints() + ")";
                }
                return msg;
            } else {
                resetCombo();
                int lostPoints = (int) ((double) (bet * MULL_LOSS));
                pullPoints(lostPoints);
                return "\n-" + lostPoints + "pkt. (" + user.getPoints() + ")";
            }
        } else if (totalCombo == 5) {
            pushPoints((int) ((double) points * MUL_FILLED));

            if (getCombo() > 1) {
                increaseCombo();
                System.out.println("combo = " + getAbsoluteCombo());
                msg = "\n +" + points + "pkt! (" + user.getPoints() + ")\n Mnożnik pkt++!";
            } else {
                increaseCombo();
                System.out.println("combo = " + getAbsoluteCombo());
                msg = "\n+" + points + "pkt! (" + user.getPoints() + ")";
            }
            return msg;
        } else {
            pushPoints(points);

            // nazwy dla 3,4,5 (4 - Kareta?)
            if (getCombo() > 1) {
                increaseCombo();
                System.out.println("combo = " + getAbsoluteCombo());
                msg = "\n +" + points + "pkt! (" + user.getPoints() + ")\n Mnożnik x" + getCombo() + "!";
            } else {
                increaseCombo();
                System.out.println("combo = " + getAbsoluteCombo());
                msg = "\n+" + points + "pkt! (" + user.getPoints() + ")";
            }
            return msg;
        }
    }

    private static void increaseCombo() {
        combo++;
    }

    private static int getAbsoluteCombo() {
        return combo;
    }

    private static int getCombo() {
        if (combo == 0) {
            return 1;
        } else
            return combo;
    }

    private static void resetCombo() {
        combo = 0;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(REGEX_PULL_ANY, REGEX_INFO, REGEX_PULL_ALL);
    }
}