package bot.utils.gabe_modules.util;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_interface.Util;
import bot.utils.gabe_modules.modules_base.ModuleBase;
import bot.utils.bot.helper.helper_class.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ResourceModule extends ModuleBase {
    protected List<String> resourceName;

    /**
     *
     * @param chatbot
     * @param commands
     * @param resourceName full resource name (with extension) located in your resource package folder.
     * @author Gabe
     */
    public ResourceModule(Chatbot chatbot, List<String> commands, String resourceName) {
        super(chatbot, commands);

        try {
            System.out.println(appendModulePath(resourceName));
            this.resourceName = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + resourceName));
        } catch (IOException e) {
            // TODO add global debugMessages field in Chatbot so this can be toggled.
            System.out.println(getClass().getSimpleName() + " niedostępne w bieżącej sesji.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : commands) {
            if (match.equals(command)) {
                Matcher matcher = Pattern.compile(match).matcher(message.getMessage());
                if (matcher.find() && !matcher.group(1).isEmpty()) {
                    String randomMessage = Util.GET_RANDOM(resourceName);
                    chatbot.sendMessage(randomMessage);
                } else {
                    throw new MalformedCommandException();
                }
                return true;
            } else {
                return false;
            }
        }
        System.out.println("Coś poszło nie tak...\ncommands.size(): " + commands.size());
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
