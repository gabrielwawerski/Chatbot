package bot.utils.gabe_modules.module_base;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.bot.helper.helper_interface.Util;
import bot.utils.gabe_modules.util.BareModule;
import bot.utils.gabe_modules.util.ModuleBase;
import bot.utils.gabe_modules.util.MessageModule;
import bot.utils.gabe_modules.util.ResourceModule;
import bot.utils.gabe_modules.util.SingleMessageModule;

import java.util.ArrayList;

/**
 * If writing module from scratch, you have to implement this interface, otherwise the API will not be able to access it
 * when iterating over loaded modules.<p>
 * Each module that implements, as well as is created inside {@link Chatbot#loadModules()} method, will have it's
 * {@link #process(Message)} method called every time new message is grabbed.<p>
 * If a module wants to be able to react to trigger commands, they have to be "actionified" first, inside the constructor.
 * See {@link Util#ACTIONIFY(String)}.<p>
 *
 * See my concrete implementations for more info: {@link BareModule}, {@link ModuleBase}, {@link SingleMessageModule},
 * {@link MessageModule}, {@link ResourceModule}.
 *
 * @author Gabe
 */
public interface Module {
    /**
     * You should first call {@link #getMatch(Message)}.
     *
     * Use this snippet if your module will send only one, same message.
     * <pre>{@code
     * updateMatch(message);
     * for (String command : commands) {
     *     if (match.equals(command)) {
     *         chatbot.sendMessage(this.message);
     *         return true;
     *     }
     * }
     * return false;
     * }</pre>
     *
     * @param message latest message, received from API gods themselves. Praise the sun.
     * @return should return true if request has been successfully processed, false otherwise
     * @throws MalformedCommandException
     */
    public boolean process(Message message) throws MalformedCommandException;

    /**
     * Should make an attempt to match your regexes against latest received message.
     * @param message
     * @return
     */
    public String getMatch(Message message);

    public String appendModulePath(String message);

    /**
     * Use {@link Util#DEACTIONIFY(String)} on your commands when adding them to ArrayList to return.
     *
     * @return
     */
    public ArrayList<String> getCommands();
}
