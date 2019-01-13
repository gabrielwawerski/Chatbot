package bot.core.gabes_framework.core.api;

import bot.core.gabes_framework.core.util.Utils;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.helper.ModuleBase;
import bot.core.gabes_framework.helper.message.MessageModule;
import bot.core.gabes_framework.helper.resource.RandomResourceModule;
import bot.core.gabes_framework.helper.simple.SimpleModule;
import bot.core.gabes_framework.helper.message.SingleMessageModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Connects my framework to hollandjake's facebook's messenger API. Originally taken from hollandjake's implementation:
 * {@link bot.core.hollandjake_api.helper.interfaces.Module}.
 *
 * <p>If you want to write modules from scratch, you have to implement this interface. It currently is hooked to
 * hollandjake's API (instead of his), therefore implementing his interface or instantiating his modules will not work.
 *
 * <p>Every message sent to thread that the bot is listening to gets scraped by the API, which creates a new
 * {@link Message} instance from message data. It then loops over all loaded modules, calling each module
 * {@link #process(Message)}.
 *
 * <p>Modules can react to commands, which you can specify by subclassing one of my abstract classes and passing commands
 * in the constructor (see {@link SimpleModule} for more info), or by adding them to your subclass by
 * hand. See {@link ModuleBase} for more info.
 *
 * <p>Created for my module framework. I'm hoping to add more functionality, so this interface might change.
 * All credit for making custom messenger bots possible goes to hollandjake.
 *
 * <p>For more information, take a look at these: {@linkplain ModuleBase}, {@linkplain SimpleModule},
 * {@linkplain SingleMessageModule}, {@linkplain MessageModule}, {@linkplain RandomResourceModule}.
 *
 * <p><p>v1.1
 * <br>- added {@link #isOnline()} and {@link #echoOnline()} methods.
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

    /**
     * @return true if module has been <b>succesfully loaded</b>, false otherwise.
     * @since 0.3014
     */
    public boolean isOnline();

    public void echoOnline();

    /**
     * Should make an attempt to match your regex/es against received message.
     *
     * <p>See my helper methods: {@link ModuleBase#findMatch(Message, List)},
     * {@link ModuleBase#findMatch(Message, String...)}
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
