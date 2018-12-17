package bot.utils.gabe_modules.util;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_interface.Util;
import bot.utils.bot.helper.helper_class.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class ResourceModule extends ModuleBase {
    protected List<String> resourceContent;

    public ResourceModule(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
    }

    /**
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
