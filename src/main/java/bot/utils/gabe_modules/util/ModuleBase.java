package bot.utils.gabe_modules.util;

import bot.Chatbot;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.bot.helper.helper_interface.Util;
import bot.utils.gabe_modules.module_base.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Offers a simple way to create modules. Does not provide messaging capabilities. If you need your module to be
 * able to communicate back, see {@link MessageModule}, {@link ResourceModule}, {@link SingleMessageModule}
 * implementations. If these do not satisfy your needs, you can extend "lower" level classes, such as this one,
 * or {@link BareModule} class, which gives you only basic connection to the chatbot's API. You can of course implement
 * {@link Module} interface and it's methods yourself or even use hollandjake's implementation.<br>
 * See {@link bot.utils.bot.helper.helper_interface.Module}
 * <br><br>
 * Extend from this class if you will be supplying commands through constructor. All {@link Module} methods have been
 * taken care of for you, except {@link Module#process(Message)}, which you have to implement yourself.<br>
 * If you need more control over your commands, see {@link BareModule}.  See
 * {@linkplain Module#process(Message)}, {@link Module} and {@link BareModule} for more info.
 *
 * @author Gabe (gabriel.wawerski@gmail.com)
 */
public abstract class ModuleBase extends BareModule {
    /** Your module's trigger commands, provided by you in the constructor. See {@link #ModuleBase(Chatbot, List)}*/
    protected List<String> commands;

    /**
     * Use {@code List.of()} to provide the commands.
     *
     * @param chatbot chatbot reference
     * @param commands trigger commands that bot will react to in {@link #process(Message)} method
     */
    public ModuleBase(Chatbot chatbot, List<String> commands) {
        super(chatbot);
        this.commands = commands.stream().map(Util::ACTIONIFY).collect(Collectors.toList());
    }

    public ModuleBase(Chatbot chatbot) {
        super(chatbot);
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
        return (ArrayList<String>) commands.stream().map(Util::DEACTIONIFY).collect(Collectors.toList());
    }
}
