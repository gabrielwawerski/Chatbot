package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.resource.SaveResourceModule;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureSuggest extends SaveResourceModule {
    private final String INFO_MESSAGE = "Po !suggest opisz swój pomysł na nową funkcję!";

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
        Matcher matcher = null;

        if (match.equals(SUGGEST) || match.equals(POMYSL)) {
            addPoints(message, 1);
            chatbot.sendMessage(INFO_MESSAGE);
            return true;
        } else if (match.equals(SUGGEST_ANY)) {
            matcher = Pattern.compile(SUGGEST_ANY).matcher(message.getMessage());

            if (matcher.find()) {
                addPoints(message, 2);
                String msg = message.getMessage().substring(9);
                if (msg.length() > 300) {
                    chatbot.sendMessage("Wiadomość za długa.");
                    return false;
                }
                msg = message.getSender().getName() + " " + msg;
                appendStringToFile(msg);
                chatbot.sendMessage(Utils.PUSHPIN_EMOJI + " Dzięki!");
                return true;
            }
        } else if (match.equals(POMYSL_ANY)) {
            matcher = Pattern.compile(POMYSL_ANY).matcher(message.getMessage());
            if (matcher.find()) {
                addPoints(message, 2);
                String msg = message.getMessage().substring(8);
                msg = message.getSender().getName() + " " + msg;
                appendStringToFile(msg);
                chatbot.sendMessage(Utils.PUSHPIN_EMOJI + " Dzięki!");
//                 TODO random responses each time
//                List<String> randomRespones;
//                String uruchamiamAi = "Dzięki! \uD83D\uDD2C";
//                randomRespones = List.of(uruchamiamAi, );
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
