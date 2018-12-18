package bot.impl.gabes_framework.resource;

import bot.Chatbot;
import bot.impl.orig_impl.exceptions.MalformedCommandException;
import bot.impl.orig_impl.helper.interfaces.Util;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.gabes_framework.simple.SimpleModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Extend if your module needs
 */
public abstract class ResourceModule extends SimpleModule {
    protected List<String> resourceContent;

    public ResourceModule(Chatbot chatbot, List<String> regexes) {
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
    public ResourceModule(Chatbot chatbot, List<String> regexes, String resourceName) {
        super(chatbot, regexes);
        try {
            this.resourceContent
                    = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + resourceName));
            System.out.println(getClass().getSimpleName() + " online.");
        } catch (IOException e) { // TODO add global debugMessages field in Chatbot so this can be toggled.
            System.out.println(getClass().getSimpleName() + " niedostępne w bieżącej sesji.");
            e.printStackTrace();
        }
    }

    public ResourceModule(Chatbot chatbot, String resourceName) {
        super(chatbot);
        try {
            this.resourceContent
                    = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + resourceName));
            System.out.println(getClass().getSimpleName() + " online.");
        } catch (IOException e) {
            System.out.println(getClass().getSimpleName() + " niedostępne w bieżącej sesji.");
            e.printStackTrace();
        }
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

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        for (String regex : regexList) {
            if (messageBody.matches(regex)) {
                return regex;
            }
        }
        return "";
    }
}
