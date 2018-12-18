package bot.utils.gabe_modules.module_base;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.core.helper.interfaces.Util;
import bot.utils.gabe_modules.util.module_library.MessageModule;
import bot.utils.gabe_modules.util.module_library.ResourceModule;
import bot.utils.gabe_modules.util.module_library.SimpleModule;
import bot.utils.gabe_modules.util.module_library.SingleMessageModule;

import java.util.ArrayList;

/**
 * Framework for creating modules. As of now, all code is a copy of hollandjake's {@link bot.core.helper.interfaces.Module}.<p>
 *
 * If writing module from scratch, you have to implement this interface, otherwise the API will not be able to access it
 * when iterating over loaded modules.<p>
 *
 * Each module that implements, as well as is created inside {@link Chatbot#loadModules()} method, will have it's
 * {@link #process(Message)} method called every time new message is grabbed. If a module wants to be able to react
 * to trigger commands, they have to be "actionified" first, inside the constructor. See {@link Util#ACTIONIFY(String)}.<p>
 *
 * Created for my own module implementation. I'm hoping to add more functionality, so this interface might change.
 * All credit for making custom messenger bots possible, goes to hollandjake.<p>
 *
 * For more info, see my libraries: {@linkplain ModuleBase}, {@linkplain SimpleModule}, {@linkplain SingleMessageModule},
 * {@linkplain MessageModule}, {@linkplain ResourceModule}.<p>
 *
 * @version 1.0
 * @since 0.19
 *
 * @author hollandjake (https://hollandjake.com)
 * @author Gabe (only javadocs)
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
     * @param message latest thread message, received from API gods themselves. Praise the sun.
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
     * Use {@link Util#DEACTIONIFY(String)} on your commands when adding them to ArrayList.
     *
     * @return
     */
    public ArrayList<String> getCommands();
}
