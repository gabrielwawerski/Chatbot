package bot.utils.gabe_modules.modules_base;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper_class.Message;
import bot.utils.bot.helper_interface.Util;
import bot.utils.gabe_modules.util.MessageModule;
import bot.utils.gabe_modules.util.ResourceModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extend from this class if you will be supplying commands through constructor. All {@link Module} methods are taken
 * care of, except {@link Module#process(Message)}, which you have to implement yourself.<br> If you need more control
 * over your commands, see {@link BareModule}. Take a moment to check these classes as well: {@link }
 * See {@linkplain Module#process(Message)} documentation for more info..
 *
 * @author Gabe
 */
public abstract class BaseModule extends BareModule {
    /** Your module's trigger commands, provided by you in the constructor. */
    protected final List<String> commands;

    public BaseModule(Chatbot chatbot, List<String> commands) {
        super(chatbot);
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



    @Override
    public ArrayList<String> getCommands() {
        return (ArrayList<String>) commands.stream().map(CONSTANTS::DEACTIONIFY).collect(Collectors.toList());
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }
}