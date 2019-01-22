package bot.modules.gabe.text;

import bot.core.Chatbot;
import bot.modules.gabe.point_system.util.Points;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class B extends ModuleBase {
    private static final String B_ANY_REGEX = Utils.TO_REGEX("b (.*)");
    private static final String INFO_REGEX = Utils.TO_REGEX("b");

    public B(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(B_ANY_REGEX, INFO_REGEX);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();
        String temp;

        if (is(INFO_REGEX)) {
            pushPoints(message, Points.POINTS_B_INFO_REGEX);
            chatbot.sendMessage("Podaj tekst do transformacji.");
            return true;
        } else if (is(B_ANY_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(messageBody);
            if (matcher.find()) {
                pushPoints(message, Points.POINTS_B_REGEX);
                temp = matcher.group(1);
                for (int i = 0; i < messageBody.length(); i++) {
                    temp = temp
                            .replaceAll("B", Emoji.B)
                            .replaceAll("b", Emoji.B)
                            .replaceAll("P", Emoji.B)
                            .replaceAll("p", Emoji.B);
                }
                chatbot.sendMessage(temp);
                return true;
            }
        }
        return false;
    }
}
