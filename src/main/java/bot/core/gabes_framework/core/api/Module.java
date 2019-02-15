package bot.core.gabes_framework.core.api;

import bot.core.gabes_framework.core.util.Utils;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.gabes_framework.framework.resource.RandomResourceModule;
import bot.core.gabes_framework.framework.message.SingleMessageModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Connects my framework to hollandjake's messenger API. Originally taken from hollandjake's implementation:
 * {@link bot.core.hollandjake_api.helper.interfaces.Module}.
 * <p>
 * If you want to write modules from scratch, you have to implement this interface. It currently is hooked to
 * hollandjake's API, therefore implementing his interface or instantiating his modules will not work.
 * <p>
 * Every message sent to thread that the bot is listening to, gets scraped by the API, which attempts to create
 * {@link Message} instance from message data. It then loops over all loaded modules, calling each module
 * {@link #process(Message)}.
 * <p>
 * Modules can react to commands, which you can specify in a simple way - by extending my framework's main class,
 * {@link ModuleBase} and passsing their regexes, from overriden {@link ModuleBase#setRegexes()} method. For more
 * info on regexes, see {@link Utils#TO_REGEX(String)} // TODO
 * <p>
 * Written for my module framework - all credit for making custom messenger bots possible goes to hollandjake.
 * <p>
 * For more info, see: {@linkplain ModuleBase}
 * <p><p>
 * v1.1<br>
 *     - added: {@link #isOnline()} {@link #echoOnline()}
 *
 * @see ModuleBase
 * @see SingleMessageModule
 * @see RandomResourceModule
 *
 * @version 1.1
 * @since 0.19
 * @author hollandjake https://hollandjake.com/,
 *                     https://github.com/hollandjake
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

    public boolean isOnline();

    public void echoOnline();

    /**
     * Should make an attempt to match your regex/es against received message.
     *
     * @param message message received in {@link #process(Message)}
     * @return the regex found, otherwise an empty String: {@code ""}
     */
    public String getMatch(Message message);

    public String appendModulePath(String message);

    /**
     * Use {@link Utils#TO_COMMAND(String)} on your regexes when adding them to ArrayList.
     *
     * <p>You can also use one of my helper methods: {@link Utils#getCommands(List)}, {@link Utils#getCommands(String...)}
     */
    public ArrayList<String> getCommands();
}
