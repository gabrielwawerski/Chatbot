package bot.core.gabes_framework.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.api.Module;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.message.MessageModule;
import bot.core.gabes_framework.util.resource.RandomResourceModule;
import bot.core.gabes_framework.util.message.SingleMessageModule;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for all my modules. It is the absolute minimum needed for a module to work properly. Extend from it,
 * if you need to have an absolute control over your module functionality.<p>
 *
 * If you're considering writing a module that will respond with a single message when specified trigger regex/es are
 * matched, see my library class: {@link SingleMessageModule}<p>
 *
 * If you want to write a module that responds with random message if any of the trigger regex/es are detected,
 * see {@link MessageModule}<p>
 *
 * If you want to createUser a module that will respond with random message, taken from a .txt file, take a look at
 * {@link RandomResourceModule}
 *
 * @version 1.0
 * @since 0.29
 */
public abstract class ModuleBase implements Module {
    protected final Chatbot chatbot;
    /** Needs to be assigned to the latest received {@code message}'s value. After overriding {@link Module#process(Message)}
     * method, call {@link #updateMatch(Message)} inside it first, which takes care of the assigning for you. Although
     * this field and it's corresponding method are not necessary, they aim to make writing modules less error prone.<br>
     * If you don't want to use them, use the snippet below at the beginning of your overriden {@code process} method.
     * <pre>{@code match = getMatch(message)}</pre> See also {@link #updateMatch(Message)}, {@link #getMatch(Message)},
     * {@link #process(Message)} */
    protected String match;

    protected boolean online;

    public ModuleBase(Chatbot chatbot) {
        this.chatbot = chatbot;
        setOnline();
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public void echoOnline() {
        String msg = "";
        if (online) {
            msg += "ONLINE | " + getClass().getSimpleName();
        } else {
            msg += "OFFLINE| " + getClass().getSimpleName();
        }
        System.out.println(msg);
    }

    protected void setStatus(boolean online) {
        this.online = online;
    }

    protected void setOnline() {
        online = true;
    }

    protected void setOffline() {
        online = false;
    }

    /**
     * Convenience method. Either make it a first call inside overriden {@link #process(Message)} method, or use the
     * snippet below:<pre>{@code match = getMatch(message)}</pre><p><p>
     *
     * <b>WARNING!<br></b>
     * Make sure that you have <b>implemented</b> {@link Module#getMatch(Message)} <b>before</b> calling this method!
     *
     * @param message thread latest message, passed to your module in {@link #process(Message)}
     * @see #match
     * @see Module#getMatch(Message)
     * @see #process(Message)
     */
    public void updateMatch(Message message) {
        match = getMatch(message);
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }

    protected boolean isOr(String... regexes) {
        for (String regex : regexes) {
            if (match.equals(regex)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isOr(String REGEX, String OTHER) {
        return match.equals(REGEX) || match.equals(OTHER);
    }

    protected boolean is(String REGEX) {
        return match.equals(REGEX);
    }

    protected boolean is(List<String> REGEXES) {
        for (String regex : REGEXES) {
            if (match.equals(regex)) {
                return true;
            }
        }
        return false;
    }

    protected boolean found(Message message) {
        Matcher matcher = Pattern.compile(match).matcher(message.getMessage());
        return matcher.find();
    }

    protected Matcher getMatcher(Message message) {
        return Pattern.compile(match).matcher(message.getMessage());
    }

    protected String findMatch(Message message, List<String> commands) {
        String messageBody = message.getMessage();

        for (String command : commands) {
            if (messageBody.matches(command)) {
                return command;
            }
        }
        return "";
    }

    protected String findMatch(Message message, String... commands) {
        String messageBody = message.getMessage();

        for (String command : commands) {
            if (messageBody.matches(command)) {
                return command;
            }
        }
        return "";
    }
}
