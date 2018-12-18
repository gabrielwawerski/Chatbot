package bot.gabes_framework.core.libs.api;

import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.core.helper.interfaces.Util;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.message.MessageModule;
import bot.gabes_framework.resource.ResourceModule;
import bot.gabes_framework.simple.SimpleModule;
import bot.gabes_framework.message.SingleMessageModule;

import java.util.ArrayList;

/**
 * Connects my framework to hollandjake's facebook's messenger API. As of now, this interface is a copy of hollandjake's
 * {@link bot.core.helper.interfaces.Module} interface, with the exception of added javadocs.
 *
 * <p>If you want to write modules from scratch, you have to implement this interface. It currently is hooked to
 * hollandjake's API (instead of his), therefore implementing his interface or instantiating his modules will not work.
 *
 * <p>Every new message, sent to the thread that the bot is set to listen to, gets scraped by the API, which creates
 * a new {@link Message} instance from the message data. It then loops over all loaded modules, calling each module
 * {@link #process(Message)} method.
 *
 * <p>Modules can react to commands, which you can specify by subclassing one of my framework abstract classes and
 * passing commands in the constructor (see {@link SimpleModule} for more info), or by adding them to your subclass by
 * hand. See {@link ModuleBase} for more info.
 *
 * <p>Created for my modules framework. I'm hoping to add more functionality, so this interface might change.
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
