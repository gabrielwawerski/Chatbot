package bot.modules.gabe_modules.random.rand_text;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.resource.RandomResourceModule;

import java.util.ArrayList;

public class JebacLeze extends RandomResourceModule {
    private final String JEBAC_LEZE = "jebac leze";
    private final String JEBACLEZE = "jebacleze";

    private final String JEBACLEZE_REGEX = Utils.TO_REGEX("jebacleze");
    private final String LEZE_REGEX = Utils.TO_REGEX("leze");
    private final String JEBAC_LEZE_REGEX = Utils.TO_REGEX("jebac leze");
    private final String JEBAC_REGEX = Utils.TO_REGEX("jebac");

    public JebacLeze(Chatbot chatbot, String resourceName) {
        super(chatbot, resourceName);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOr(JEBACLEZE_REGEX, LEZE_REGEX, JEBAC_LEZE_REGEX, JEBAC_REGEX, JEBAC_LEZE, JEBACLEZE)) {
            chatbot.sendMessage(getRandomMessage());
            return true;
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, JEBACLEZE_REGEX, LEZE_REGEX, JEBAC_LEZE_REGEX, JEBAC_REGEX, JEBAC_LEZE, JEBACLEZE);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(JEBACLEZE_REGEX, LEZE_REGEX, JEBAC_LEZE_REGEX, JEBAC_REGEX, JEBAC_LEZE, JEBACLEZE);
    }
}
