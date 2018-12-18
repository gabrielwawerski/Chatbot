package bot.utils.gabe_modules.util.module_library;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.interfaces.Util;
import bot.core.helper.misc.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Extend if your module_library needs
 */
public abstract class ResourceModule extends SimpleModule {
    protected List<String> resourceContent;

    public ResourceModule(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
    }

    /**
     * Be sure to add file (with the same name as {@code resourceName argument} to your module_library's folder. Your module_library
     * folder's name has to also be named like your module_library's class name.
     *
     * @param chatbot
     * @param resourceName full resource name (with extension) located in your resource package folder.
     * @author Gabe
     */
    public ResourceModule(Chatbot chatbot, List<String> commands, String resourceName) {
        super(chatbot, commands);
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
        for (String command : commands) {
            if (match.equals(command)) {
                chatbot.sendMessage(Util.GET_RANDOM(resourceContent));
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        for (String command : commands) {
            if (messageBody.matches(command)) {
                return command;
            }
        }
        return "";
    }
}
