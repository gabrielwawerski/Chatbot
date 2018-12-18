package bot.utils.gabe_modules.module_base;

import bot.core.Chatbot;
import bot.core.helper.misc.Message;
import bot.utils.gabe_modules.module_library.MessageModule;
import bot.utils.gabe_modules.module_library.ResourceModule;
import bot.utils.gabe_modules.module_library.SingleMessageModule;

/**
 * Base class for all my modules. It is the absolute minimum needed for a module to work properly. Extend from it,
 * if you need to have absolute control over your module functionality.<p>
 *
 * If you're considering writing a module that will respond with a single message when specified trigger regex/es are
 * matched, see my library class: {@link SingleMessageModule}<p>
 *
 * If you want to write a module that responds with random message if any of the trigger regex/es are detected,
 * see {@link MessageModule}<p>
 *
 * If you want to create a module that will respond with random message, taken from a .txt file, take a look at
 * {@link ResourceModule}
 *
 * @version 1.0
 * @since TODO
 * @author Gabe
 */
public abstract class ModuleBase implements Module {
    protected final Chatbot chatbot;
    /** Needs to be assigned to the latest received {@code message}'s value. After overriding {@link #process(Message)}
     * method, call {@link #updateMatch(Message)} inside it first, which takes care of the assigning for you. Although
     * this field and it's corresponding method are not necessary, they aim to make writing modules less error prone.<br>
     * If you don't want to use them, use the snippet below at the beginning of your overriden {@code process} method.
     * <pre>{@code match = getMatch(message)}</pre> See also {@link #updateMatch(Message)}, {@link #getMatch(Message)},
     * {@link #process(Message)} */
    protected String match;

    public ModuleBase(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    /**
     * Convenience method. Either make it a first call inside overriden {@link #process(Message)} method, or use the
     * snippet below:<pre>{@code match = getMatch(message)}</pre>See {@link #match} for more info.
     *
     * @param message thread latest message, passed to your module {@link #process(Message)} method
     */
    public void updateMatch(Message message) {
        match = getMatch(message);
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }
}
