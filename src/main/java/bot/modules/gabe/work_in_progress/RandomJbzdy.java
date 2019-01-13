package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class RandomJbzdy extends ModuleBase {
    private final static String URL = "https://jbzdy.pl/top/miesiac/1";

    private final String JBZDY_REGEX = Utils.TO_REGEX("jbzdy");
    private final String JBZD_REGEX = Utils.TO_REGEX("jbzd");
    private final String JB_REGEX = Utils.TO_REGEX("jb");

    public RandomJbzdy(Chatbot chatbot) {
        super(chatbot);
        initialize();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }

    private void initialize() {
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(JBZDY_REGEX, JBZD_REGEX, JB_REGEX);
    }

}
