package bot.impl.gabes_framework.search;

import bot.Chatbot;
import bot.impl.orig_impl.helper.interfaces.Util;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.gabes_framework.core.ModuleBase;
import bot.impl.gabes_framework.core.libs.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Base class for modules which will send search result url to chat, with search query given by the user. For basic
 * functionality, see {@link SimpleSearchModule}, {@link SearchModule}. Be sure to call {@link #makeRegex(String)}
 * on each regex you add.
 *
 * <p>Example:
 * <pre>
 * {@code private static final String EXAMPLE_ANY_REGEX    = makeRegex("command (.*)");}
 * {@code private static final String EXAMPLE_PRESET_REGEX = makeRegex("command|help);}</pre>
 *
 * <p><p><b>Tips on how to write custom regular expression:</b>
 * <ul>
 *     <li>if you want your module to format and send urls properly, make sure to add " {@code (.*)}" regular expression
 *         (notice the single empty space between module's main command and the regex) after your desired trigger
 *         command. See {@link #ANY_REGEX} for more info</li>
 *     <li>it's best to assign regexes with a call to {@link #makeRegex(String)}.</li>
 *     <li>if you want to add a fixed command and you want it to be prefixed with module's signature command, write
 *         it down as shown in second example above (notice that there is no gap between the two commands)</li>
 *     <li>single command </li>
 * </ul>
 * @version 1.0
 * @since 0.30
 * @see SearchModule
 * @see SimpleSearchModule
 * @see #makeRegex(String)
 */
public abstract class SearchModuleBase extends ModuleBase {
    protected Matcher matcher;

    protected final String SEARCH_URL;
    protected final String SEPARATOR;

    /** Common separator, used by most websites to separate individual words in their search result URL */
    protected static final String DEFAULT_SEPARATOR = "+";
    /** {@code (.*)} regex, means "any text" */
    protected static final String ANY_REGEX = " (.*)";

    public SearchModuleBase(Chatbot chatbot) {
        super(chatbot);
        SEARCH_URL = setSearchUrl();
        SEPARATOR = setSeparator();
    }

    /**
     * Should return website's entire search url, up to (including) the last character before actual search query.<p>
     *
     * Example:<br>
     * Simple google search url can look like this: {@code google.com/search?q=your+search+message}
     * Here, your {@link #SEARCH_URL} {@code = google.com/search?q=}
     */
    protected abstract String setSearchUrl();

    /**
     * Should simply return your SEPARATOR. See {@link SearchModule#DEFAULT_SEPARATOR} for more info.
     */
    @SuppressWarnings("all")
    protected abstract String setSeparator();

    /**
     * Returns a complete URL with properly formatted user query. Value returned is ready to be sent to thread.
     *
     * @return formatted message ready to be sent
     */
    protected String getFinalMessage(String messageBody) {
        return SEARCH_URL + toQuery(messageBody);
    }

    protected boolean isMatchFound() {
        if (matcher == null) {
            System.out.println(getClass().getSimpleName() + "'s matcher is null!");
            return false;
        } else {
            return matcher.find(); // && !matcher.group(1).isEmpty()?
        }
    }

    /**
     * Helper method. Sets {@link #matcher} to user query. Use when providing regexes inside the class, but if doing so,
     * remember to also override {@link #getMatch(Message)}, {@link #getCommands()} methods. Use after
     * {@link #updateMatch(Message)} in overriden {@link #process(Message)} method.
     *
     * @param messageBody received Message contents. See {@link Message#getMessage()}
     */
    protected void updateMatcher(String messageBody) {
        this.matcher = Pattern.compile(match).matcher(messageBody);
    }

    /**
     * Replaces every space ( ) with {@link #SEPARATOR}.
     *
     * @param userMessage user message to be formatted
     * @return formatted user message, ready to be
     */
    protected String toQuery(String userMessage) {
        return getUserMessage().replaceAll("\\s+", SEPARATOR);
    }

    /**
     * Must be called when using {@link SearchModule#SearchModule(Chatbot)} constructor, after adding your regexes! Firstly applies
     * regular expression to each list element, via {@link #addAnyToQuery(String)} and then "actionifies" them, with
     * {@link Util#ACTIONIFY(String)}.
     */
    // TODO think of a better name
    protected List<String> getMappedRegexList(List<String> regexList) {
        return regexList.stream() // test if working correctly
                .map(this::addAnyToQuery)
                .map(Util::ACTIONIFY)
                .collect(Collectors.toList());
    }

    protected static String makeRegex(String regex) {
        return Utils.actionify(regex);
    }

    /**
     * Returns user's message, without the regex.
     */
    private String getUserMessage() {
        return matcher.group(1); // not null-safe!!!!
    }

    /**
     * Returns regular expression with {@link #ANY_REGEX} appended to it.
     */
    private String addAnyToQuery(String regex) {
        System.out.println("regex: " + regex + "\n" + regex + ANY_REGEX);
        return regex + ANY_REGEX;
    }
}
