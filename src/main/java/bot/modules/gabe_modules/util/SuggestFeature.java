package bot.modules.gabe_modules.util;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.resource.SaveResourceModule;
import bot.gabes_framework.simple.SimpleModule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuggestFeature extends SaveResourceModule {
    private final String INFO_MESSAGE = "Po komendzie !suggest opisz funkcjonalność którą chciałbyś zobaczyć.";

    private final String SUGGEST_REGEX = Utils.TO_REGEX("suggest");
    private final String SUGGESTION_REGEX = Utils.TO_REGEX("suggest (.*)");

    public SuggestFeature(Chatbot chatbot, String fileName) {
        super(chatbot, fileName);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (match.equals(SUGGEST_REGEX)) {
            chatbot.sendMessage(INFO_MESSAGE);
            return true;
        } else if (match.equals(SUGGESTION_REGEX)) {
            Matcher matcher = Pattern.compile(SUGGESTION_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                String msg = message.getMessage().substring(8);
                msg = message.getSender().getName() + " " + msg;
                appendStringToFile(msg);
                chatbot.sendMessage("Zapisano, dzięki!");
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(SUGGEST_REGEX)) {
            return SUGGEST_REGEX;
        } else if (messageBody.matches(SUGGESTION_REGEX)) {
            return SUGGESTION_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.TO_COMMAND(SUGGEST_REGEX));
        commands.add(Utils.TO_COMMAND(SUGGESTION_REGEX));
        return commands;
    }
}
