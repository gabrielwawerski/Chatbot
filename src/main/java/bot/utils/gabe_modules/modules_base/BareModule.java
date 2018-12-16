package bot.utils.gabe_modules.modules_base;

import bot.Chatbot;
import bot.utils.bot.helper_class.Message;

import java.util.ArrayList;

/**
 * Extend from this class if you want to set up regexes yourself.
 *
 * @author hollandjake, Gabe
 */
public abstract class BareModule implements Module {
    protected final Chatbot chatbot;
    /** Needs to be assigned to the latest received message String value. Use {@link #updateMatch(Message)} to have it
     * assigned for you automatically, or put <pre>{@code match = getMatch(message)}</pre> as a first instruction inside
     * your overriden {@link #process(Message)} method. */
    protected String match;

    public BareModule(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    /**
     * Helper method. Either call it as a first instruction when overriding {@link #process(Message)}, or call:<br>
     * <pre>
     * {@code match = getMatch(message)}
     * </pre>
     * yourself, after overriding {@link #process(Message)}.
     *
     * @param message received message
     */
    public void updateMatch(Message message) {
        match = getMatch(message);
    }

    /**
     *
     *
     * @param message filename to append path to
     * @return appended path
     */
    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }

    /**
     * Add your commands to the
     *
     * @return commands you have created in your class.
     */
    @Override
    public abstract ArrayList<String> getCommands();
}
