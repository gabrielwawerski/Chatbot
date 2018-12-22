package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.resource.SaveResourceModule;

import java.util.ArrayList;
import java.util.List;
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

        Matcher matcher = null;
        if (match.equals(SUGGEST) || match.equals(POMYSL)) {
            chatbot.sendMessage(INFO_MESSAGE);
            return true;
        } else if (match.equals(SUGGEST_ANY)) {
            matcher = Pattern.compile(SUGGEST_ANY).matcher(message.getMessage());

            if (matcher.find()) {
                String msg = message.getMessage().substring(7);
                msg = message.getSender().getName() + " " + msg;
                appendStringToFile(msg);
                chatbot.sendMessage("Zapisano!  " + Utils.EMOJI_PUSHPIN);
                return true;
            }
        } else if (match.equals(POMYSL_ANY)) {
            matcher = Pattern.compile(POMYSL_ANY).matcher(message.getMessage());
            if (matcher.find()) {
                String msg = message.getMessage().substring(7);
                msg = message.getSender().getName() + " " + msg;
                appendStringToFile(msg);
                chatbot.sendMessage(Utils.EMOJI_PUSHPIN + "\nZapisano!");
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
