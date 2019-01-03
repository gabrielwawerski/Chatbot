package bot.modules.gabe.rand;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.resource.RandomResourceModule;

import java.util.ArrayList;
import java.util.List;

public class JebacLeze extends RandomResourceModule {
    private final String JEBAC_LEZE = "jebac leze";
    private final String JEBACLEZE = "jebacleze";
    private final String JEBAiC_LEZE = "jebać leze";
    private final String JEBAiC_iLEZE = "jebać łeze";

    private final String JEBACLEZE_REGEX = Utils.TO_REGEX("jebacleze");
    private final String JEBAiC_LEZE_REGEX = Utils.TO_REGEX("jebać leze");
    private final String JEBAiC_iLEZE_REGEX = Utils.TO_REGEX("jebać łeze");
    private final String LEZE_REGEX = Utils.TO_REGEX("leze");
    private final String JEBAC_LEZE_REGEX = Utils.TO_REGEX("jebac leze");
    private final String JEBAC_REGEX = Utils.TO_REGEX("jebac");

    private final List<String> regexes;

    public JebacLeze(Chatbot chatbot, String resourceName) {
        super(chatbot, resourceName);
        regexes = List.of(JEBAC_LEZE, JEBACLEZE,
                JEBAiC_LEZE, JEBAiC_iLEZE,
                JEBACLEZE_REGEX, JEBAiC_LEZE_REGEX,
                JEBAiC_iLEZE_REGEX, LEZE_REGEX,
                JEBAC_LEZE_REGEX, JEBAC_REGEX);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(regexes)) {
            addPoints(message, Utils.POINTS_JEBACLEZE_REGEX);
            chatbot.sendMessage(getRandomMessage());
            return true;
        }
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
