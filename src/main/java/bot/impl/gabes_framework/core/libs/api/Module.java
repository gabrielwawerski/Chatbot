package bot.impl.gabes_framework.core.libs.api;

import bot.Chatbot;
import bot.impl.orig_impl.exceptions.MalformedCommandException;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.orig_impl.helper.interfaces.Util;
import bot.impl.gabes_framework.core.ModuleBase;
import bot.impl.gabes_framework.message.MessageModule;
import bot.impl.gabes_framework.resource.ResourceModule;
import bot.impl.gabes_framework.simple.SimpleModule;
import bot.impl.gabes_framework.message.SingleMessageModule;

import java.util.ArrayList;

/**
 * Connects my framework to hollandjake's API, which in turn is an API to the SeleniumAPI (?). As of now, this interface
 * is a copy of hollandjake's {@link bot.impl.orig_impl.helper.interfaces.Module} interface.
 *
 * <p>If writing module from scratch, you have to implement this interface, otherwise the API will not be able to access it
 * when iterating over loaded modules.<p>
 *
 * Each module that implements, as well as is created inside {@link Chatbot#loadModules()} method, will have it's
 * {@link #process(Message)} method called every time new message is grabbed. If a module wants to be able to react
 * to trigger regexList, they have to be "actionified" first, inside the constructor. See {@link Util#ACTIONIFY(String)}.<p>
 *
 * Created for my implementation of the modules framework. I'm hoping to add more functionality, so this interface might change.
 * All credit for making custom messenger bots possible goes to hollandjake.<p>
 *
 * For more info, take a look at the rest of my framework: {@linkplain ModuleBase}, {@linkplain SimpleModule},
 * {@linkplain SingleMessageModule}, {@linkplain MessageModule}, {@linkplain ResourceModule}.<p>
 *
 * @version 1.0
 * @since 0.19
 *
 * @author hollandjake https://hollandjake.com/,
 *                     https://github.com/hollandjake
 *
 * @author Gabe        https://github.com/gabrielwawerski
 */
public interface Module {
    /**
     * You should first call {@link #getMatch(Message)} after overriding.
     *
     * Use this snippet if your module will send only one, same message.
     * <pre>{@code
     * updateMatch(message);
     * for (String regex : regexList) {
     *     if (match.equals(regex)) {
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
     * Use {@link Util#DEACTIONIFY(String)} on your regexes when adding them to ArrayList.
     *
     * @return
     */
    public ArrayList<String> getCommands();
}
