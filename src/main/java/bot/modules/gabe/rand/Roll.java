package bot.modules.gabe.rand;

import bot.core.Chatbot;
import bot.modules.gabe.point_system.submodule.PointUtils;
import bot.modules.gabe.point_system.util.Points;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.ModuleBase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.hollandjake_api.helper.interfaces.Util.ACTIONIFY;

/**
 * @author Gabe
 */
public class Roll extends ModuleBase {
    private final int MIN_ROLL = 3;
    private final int MAX_ROLL = 100 + 1;

    private static final String REGEX_ROLL = ACTIONIFY("roll (\\d+)");
    private static final String REGEX_FIXED_ROLL = ACTIONIFY("roll");

    public Roll(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(REGEX_FIXED_ROLL, REGEX_ROLL);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(REGEX_FIXED_ROLL)) {
            db.refresh(user);
            int roll = roll(MIN_ROLL, MAX_ROLL);

            if (roll <= 9) {
                user.addPoints(Points.POINTS_ROLL_9);
            } else if (roll <= 29 && roll >= 10) {
                user.addPoints(Points.POINTS_ROLL_29);
            } else if (roll <= 59 && roll >= 30) {
                user.addPoints(Points.POINTS_ROLL_59);
            } else if (roll <= 89 && roll >= 60) {
                user.addPoints(Points.POINTS_ROLL_89);
            } else if (roll <= 99 && roll >= 90) {
                user.addPoints(Points.POINTS_ROLL_99);
            } else if (roll == 100) {
                user.addPoints(Points.POINTS_ROLL_100);
            }

            db.update(user);
            chatbot.sendMessage("Twój los: " + Integer.toString(roll));
            return true;

        } else if (is(REGEX_ROLL)) {
            db.refresh(user);
            Matcher matcher = Pattern.compile(REGEX_ROLL).matcher(message.getMessage());

            if (matcher.find()) {
                try {
                    int roll = roll(MIN_ROLL, Integer.parseInt(matcher.group(1)));

                    // TODO bonusowe punkty - jak wyżej, tylko procentowo (9, 29, 59, 89, 99, 100)%

                    user.addPoints(Points.POINTS_ROLL_REGEX);
                    db.update(user);
                    chatbot.sendMessage("Twój los: " + roll + " " + Emoji.HOURGLASS);
                    return true;
                } catch (NumberFormatException e) {
                    chatbot.sendMessage("Coś poszło nie tak... gdzie jest moje gabe?");
                    db.update(user);
                    return false;
                }
            }
            return false;
        }

        return false;
    }

    private int roll(int lower, int upper) {
        return (int) (Math.random() * (upper - lower) + lower);
    }

    private boolean isNumeric(String str) {
        return str.matches(".*\\d+.*");
    }
}
