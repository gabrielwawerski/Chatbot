package bot.core.gabes_framework.util.resource;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.simple.SimpleModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Extend if your module needs
 */
public abstract class RandomResourceModule extends SimpleModule {
    protected List<String> resourceContent;

    public RandomResourceModule(Chatbot chatbot, List<String> regexes) {
        super(chatbot, regexes);
    }

    /**
     * Be sure to add a file (with the same name as {@code resourceName} to your module folder. Your module folder's name
     * has to be named like your module's class name.
     *
     * @param chatbot
     * @param resourceName full resource name (with extension) located in your resource package folder.
     * @version 1.0
     * @since 0.29
     */
    public RandomResourceModule(Chatbot chatbot, List<String> regexes, String resourceName) {
        super(chatbot, regexes);

        try {
            this.resourceContent
                    = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + resourceName));
            setOnline();
        } catch (IOException e) { // TODO add global debugMessages field in Chatbot so this can be toggled.
            setOffline();
            e.printStackTrace();
        }
    }

    public RandomResourceModule(Chatbot chatbot, String resourceName) {
        super(chatbot);
        try {
            this.resourceContent
                    = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + resourceName));
            setOnline();
        } catch (IOException e) {
            setOffline();
            e.printStackTrace();
        }
    }

    protected String getRandomMessage() {
        return Util.GET_RANDOM(resourceContent);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        String match = getMatch(message);
        for (String regex : regexList) {
            if (match.equals(regex)) {
                chatbot.sendMessage(Util.GET_RANDOM(resourceContent));
                return true;
            }
        }
        return false;
    }
}
