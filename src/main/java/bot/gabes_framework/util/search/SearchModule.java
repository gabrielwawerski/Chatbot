package bot.gabes_framework.util.search;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import com.google.errorprone.annotations.ForOverride;

import java.util.List;

/**
 * kind of modular search module, WIP
 */
public abstract class SearchModule extends SearchModuleBase {
    private String messageBody = "";

    /**
     * Use this, if ...
     * Be sure to call {@link #getMappedRegexList(List)} before doing anything else, otherwise your regexes won't trigger
     * the module logic!<p>
     *
     * Default separator is a plus sign "+". If needed, you can change it by overriding {@link #setSeparator()}
     */
    public SearchModule(Chatbot chatbot, List<String> regexList) {
        super(chatbot);
        getMappedRegexList(regexList);
    }

    public SearchModule(Chatbot chatbot) {
        super(chatbot);
    }

    /**
     * Tries to match user's message with each {@link #} item. If a match is found, instantiates {@link #matcher}
     * with
     * .You can then use {@link #updateMatcher(String)}, which tries
     * to match the message with your regexes
     */
    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        messageBody = message.getMessage();

        if (!execBeforeRegexLoop(message)) {
            return false;
        }
        execBeforeRegexLoop_(message);

        for (String regex : getRegexList()) {
            if (match.equals(regex)) {
                updateMatcher(messageBody);

                if (isMatchFound()) {
                    if (!execIfMatchFound(message)) {
                        return false;
                    }
                    execIfMatchFound_(message);
                    return true;

                } else {
                    System.out.println(getClass().getSimpleName() + " did not find any match.");
                    chatbot.sendMessage("Coś poszło nie tak. Jeszcze się trzymam...");
                    return false;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    protected abstract List<String> getRegexList();

    /**
     * Called before the regex matching loop. Override, if you need the possibility to stop any further code execution
     * in your module. If so, return {@code true} to finish (tells the API that message has been successfully processed)<p>
     *
     * Example: If user's message is longer than it can be, return false to finish message processing by your module.
     *
     * @param message ignore if not needed
     * @return false, if you need to stop further module's code execution, true is essentialy ignored - return it,
     *         if everything is OK
     */
    @ForOverride
    protected boolean execBeforeRegexLoop(Message message) {
        return true;
    }

    /**
     * Called before the regex matching loop. Override, if needed.<p>
     *
     * @param message ignore if not needed
     */
    @ForOverride
    protected void execBeforeRegexLoop_(Message message) {
    }

    /**
     * Called inside regex matching loop, if {@link #isMatchFound()} returns true. Override, if you need the possibility
     * to stop any further code execution in your module. If so, return {@Code true} to finish (tells the API that
     * message has been successfully processed).
     *
     * Example: If user's message is longer than it can be, return false to finish message processing by your module.
     *
     * @param message
     * @return
     */
    @ForOverride
    protected boolean execIfMatchFound(Message message) {
        return true;
    }

    /**
     * @param message ignore if not needed
     * @return false, if you need to stop further module's code execution, true is essentialy ignored - return it,
     *         if everything is OK
     */
    @ForOverride
    protected void execIfMatchFound_(Message message) {
        // class's default logic - simply sends the query url
        chatbot.sendMessage(getFinalMessage(messageBody));
    }
}
