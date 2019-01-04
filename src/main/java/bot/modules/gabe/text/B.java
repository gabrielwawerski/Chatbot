package bot.modules.gabe.text;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.core.Utils;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.Utils.EMOJI_B;

public class B extends ModuleBase {
    private final String B_ANY_REGEX = Utils.TO_REGEX("b (.*)");
    private final String INFO_REGEX = Utils.TO_REGEX("b");

    public B(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();
        String temp;

        if (is(INFO_REGEX)) {
            addPoints(message, Utils.POINTS_B_INFO_REGEX);
            chatbot.sendMessage("Podaj tekst do transformacji.");
            return true;
        } else if (is(B_ANY_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(messageBody);
            if (matcher.find()) {
                addPoints(message, Utils.POINTS_B_REGEX);
                temp = matcher.group(1);
                for (int i = 0; i < messageBody.length(); i++) {
                    temp = temp
                            .replaceAll("B", EMOJI_B)
                            .replaceAll("b", EMOJI_B)
                            .replaceAll("P", EMOJI_B)
                            .replaceAll("p", EMOJI_B);
                }
                chatbot.sendMessage(temp);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, B_ANY_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(B_ANY_REGEX);
    }
}
