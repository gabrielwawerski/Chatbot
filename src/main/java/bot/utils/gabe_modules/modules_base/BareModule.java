package bot.utils.gabe_modules.modules_base;

import bot.Chatbot;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.gabe_modules.interfaces.Module;
import bot.utils.gabe_modules.util.MessageModule;
import bot.utils.gabe_modules.util.ResourceModule;
import bot.utils.gabe_modules.util.SingleMessageModule;

/**
 * Base class for all my modules. It is the absolute minimum needed for a module to work properly. Extend from it,
 * if you need to have absolute control over your module's functionality.<br>
 *
 * If you're considering writing a module that will respond when specified trigger commands are detected, with only
 * one message, see my "helper" class: {@link SingleMessageModule}<br>
 * If you want to write a module that responds with random message if any of the trigger commands are detected,
 * see {@link MessageModule}<br>
 * If you wanted to create a module that will respond with random message, taken from .txt file, take a look at
 * {@link ResourceModule}
 *
 * @author Gabe
 */
public abstract class BareModule implements Module {
    protected final Chatbot chatbot;
    /** Needs to be assigned to the latest received {@code message}'s value. After overriding {@link #process(Message)}
     * method, call {@link #updateMatch(Message)} inside it first, which takes care of the assigning for you. Although
     * this field and it's corresponding method are not necessary, they aim to make writing modules less error prone.<br>
     * If you don't want to use them, use the snippet below at the beginning of your overriden {@code process} method.
     * <pre>{@code match = getMatch(message)}</pre> See also {@link #updateMatch(Message)}, {@link #getMatch(Message)},
     * {@link #process(Message)} */
    protected String match;

    /**
     *
     *
     * @param chatbot
     */
    public BareModule(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    /**
     * Convenience method. Either make it a first call inside overriden {@link #process(Message)} method, or use the
     * snippet below:<pre>{@code match = getMatch(message)}</pre>See {@link #match} for more info.
     *
     * @param message thread's latest message, passed to your module, to {@link #process(Message)} method
     */
    public void updateMatch(Message message) {
        match = getMatch(message);
    }
}
