package bot.core.gabes_framework.framework;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.core.api.Module;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.resource.RandomResourceModule;
import bot.core.gabes_framework.framework.message.SingleMessageModule;
import bot.modules.gabe.point_system.submodule.PointUtils;

import java.util.*;
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
    protected static User user;
    /**
     * Needs to be assigned to latest received {@code message}. After overriding {@link Module#process(Message)} method,
     * call {@link #updateMatch(Message)} inside it, which takes care of the assigning for you. Although this field
     * and it's corresponding method are not necessary, they aim to make writing modules less error prone.
     * <p>
     * If you don't want to use these, take a look the snippet below:
     *
     * <pre>{@code match = getMatch(message)}</pre>
     * <p>
     * See also {@linkplain #updateMatch(Message)}, {@linkplain #getMatch(Message)}, {@linkplain #process(Message)}
     */
    protected String match;
    /** make sure to make your regexes static, otherwise {@code NullPointerException} will be thrown. */
    protected List<String> regexes;
    private boolean online;

    private long timeoutRelease;

    private static final long TIMEOUT = 350;

    public ModuleBase(Chatbot chatbot) {
        this.chatbot = chatbot;
        regexes = setRegexes();
        db = DBConnection.getInstance();
        timeoutRelease = getTimeoutRelease();
        setOnline();
    }

    private long getTimeoutRelease() {
        return new Date().getTime() + TIMEOUT;
    }

    protected boolean isRegex(String messageBody) {
        for (String regex : regexes) {
            if (messageBody.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Should return a list with all regexes you have added. Use {@code List.of(regex1, Utils.TO_REGEX("regex2"), "regex3")}.
     * <p>
     * As shown in example above, you don't have to add regexes as fields, nor use {@link Utils#TO_REGEX(String)} on them.
     * When not using abovementioned method, users will be able to trigger your regex without "!".
     */
    protected abstract List<String> setRegexes();

    public List<String> getRegexes() {
        return regexes;
    }

    public void updateMatch(Message message) {
        match = getMatch(message);
    }

    /**
     * Use when you don't have desired user reference. You simply pass received message and amount of points to add.
     * Also adds 1 messageCount.
     */
    protected void pushPoints(Message message, int points) { // TODO remove message argument
        user = db.getUser(message);

        if (user == User.INVALID_USER) {
            System.out.println("no points added: invalid user!");
            return;
        } else {
            user.addPoints(points);
            db.update(user);
            db.refresh(user);
            System.out.println(user.getName().substring(0, 4) + " " + PointUtils.format(points));
        }
    }

    protected void pushPoints(ArrayList<User> users, int points) {
        for (User user : users) {
            if (user == User.INVALID_USER) {
                System.out.println("no points added: invalid user!");
                continue;
            }
            user.addPoints(points);
            System.out.println(user.getName().substring(0, 4) + " " + PointUtils.format(points));
        }
        db.update(users);
    }

    protected void pushPoints(int points) {
        db.refresh(user);

        if (user == User.INVALID_USER) {
            System.out.println("no points added: invalid user!");
            return;
        } else {
            user.addPoints(points);
            db.update(user);
            db.refresh(user);
            System.out.println(user.getName() + " " + PointUtils.format(points));
        }
    }

    protected void pullPoints(int points) {
        db.refresh(user);

        if (user == User.INVALID_USER) {
            System.out.println("no points subtracted: invalid user!");
            return;
        } else {
            user.subPoints(points);
            db.update(user);
            db.refresh(user);
            System.out.println(user.getName().substring(0, 4) + " )-" + points + ")");
        }
    }

    protected void pushMessageCount() {
        db.refresh(user);

        if (user == User.INVALID_USER) {
            System.out.println("no points subtracted: invalid user!");
            return;
        } else {
            user.addMessageCount();
            db.update(user);
            System.out.println(user.getName().substring(0, 4) + "(+MSG) ");
        }
    }

    /**
     * Caution: no invalid user check here!
     */
    protected void pushMessageCount(Message message) {
        user = db.getUser(message);

        if (user == User.INVALID_USER) {
            System.out.println("no points subtracted: invalid user!");
            return;
        } else {
            user.addMessagecount(1);
            db.update(user); // todo
            db.refresh(user);
            System.out.println(user.getName().substring(0, 4) + "(MSG+)");
        }
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

    protected List<String> noRegexes() {
        return List.of("$^");
    }

    @Override
    public void echoOnline() {
        String msg = "";
        if (isOnline()) {
            msg += "ONLINE | " + getClass().getSimpleName();
        } else {
            msg += "OFFLINE| " + getClass().getSimpleName();
        }
        System.out.println(msg);
    }

    public boolean isOnline() {
        return online;
    }

    protected void setOnline() {
        online = true;
    }

    protected void setOffline() {
        online = false;
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
