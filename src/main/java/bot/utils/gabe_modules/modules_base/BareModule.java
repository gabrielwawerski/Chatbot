package bot.utils.gabe_modules.modules_base;

import bot.Chatbot;
import bot.utils.bot.helper_class.Message;

import java.util.ArrayList;

/**
 * Extend from this class if you want to have absolute control over how your
 *
 * @author Gabe
 */
public abstract class BareModule implements Module {
    protected final Chatbot chatbot;
    /** Needs to be assigned to the latest received {@code message}'s value. After overriding {@link #process(Message)}
     * method, call {@link #updateMatch(Message)} inside it first, which takes care of the assigning for you. Although
     * this field and it's corresponding method are not necessary, they aim to make writing modules easier.<br>
     * If you don't want to use them, use the snippet below at the beginning of your overriden {@code process} method.
     * <pre>{@code match = getMatch(message)}</pre> */
    protected String match;

    public BareModule(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    /**
     * Convenience method. Either make it a first call inside overriden {@link #process(Message)} method, or use the
     * snippet below:<pre>{@code match = getMatch(message)}</pre> whic. See {@link #match} for more info.
     *
     * @param message latest thread message, passed to your module, in {@link #process(Message)} method
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
