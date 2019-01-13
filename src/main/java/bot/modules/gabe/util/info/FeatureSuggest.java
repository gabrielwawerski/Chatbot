package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.helper.resource.SaveResourceModule;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureSuggest extends SaveResourceModule {
    private final String INFO_MESSAGE = "Po !suggest opisz swój pomysł na nową funkcję!";

    private final String SUGGEST_REGEX = Utils.TO_REGEX("suggest");
    private final String POMYSL_REGEX = Utils.TO_REGEX("pomysl");

    private final String SUGGEST_ANY = Utils.TO_REGEX("suggest (.*)");
    private final String POMYSL_ANY = Utils.TO_REGEX("pomysl (.*)");

    public FeatureSuggest(Chatbot chatbot, String fileName) {
        super(chatbot, fileName);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        Matcher matcher = null;

        if (isOr(SUGGEST_REGEX, POMYSL_REGEX)) {
            addPoints(message, Utils.POINTS_FEATURESUGGEST_INFO_REGEX);

            chatbot.sendMessage(INFO_MESSAGE);
            return true;
        } else if (is(SUGGEST_ANY)) {
            matcher = Pattern.compile(SUGGEST_ANY).matcher(message.getMessage());

            if (matcher.find()) {
                addPoints(message, Utils.POINTS_FEATURESUGGEST_REGEX);

                String msg = message.getMessage().substring(9);
                if (msg.length() > 300) {
                    chatbot.sendMessage("Wiadomość za długa.");
                    return false;
                }
                msg = message.getSender().getName() + " " + msg;
                appendStringToFile(msg);
                chatbot.sendMessage(Utils.EMOJI_PUSHPIN + " Dzięki!");
                return true;
            }
        } else if (is(POMYSL_ANY)) {
            matcher = Pattern.compile(POMYSL_ANY).matcher(message.getMessage());
            if (matcher.find()) {
                addPoints(message, Utils.POINTS_FEATURESUGGEST_REGEX);

                String msg = message.getMessage().substring(8);
                msg = message.getSender().getName() + " " + msg;
                appendStringToFile(msg);
                chatbot.sendMessage(Utils.EMOJI_PUSHPIN + " Dzięki!");
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
    protected List<String> setRegexes() {
        return List.of(SUGGEST_REGEX, POMYSL_REGEX, SUGGEST_ANY, POMYSL_ANY);
    }
}
