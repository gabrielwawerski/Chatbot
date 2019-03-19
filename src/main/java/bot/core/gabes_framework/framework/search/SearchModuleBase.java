package bot.core.gabes_framework.framework.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.simple.SimpleSearchModule;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.gabes_framework.core.util.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Base class for modules which will send search result url to chat, with search query given by the user. For basic
 * functionality, see {@link SimpleSearchModule}.
 * <br><br>
 * <b>Tips on how to write regular expressions:</b>
 * <ul>
 *     <li>if you want your module to format and send urls properly, make sure to add " {@code (.*)}" regular expression
 *         after your desired trigger command (notice the single empty space). See {@link #ANY_REGEX} for more info</li>
 *     <li>it is best to assign regexes with {@link Utils#TO_REGEX(String)}</li>
 *     <li>if your module needs a fixed command, simply do {@code Utils.TO_REGEX("your keyword");}</li>
 * </ul>
 * <p>
 * Example:
 * <pre>
 * {@code private final String EXAMPLE_ANY_REGEX   = toRegex("command (.*)");}
 * {@code private final String EXAMPLE_OTHER_REGEX = Utils.TO_REGEX("command");}
 * {@code private final String EXAMPLE_FIXED_REGEX = toRegex("info");}
 * </pre>
 *
 * @version 1.0
 * @since 0.30
 * @see SimpleSearchModule
 */
public abstract class SearchModuleBase extends ModuleBase {
    protected Matcher matcher;
    private String userMessage;

    // TODO move these to SimpleSearchModule
    private String SEARCH_URL;
    private String WORD_SEPARATOR;

    /** Common separator, used by most websites to separate individual words in their search result URL */
    protected static final String DEFAULT_WORD_SEPARATOR = "+";
    /** {@code (.*)} regex, means "any text" */
    protected static final String ANY_REGEX = "(.*)";

    public SearchModuleBase(Chatbot chatbot) {
        super(chatbot);
        SEARCH_URL = getSearchUrl();
        WORD_SEPARATOR = getSeparator();
    }

    /**
     * Should return website's entire search url, up to (including) the last character before actual search query
     * <p>
     * Simple Google search URL can look like this:
     * <p>
     * {@code google.com/search?q=your+query}
     * <p>
     * Here, {@link #SEARCH_URL} will be: {@code google.com/search?q=}
     */
    protected abstract String getSearchUrl();

    /**
     * Override if a website has different query separator than {@link #DEFAULT_WORD_SEPARATOR}. You should simply return
     * your separator here.
     *
     * @see #DEFAULT_WORD_SEPARATOR
     */
    protected String getSeparator() {
        return DEFAULT_WORD_SEPARATOR;
    }

    /**
     * Returns a complete URL with properly formatted user query. Value returned is ready to be sent to thread.
     *
     * @return formatted message ready to be sent
     */
    protected String getFinalMessage() {
        return SEARCH_URL + toQuery();
    }

    protected void setUserMessage(String message) {
        userMessage = message;
    }

    protected boolean matchFound() {
        if (matcher == null) { // && !matcher.group(1).isEmpty()?
            System.out.println(getClass().getSimpleName() + "'s matcher is null!");
            return false;
        } else {
            return matcher.find();
        }
    }

    /**
     * Helper method. Sets {@link #matcher} to user query. Use when providing regexes inside the class, but if doing so,
     * remember to also override {@link #getMatch(Message)}, {@link #getCommands()} methods. Use after
     * {@link #updateMatch(Message)} in overriden {@link #process(Message)} method.
     *
     * @param messageBody {@linkplain Message} instance. See {@link Message#getMessage()} for more info.
     */
    // TODO rewrite javadoc
    protected void updateMatcher(String messageBody) {
        this.matcher = Pattern.compile(match).matcher(messageBody);
    }

    /**
     * Applies regular expression to each list element via {@link #addAnyToQuery(String)} and then "actionifies" them,
     * with {@link bot.core.hollandjake_api.helper.interfaces.Util#ACTIONIFY(String)}.
     */
    protected List<String> getMappedSearchRegexes(List<String> regexes) {
        return regexes.stream()
                .map(this::addAnyToQuery)
                .map(Utils::TO_REGEX)
                .collect(Collectors.toList());
    }

    /**
     * Replaces every space with {@link #WORD_SEPARATOR}.
     */
    private String toQuery() {
        return getUserMessage().replaceAll("\\s+", WORD_SEPARATOR);
    }

    /**
     * Returns only the actual user message (omits command).
     */
    protected String getUserMessage() {
        if (userMessage == null) {
            System.out.println("user message null");
            return matcher.group(1); // not null-safe!!!!
        } else {
            System.out.println("not null");
            return userMessage;
        }
    }

    /**
     * Returns provided regex with {@link #ANY_REGEX} appended to it.
     */
    private String addAnyToQuery(String regex) {
        return regex + " " + ANY_REGEX;
    }
}
