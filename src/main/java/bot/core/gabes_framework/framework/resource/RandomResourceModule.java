package bot.core.gabes_framework.framework.resource;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.hollandjake_api.helper.misc.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Extend if your module needs
 */
public abstract class RandomResourceModule extends ModuleBase {
    protected List<String> resourceContents;

    /**
     * Be sure to add a file (with the same name as {@code resourceName}) to your module folder in "resources".
     * Your module's folder name has to be named exactly like it's class name.
     *
     * @param resourceName resource name (with extension), located in your resource folder.
     * @version 1.0
     * @since 0.29
     */
    public RandomResourceModule(Chatbot chatbot, String resourceName) {
        super(chatbot);
        try {
            this.resourceContents
                    = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + resourceName));
            setOnline();
        } catch (IOException e) { // TODO add global debugMessages field in Chatbot so this can be toggled.
            setOffline();
            e.printStackTrace();
        }
    }

    protected String getRandomMessage() {
        return Util.GET_RANDOM(resourceContents);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isRegex()) {
            chatbot.sendMessage(Util.GET_RANDOM(resourceContents));
            return true;
        }
        return false;
    }
}
