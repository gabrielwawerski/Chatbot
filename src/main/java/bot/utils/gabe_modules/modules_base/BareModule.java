package bot.utils.gabe_modules.modules_base;

import bot.Chatbot;
import bot.utils.bot.helper_class.Message;

import java.util.ArrayList;

/**
 * Extend from this class if you want to set up regexes yourself.
 *
 * @author Gabe
 */
public abstract class BareModule implements Module {
    protected final Chatbot chatbot;
    /** Must be called when overriding {@link #process(Message)} method */
    protected String match;

    public BareModule(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    /**
     * Call this method as a first instruction inside overriden {@link Module#process(Message)} method.
     *
     * @param message
     */
    @Override
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
