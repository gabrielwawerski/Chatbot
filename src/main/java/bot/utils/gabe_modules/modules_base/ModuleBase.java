package bot.utils.gabe_modules.modules_base;

import bot.Chatbot;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.bot.helper.helper_interface.Util;
import bot.utils.gabe_modules.interfaces.Module;
import bot.utils.gabe_modules.util.MessageModule;
import bot.utils.gabe_modules.util.ResourceModule;
import bot.utils.gabe_modules.util.SingleMessageModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides an easy way of creating new modules. Does not provide messaging capabilities. If you need your module to be
 * able to communicate back, see {@link MessageModule}, {@link ResourceModule}, {@link SingleMessageModule}. If these do
 * not satisfy your needs, you can extend lower "level" classes such as this, or {@link BareModule} class, which gives
 * you only basic connection to this chatbot's API. You can of course extend from "higher level" classes and override as
 * needed.<br><br>
 *
 * Extend from this class if you will be supplying commands through constructor. All {@link Module} methods are taken
 * care of, except {@link Module#process(Message)}, which you have to implement yourself.<br> If you need more control
 * over your commands, see {@link BareModule}. Take a moment to check these classes as well: {@link }
 * See {@linkplain Module#process(Message)} documentation for more info.<br>
 * See {@link Module} and {@link BareModule} for more info.
 *
 * @author Gabe (gabriel.wawerski@gmail.com)
 */
public abstract class ModuleBase extends BareModule {
    /** Your module's trigger commands, provided by you in the constructor. See {@link #ModuleBase(Chatbot, List)}*/
    protected List<String> commands;

    /**
     * Use {@code List.of()} when creating an instance.
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
