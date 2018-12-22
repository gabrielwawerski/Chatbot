package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.resource.SaveResourceModule;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureSuggest extends SaveResourceModule {
    private final String INFO_MESSAGE = "Po !suggest podaj swój pomysł na funkcję!";

    private final String SUGGEST = Utils.TO_REGEX("suggest");
    private final String POMYSL = Utils.TO_REGEX("pomysl");

    private final String SUGGEST_ANY = Utils.TO_REGEX("suggest (.*)");
    private final String POMYSL_ANY = Utils.TO_REGEX("pomysl (.*)");

    public FeatureSuggest(Chatbot chatbot, String fileName) {
        super(chatbot, fileName);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (match.equals(SUGGEST) || match.equals(POMYSL)) {
            chatbot.sendMessage(INFO_MESSAGE);
            return true;
        } else if (match.equals(SUGGEST_ANY) || match.equals(POMYSL_ANY)) {
            Matcher matcher = Pattern.compile(POMYSL_ANY).matcher(message.getMessage());

            if (matcher.find()) {
                String msg = message.getMessage().substring(8);
                msg = message.getSender().getName() + " " + msg;
                appendStringToFile(msg);
                chatbot.sendMessage("U+1F5D2 Dzięki!");
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(SUGGEST)) {
            return SUGGEST;
        } else if (messageBody.matches(POMYSL)) {
            return POMYSL;
        } else if (messageBody.matches(SUGGEST_ANY)) {
            return SUGGEST_ANY;
        } else if (messageBody.matches(POMYSL_ANY)) {
            return POMYSL_ANY;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.TO_COMMAND(SUGGEST));
        commands.add(Utils.TO_COMMAND(POMYSL));
        commands.add(Utils.TO_COMMAND(SUGGEST_ANY));
        commands.add(Utils.TO_COMMAND(POMYSL_ANY));
        return commands;
    }
}
