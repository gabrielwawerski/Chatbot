package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class TranslateAPI extends ModuleBase {
    private final String EN_PL_TRANSLATE_REGEX = Utils.TO_REGEX("translate (.*)");
    private final String EN_PL_TR_REGEX = Utils.TO_REGEX("tr (.*)");

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
    protected List<String> setRegexes() {
        return List.of(EN_PL_TRANSLATE_REGEX, EN_PL_TR_REGEX, TRANSLATE__REGEX, PL_REGEX);
    }
}
