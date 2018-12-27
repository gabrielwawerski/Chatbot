package bot.modules.gabe.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.core.Utils;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.Utils.B_EMOJI;

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
            chatbot.sendMessage("Podaj tekst do transformacji.");
            return true;
        } else if (is(B_ANY_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(messageBody);
            if (matcher.find()) {
                temp = matcher.group(1);
                for (int i = 0; i < messageBody.length(); i++) {
                    temp = temp
                            .replaceAll("B", B_EMOJI)
                            .replaceAll("b", B_EMOJI)
                            .replaceAll("P", B_EMOJI)
                            .replaceAll("p", B_EMOJI);
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
