package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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

    private static final double MUL_2 = 1.2;
    private static final double MUL_3 = 1.4;
    private static final double MUL_4 = 1.6;
    private static final double MUL_MAX = 2;

    private static final double MUL_EXTRA_1 = 0.1;
    private static final double MUL_EXTRA_2 = 0.3;
    private static final double MUL_EXTRA_3 = 0.5;
    private static final double MUL_EXTRA_4 = 1;
    private static final double MUL_EXTRA_5 = 2;

    private static final String PULL_REGEX = TO_REGEX("pull (\\d+)");

    public SlotMachine(Chatbot chatbot) {
        super(chatbot);
        db = DBConnection.getInstance();
        emojis = Emoji.getAll();
    }

    @Override
    public boolean process(Message message) {
        updateMatch(message);

        if (is(PULL_REGEX)) {
            User user;
            String msg = null;
            Matcher matcher = Pattern.compile(PULL_REGEX).matcher(message.getMessage());
            int bet;
            int size = emojis.size();

            String roll_1 = emojis.get(Utils.getRandom(size));
            roll_1 = emojis.get(Utils.getRandom(size));
            roll_1 = emojis.get(Utils.getRandom(size));

            String roll_2 = emojis.get(Utils.getRandom(size));
            roll_2 = emojis.get(Utils.getRandom(size));
            roll_2 = emojis.get(Utils.getRandom(size));

            String roll_3 = emojis.get(Utils.getRandom(size));
            roll_3 = emojis.get(Utils.getRandom(size));
            roll_3 = emojis.get(Utils.getRandom(size));

            String roll_4 = emojis.get(Utils.getRandom(size));
            roll_4 = emojis.get(Utils.getRandom(size));
            roll_4 = emojis.get(Utils.getRandom(size));

            String roll_5 = emojis.get(Utils.getRandom(size));

            if (matcher.find()) {
                System.out.println("matcher found");
                bet = Integer.parseInt(matcher.group(1));

                if ((user = db.getUser(message)) != null) {
                    if (bet > user.getPoints()) {
                        chatbot.sendMessage("Nie masz tylu punktów! (" + user.getPoints() + ")");
                        return false;
                    } else if (user.getPoints() == 0) {
                        chatbot.sendMessage("Nie masz punktów!");
                        return false;
                    } else if (bet <= 0 || bet > MAX_BET) {
                        chatbot.sendMessage("Nieprawidłowy zakład. Min: 1, Max: 1,000,000. Twoje punkty: " + user.getPoints());
                        return false;
                    }
                }
                msg = roll_1 + " " + roll_2 + " " + roll_3 + " " + roll_4 + " " + roll_5 + "\n";
                msg += processRoll(user, List.of(roll_1, roll_2, roll_3, roll_4, roll_5), bet);
                chatbot.sendMessage(msg);
                return true;
            }
            chatbot.sendMessage("Nieprawidłowa komenda.");
            return false;
        }
        return false;
    }

    private String processRoll(User user, List<String> rolls, int bet) {
        //region declarations
        int points = 0;

        int mul2 = 2;
        int mul3 = 3;
        int mul4 = 4;
        int mul5 = 5;

        int combo1 = 0;
        int combo2 = 0;
        int combo3 = 0;
        int combo4 = 0;
        int combo5 = 0;

        String roll1 = rolls.get(0);
        String roll2 = rolls.get(1);
        String roll3 = rolls.get(2);
        String roll4 = rolls.get(3);
        String roll5 = rolls.get(4);
        //endregion

        //region find combos
        if (roll1.equals(roll2)) combo1++;
        if (roll1.equals(roll3)) combo1++;
        if (roll1.equals(roll4)) combo1++;
        if (roll1.equals(roll5)) combo1++;
        
        if (roll2.equals(roll1)) combo2++;
        if (roll2.equals(roll3)) combo2++;
        if (roll2.equals(roll4)) combo2++;
        if (roll2.equals(roll5)) combo2++;
        
        if (roll3.equals(roll1)) combo3++;
        if (roll3.equals(roll2)) combo2++;
        if (roll3.equals(roll4)) combo3++;
        if (roll3.equals(roll5)) combo3++;
        
        if (roll4.equals(roll1)) combo4++;
        if (roll4.equals(roll2)) combo4++;
        if (roll4.equals(roll3)) combo4++;
        if (roll4.equals(roll5)) combo4++;
        
        if (roll5.equals(roll1)) combo5++;
        if (roll5.equals(roll2)) combo5++;
        if (roll5.equals(roll3)) combo5++;
        if (roll5.equals(roll4)) combo5++;
        //endregion

        if (combo1 == mul5 | combo2 == mul5 | combo3 == mul5 | combo4 == mul5 | combo5 == mul5) {
            if (combo1 == mul5) points = (int) (bet *= MUL_MAX);
            else if (combo2 == mul5) points = (int) (bet *= MUL_MAX);
            else if (combo3 == mul5) points = (int) (bet *= MUL_MAX);
            else if (combo4 == mul5) points = (int) (bet *= MUL_MAX);
            else if (combo5 == mul5) points = (int) (bet *= MUL_MAX);
            System.out.println("bet = " + bet);
        } else if (combo1 == mul4 | combo2 == mul4 | combo3 == mul4 | combo4 == mul4 | combo5 == mul4) {
            if (combo1 == mul4) points = (int) (bet *= MUL_4);
            else if (combo2 == mul4) points = (int) (bet *= MUL_4);
            else if (combo3 == mul4) points = (int) (bet *= MUL_4);
            else if (combo4 == mul4) points = (int) (bet *= MUL_4);
            else if (combo5 == mul4) points = (int) (bet *= MUL_4);
            System.out.println("bet = " + bet);
        } else if (combo1 == mul3 | combo2 == mul3 | combo3 == mul3 | combo4 == mul3 | combo5 == mul3) {
            if (combo1 == mul3) points = (int) (bet *= MUL_3);
            else if (combo2 == mul3) points = (int) (bet *= MUL_3);
            else if (combo3 == mul3) points = (int) (bet *= MUL_3);
            else if (combo4 == mul3) points = (int) (bet *= MUL_3);
            else if (combo5 == mul3) points = (int) (bet *= MUL_3);
            System.out.println("bet = " + bet);
        } else if (combo1 == mul2 | combo2 == mul2 | combo3 == mul2 | combo4 == mul2 || combo5 == mul2) {
            if (combo1 == mul2) points = (int) (bet *= MUL_2);
            else if (combo2 == mul2) points = (int) (bet *= MUL_2);
            else if (combo3 == mul2) points = (int) (bet *= MUL_2);
            else if (combo4 == mul2) points = (int) (bet *= MUL_2);
            else if (combo5 == mul2) points = (int) (bet *= MUL_2);
            System.out.println("bet = " + bet);
        }

        System.out.println("bet = " + bet);

        points = bet;

        if (combo1 + combo2 + combo3 + combo4 + combo5 < 2) {

            user.subPoints(points);
            db.update(user);
            return "Straciłeś " + points + " pkt. (" + user.getPoints() + ")";
        } else {
            user.addPoints(points);
            db.update(user);
            return "Wygrałeś " + points + " pkt! (" + user.getPoints() + ")";
        }
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(PULL_REGEX);
    }
}
