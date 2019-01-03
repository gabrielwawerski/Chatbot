package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;

/**
 *
 */
public class TranslateAPI extends ModuleBase {
    private final String EN_PL_TR_REGEX = Utils.TO_REGEX("tr (.*)");
    private final String EN_PL_TRANSLATE_REGEX = Utils.TO_REGEX("translate (.*)");

    private final String TRANSLATE__REGEX = Utils.TO_REGEX("translate (.*)");
    private final String PL_REGEX = Utils.TO_REGEX("pl (.*)");

    public TranslateAPI(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        if (isOr(EN_PL_TR_REGEX, EN_PL_TRANSLATE_REGEX)) {

        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return null;
    }

    @Override
    public ArrayList<String> getCommands() {
        return null;
    }
}
