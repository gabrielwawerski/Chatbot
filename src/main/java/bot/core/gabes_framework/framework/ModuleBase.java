package bot.core.gabes_framework.framework;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.core.api.Module;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.resource.RandomResourceModule;
import bot.core.gabes_framework.framework.message.SingleMessageModule;
import bot.modules.gabe.point_system.submodule.PointUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Well rounded base for modules. Provides wrapped database and regex handling, as well as utility classes for
 * databae and regex related operations.
 * <p>
 * If you're considering writing a module that will respond with a single message when your regex/es is/are matched,
 * see {@link SingleMessageModule}
 * <p>
 * If you want to create a module that will respond with random message, taken from a .txt file, take a look at
 * {@link RandomResourceModule}
 *
 * @version 1.1
 * @since 0.29
 *
 * @see #match
 * @see #regexes
 * @see #setRegexes
 * @see Utils#TO_REGEX(String)
 * @see Utils#TO_REGEX(List)
 *
 * @see SingleMessageModule
 * @see RandomResourceModule
 *
 * @see #NO_REGEX()
 */
// TODO make separate classes for modules using datatabase connections
// TODO add getMatch() calls to methods that receive Message instance
public abstract class ModuleBase implements Module {
    protected final Chatbot chatbot;
    protected DBConnection db;
    protected static User user;
    /**
     * For a basic module, typically should be assigned to the latest received {@link Message}. After overriding
     * {@link Module#process(Message)}, call {@link #updateMatch(Message)} inside it, which takes care of the dirty
     * work for you.
     * <p>
     * Although this field and it's corresponding method are not necessary, they aim to make writing
     * modules easier and less error prone.
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

    //region private fields
    private boolean online;
    private long timeoutRelease;
    private static final long TIMEOUT = 350;
    //endregion

    public ModuleBase(Chatbot chatbot) {
        this.chatbot = chatbot;
        regexes = setRegexes();
        db = DBConnection.getInstance();
        timeoutRelease = getTimeoutRelease();
        setOnline();
    }

    //region regex util
    /**
     * Regex utility. Use, when your regex values do not hold a lot of power over your logic execution, or same
     * logic is executed, regardless of the regex.
     */
    protected List<String> getMappedRegexes(String... regexes) {
        return Arrays.stream(regexes).map(Utils::TO_REGEX).collect(Collectors.toList());
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
     * Returns true if match equals any of the regexes.
     *
     * @see #setRegexes()
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
     * Equals "none"
     * <p>
     * If needed, use as a return value in overriden {@link #setRegexes()}
     *
     * @return regex representing anti-pattern
     */
    protected List<String> NO_REGEX() {
        return List.of("$^");
    }
    //endregion

    //region databse util
    protected void pushPoints(int points) {
        db.refresh(user);

        if (user == User.EMPTY_USER) {
            System.out.println("no points added: invalid user!");
        } else {
            user.addPoints(points);
            db.update(user);
            db.refresh(user);
            System.out.println(user.getName() + " " + PointUtils.format(points));
        }
    }

    /**
     * Should be used when subtracting points from message sender. Handles updating database entry for you.
     */
    protected void pullPoints(int points) {
        db.refresh(user);

        if (user == User.EMPTY_USER) {
            System.out.println("no points subtracted: invalid user!");
        } else {
            user.subPoints(points);
            db.update(user);
            db.refresh(user);
            System.out.println(user.getName().substring(0, 4) + " )-" + points + ")");
        }
    }

    /**
     * Use when you don't have desired user reference. You simply pass received message and amount of points to add.
     * Also adds 1 messageCount.
     */
    protected void pushPoints(Message message, int points) { // TODO remove message argument
        user = db.getUser(message);

        if (user == User.EMPTY_USER) {
            System.out.println("no points added: invalid user!");
        } else {
            user.addPoints(points);
            db.update(user);
            db.refresh(user);
            System.out.println(user.getName().substring(0, 4) + " " + PointUtils.format(points));
        }
    }

    protected void pushPoints(ArrayList<User> users, int points) {
        for (User user : users) {
            if (user == User.EMPTY_USER) {
                System.out.println("no points added: invalid user!");
                continue;
            }
            user.addPoints(points);
            System.out.println(user.getName().substring(0, 4) + " " + PointUtils.format(points));
        }
        db.update(users);
    }

    protected void pushMessageCount() {
        db.refresh(user);

        if (user == User.EMPTY_USER) {
            System.out.println("no points subtracted: invalid user!");
        } else {
            user.addMessagecount(1);
            db.update(user); // todo
            db.refresh(user);
            System.out.println(user.getName().substring(0, 4) + "(MSG+)");
        }
    }
    //endregion

    //region public private methods

    public List<String> getRegexes() {
        return regexes;
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
    //endregion

    //region private methods
    private boolean findRegex(List<String> regexes) {
        for (String regex : regexes) {
            if (match.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }
    //endregion

    /**
     * Should return a list with all regexes you're using. // TODO throwing exception, when regex already exists
     * Use {@code List.of(regex1, Utils.TO_REGEX("regex2"), "regex3")}.
     * <p>
     * You don't have to add regexes as fields, nor use {@link Utils#TO_REGEX} on them. If any sentence
     * below describes your module, I encourage you to keep reading.
     * <p>
     * 1. It Has exactly one regex.
     * 2. Does same thing for all regexes
     *
     *
     * <p>
     * When not using {@link Utils#TO_REGEX}, your regexes will triger even when no  "!" is present!
     * <p>
     * Also, if you want to add logic each time new message is received, {@link #NO_REGEX()}.
     */
    protected abstract List<String> setRegexes();

    protected boolean isRegex(String messageBody) {
        for (String regex : regexes) {
            if (messageBody.equalsIgnoreCase(regex)) {
                return true;
            }
        }
        return false;
    }

    public void updateMatch(Message message) {
        match = getMatch(message);
    }

    @Deprecated
    private long getTimeoutRelease() {
        return new Date().getTime() + TIMEOUT;
    }
}
