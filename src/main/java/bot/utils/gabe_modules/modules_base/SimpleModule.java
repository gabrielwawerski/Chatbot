package bot.utils.gabe_modules.modules_base;

import bot.Chatbot;
import bot.utils.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.Module;
import bot.utils.helper_class.Message;
import bot.utils.helper_interface.CONSTANTS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gabe
 */
public abstract class SimpleModule implements Module {
    protected final Chatbot chatbot;
    protected final List<String> commands;
    protected String match;

    public SimpleModule(Chatbot chatbot, List<String> commands) {
        this.chatbot = chatbot;
        this.commands = commands.stream().map(CONSTANTS::ACTIONIFY).collect(Collectors.toList());
    }

    /**
     * Tries to match received message against all {@link #commands} List entries. If a match is found, returns
     * corresponding entry from {@code commands} List.
     *
     * @param message
     * @return
     */
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

    public void updateMatch(Message message) {
        match = getMatch(message);
    }

    @Override
    public ArrayList<String> getCommands() {
        return (ArrayList<String>) commands.stream().map(CONSTANTS::DEACTIONIFY).collect(Collectors.toList());
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }
}
