package bot.core.gabes_framework.framework;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.core.api.Module;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.resource.RandomResourceModule;
import bot.core.gabes_framework.framework.message.SingleMessageModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for all my modules. It is the absolute minimum needed for a module to work properly. Extend from it,
 * if you need to have an absolute control over your module functionality.
 * <p>
 * If you're considering writing a module that will respond with a single message when specified trigger regex/es are
 * matched, see {@link SingleMessageModule}
 * <p>
 * If you want to create a module that will respond with random message, taken from a .txt file, take a look at
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
    /** make sure to make your regexes static, otherwise null pointer exception will be thrown. */
    protected List<String> regexes;

    private boolean online;

    public ModuleBase(Chatbot chatbot) {
        this.chatbot = chatbot;
        regexes = setRegexes();
        db = DBConnection.getInstance();
        setOnline();
    }

    /**
     * Should return a list with all regexes you have added. Use {@code List.of("regex1", "regex2")}.
     */
    protected abstract List<String> setRegexes();

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

    public void updateMatch(Message message) {
        match = getMatch(message);
    }

    /**
     * Use when you don't have desired user reference. You simply pass received message and amount of points to add.
     * Also adds 1 messageCount.
     */
    // TODO remove adding message count here!
    protected void addPoints(Message message, int points) {
        User user;
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

    protected boolean is(String regex) {
        return match.equalsIgnoreCase(regex);
    }

    protected boolean is(String... regexes) {
        return findRegex(Arrays.asList(regexes));
    }

    protected boolean is(List<String> regexes) {
        return findRegex(regexes);
    }

    protected boolean isOr(String regex, String other) {
        return match.equalsIgnoreCase(regex) || match.equalsIgnoreCase(other);
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

    protected List<String> getMappedRegexes(String... regexes) {
        return Arrays.stream(regexes).map(Utils::TO_REGEX).collect(Collectors.toList());
    }

    protected void setOnline(boolean online) {
        this.online = online;
    }

    protected void setOnline() {
        online = true;
    }

    protected void setOffline() {
        online = false;
    }

    private boolean findRegex(List<String> regexes) {
        for (String regex : regexes) {
            if (match.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }

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
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        for (String regex : regexes) {
            if (messageBody.matches(regex)) {
                return regex;
            }
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(regexes);
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }
}
