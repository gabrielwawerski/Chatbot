package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.List;

public class RandomJbzdy extends ModuleBase {

    private final static String URL = "https://jbzdy.pl/top/miesiac/1";

    private final String JBZDY_REGEX = Utils.TO_REGEX("jbzdy");
    private final String JBZD_REGEX = Utils.TO_REGEX("jbzd");
    private final String JB_REGEX = Utils.TO_REGEX("jb");

    private final List<String> regexes;

    public RandomJbzdy(Chatbot chatbot) {
        super(chatbot);
        regexes = List.of(JBZDY_REGEX, JBZD_REGEX, JB_REGEX);
        initialize();
    }

    private void initialize() {

    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, regexes);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(regexes);
    }
}
