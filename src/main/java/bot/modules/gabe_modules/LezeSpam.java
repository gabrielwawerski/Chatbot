package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.impl.gabes_framework.resource.ResourceModule;

import java.util.List;

public class LezeSpam extends ResourceModule {
    public LezeSpam(Chatbot chatbot, List<String> commands, String resourceName) {
        super(chatbot, commands, resourceName);
    }
}