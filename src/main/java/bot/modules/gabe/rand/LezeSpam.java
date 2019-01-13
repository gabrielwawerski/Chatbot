package bot.modules.gabe.rand;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.resource.RandomResourceModule;

import java.util.List;

public class LezeSpam extends RandomResourceModule {
    public LezeSpam(Chatbot chatbot, String resourceName) {
        super(chatbot, resourceName);
    }

    @Override
    protected List<String> setRegexes() {
        return getMappedRegexes("spam", "kurwa");
    }
}