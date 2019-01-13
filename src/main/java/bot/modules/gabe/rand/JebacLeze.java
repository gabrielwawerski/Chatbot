package bot.modules.gabe.rand;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.resource.RandomResourceModule;

import java.util.List;

public class JebacLeze extends RandomResourceModule {
    private static final String JEBAC_LEZE = "jebac leze";
    private static final String JEBACLEZE = "jebacleze";
    private static final String JEBAiC_LEZE = "jebać leze";
    private static final String JEBAiC_iLEZE = "jebać łeze";

    private static final String JEBACLEZE_REGEX = Utils.TO_REGEX("jebacleze");
    private static final String JEBAiC_LEZE_REGEX = Utils.TO_REGEX("jebać leze");
    private static final String JEBAiC_iLEZE_REGEX = Utils.TO_REGEX("jebać łeze");
    private static final String LEZE_REGEX = Utils.TO_REGEX("leze");
    private static final String JEBAC_LEZE_REGEX = Utils.TO_REGEX("jebac leze");
    private static final String JEBAC_REGEX = Utils.TO_REGEX("jebac");

    public JebacLeze(Chatbot chatbot, String resourceName) {
        super(chatbot, resourceName);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isRegex()) {
            addPoints(message, Utils.POINTS_JEBACLEZE_REGEX);
            chatbot.sendMessage(getRandomMessage());
            return true;
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(JEBAC_LEZE,
                JEBACLEZE,
                JEBAiC_LEZE,
                JEBAiC_iLEZE,
                JEBACLEZE_REGEX,
                JEBAiC_LEZE_REGEX,
                JEBAiC_iLEZE_REGEX,
                LEZE_REGEX,
                JEBAC_LEZE_REGEX,
                JEBAC_REGEX);
    }
}
