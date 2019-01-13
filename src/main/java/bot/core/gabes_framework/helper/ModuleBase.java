package bot.core.gabes_framework.helper;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.core.api.Module;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.helper.message.MessageModule;
import bot.core.gabes_framework.helper.resource.RandomResourceModule;
import bot.core.gabes_framework.helper.message.SingleMessageModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Base class for all my modules. It is the absolute minimum needed for a module to work properly. Extend from it,
 * if you need to have an absolute control over your module functionality.
 * <p>
 * If you're considering writing a module that will respond with a single message when specified trigger regex/es are
 * matched, see my library class: {@link SingleMessageModule}
 * <p>
 * If you want to write a module that responds with random message if any of the trigger regex/es are detected,
 * see {@link MessageModule}
 * <p>
 * If you want to createUser a module that will respond with random message taken from a .txt file, take a look at
 * {@link RandomResourceModule}
 *
 * @version 1.0
 * @since 0.29
 */
public abstract class ModuleBase implements Module {
    protected final Chatbot chatbot;
    protected DBConnection db;
    /**
     * Needs to be assigned to latest received {@code message}. After overriding {@link Module#process(Message)} method,
     * call {@link #updateMatch(Message)} inside it first, which takes care of the assigning for you. Although this field
     * and it's corresponding method are not necessary, they aim to make writing modules less error prone.
     * <p>
     * If you don't want to use these, take a look the snippet below at the beginning of your overriden {@code process}
     * method.
     *
     * <pre>{@code match = getMatch(message)}</pre>
     *
     * See also {@linkplain #updateMatch(Message)}, {@linkplain #getMatch(Message)}, {@linkplain #process(Message)}
     */
    protected String match;
    protected List<String> regexes;

    private boolean online;

    public ModuleBase(Chatbot chatbot) {
        this.chatbot = chatbot;
        regexes = setRegexes();
        setOnline();
        db = DBConnection.getInstance();
    }

    /**
     * Should return a list with all regexes you have added. Use {@code List.of("regex1", "regex2")}.
     */
    protected abstract List<String> setRegexes();

    protected List<String> getMappedRegexes(String... regexes) {
        return Arrays.stream(regexes).map(Utils::TO_REGEX).collect(Collectors.toList());
    }

    public List<String> getRegexes() {
        return regexes;
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

    /**
     * Use when you don't have desired user reference. You simply pass received message and amount of points to add.
     * Also adds 1 messageCount.
     */
    // TODO remove adding message count here!
    protected void addPoints(Message message, int points) {
        User user = null;
        if ((user = db.getUser(message)) == null) {
            throw new IllegalArgumentException("User not found. FIXME!");
        } else {
            user.addPoints(points);
            user.addMessagecount(1);
            db.update(user);
            System.out.println(user.getName() + "(+" + points + ")");
            System.out.print(user.getName().substring(0, 5) + "(MSG+)" + "\n");
        }
    }

    protected void processMessage(Message message) {
        // TODO
    }

    protected void processMessage(Message message, int points) {
        // TODO
    }

    protected void addMessageCount(User user) {
        user.addMessagecount(1);
        db.update(user);
        System.out.print(user.getName().substring(0, 5) + "(MSG+)" + "\n");
    }

    protected void update(User user) {
        db.update(user);
    }

    /**
     * Use when you have a reference to desired user. Also adds 1 messageCount.
     */
    protected void addPoints(User user, int points) {
        user.addPoints(points);
        user.addMessagecount(1);
        db.update(user);
        System.out.println(user.getName() + "(+" + points + ")");
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

    protected boolean is(String regex) {
        return match.equalsIgnoreCase(regex);
    }

    /**
     * Returns true if message contains any regex from {@link #regexes}. See {@link #setRegexes()} for more info.
     */
    protected boolean isRegex(Message message) {
        return isMsgRegex(message, regexes);
    }

    /**
     * Returns true if match equals one of the regexes you provided in {@link #setRegexes()}.
     */
    protected boolean isRegex() {
        for (String regex : regexes) {
            if (match.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if message matches any regex from 2nd argument.
     */
    protected boolean isRegex(Message message, List<String> regexes) {
        return isMsgRegex(message, regexes);
    }

    protected boolean is(List<String> REGEXES) {
        for (String regex : REGEXES) {
            if (match.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }

    protected boolean is(String... regexes) {
        for (String regex : regexes) {
            if (match.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isOr(String regex, String other) {
        return match.equalsIgnoreCase(regex) || match.equalsIgnoreCase(other);
    }

    protected String findMatch(Message message) {
        return getRegex(message, regexes);
    }

    /**
     * Attempts to match given message with provided regex list. If match is found, simply returns it (it's value).
     * Returns an empty String {@code ""} otherwise.
     */
    protected String findMatch(Message message, List<String> regexes) {
        return getRegex(message, regexes);
    }

    protected String findMatch(Message message, String... regexes) {
        return getRegex(message, Arrays.asList(regexes));
    }

    protected boolean patternFound(Message message) {
        Matcher matcher = Pattern.compile(match).matcher(message.getMessage());
        return matcher.find();
    }

    private String getRegex(Message message, List<String> regexes) {
        String messageBody = message.getMessage();

        for (String regex : regexes) {
            if (messageBody.matches(regex)) {
                return regex;
            }
        }
        return "";
    }

    @SuppressWarnings("all")
    private boolean isMsgRegex(Message message) {
        String messageBody = message.getMessage();

        for (String regex : regexes) {
            if (messageBody.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("all")
    private boolean isMsgRegex(Message message, List<String> regexes) {
        String messageBody = message.getMessage();

        for (String regex : regexes) {
            if (messageBody.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, regexes);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(regexes);
    }
}
