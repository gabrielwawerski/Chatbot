package bot.utils.gabe_modules.module_library.search;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.interfaces.Util;
import bot.core.helper.misc.Message;
import bot.utils.gabe_modules.module_library.simple.SearchModuleBase;
import com.google.errorprone.annotations.ForOverride;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides a quick way of creating modules which will return URL with user query. Extend if the process is as simple as
 * adding the user query to predefined URL. Example of such URL: {@code https://www.google.com/search?q=}<p>
 *
 * You can change the default separator (see {@linkplain #DEFAULT_SEPARATOR}) by overriding {@link #setSeparator()} and
 * setting it there.
 *
 * Replaces spaces between words with a plus sign.
 * @version 1.0
 * @since 0.30
 */
public abstract class SearchModule extends SearchModuleBase {
    /** Common separator, used by most websites to separate individual words in their search result URL */
    protected static final String DEFAULT_SEPARATOR = "+";
    /** {@code (.*)} regex means "any text" */
    private static final String ANY_REGEX = " (.*)";

    private String messageBody = "";

    /**
     * Be sure to call {@link #mapRegexes(List)} before doing anything else, otherwise your regexes won't trigger
     * the module logic.<p>
     *
     * Default separator is a plus sign "+". If needed, you can change it by overriding {@link #setSeparator()}
     */
    public SearchModule(Chatbot chatbot, List<String> regexes) {
        super(chatbot);
        mapRegexes(regexes);
    }

    public SearchModule(Chatbot chatbot) {
        super(chatbot);
    }

    /**
     * Tries to match user's message with each {@link #regexList} item. If a match is found, instantiates {@link #matcher}
     * with
     * .You can then use {@link #setMatcher(String)}, which tries
     * to match the message with your regexes
     */
    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        messageBody = message.getMessage();

        if (executeBeforeMatchAndReturn(message) == false) {
            return true;
        }
        executeBeforeMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
                setMatcher(messageBody);

                if (isMatchFound()) {
                    executeIfMatchFound(message);

                    if (!executeIfMatchFoundAndReturn(message)) {
                        return true;
                    }
                } else {
                    System.out.println(getClass().getSimpleName() + " did not find any match.");
                    throw new MalformedCommandException();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Called before the regex matching loop. Override, if you need the possibility to stop any further code execution
     * in your module. If so, return {@Code true} to finish (tells the API that message has been successfully processed)<p>
     *
     * Example: If user's message is longer than it can be, return false to finish message processing by your module.
     *
     * @param message ignore if not needed
     * @return false, if you need to stop further module's code execution, true is essentialy ignored - return it,
     *         if everything is OK
     */
    @ForOverride
    protected boolean executeBeforeMatchAndReturn(Message message) {
        return true;
    }

    /**
     * Called before the regex matching loop. Override, if needed.<p>
     *
     * @param message ignore if not needed
     */

    @ForOverride
    protected void executeBeforeMatch(Message message) {
    }

    /**
     *
     *
     * @param message ignore if not needed
     * @return false, if you need to stop further module's code execution, true is essentialy ignored - return it,
     *         if everything is OK
     */
    @ForOverride
    protected void executeIfMatchFound(Message message) {
        // class's default logic - simply sends the query url
        chatbot.sendMessage(getFinalUrl(messageBody));
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
    protected boolean executeIfMatchFoundAndReturn(Message message) {
        return true;
    }

    @Override
    protected String setSeparator() {
        return DEFAULT_SEPARATOR;
    }

    /**
     * Returns a complete URL with properly formatted user message.
     *
     * @param formattedMessage formatted message ready to be sent
     */
    protected String getFinalUrl(String formattedMessage) {
        return SEARCH_URL + toQuery(formattedMessage);
    }

    /**
     * Must be called when using {@link #SearchModule(Chatbot)} constructor, after adding your regexes! Firstly applies
     * regular expression to each list element, via {@link #addAnyToQuery(String)} and then "actionifies" them, with
     * {@link Util#ACTIONIFY(String)}.
     */
    protected void mapRegexes(List<String> regexList) {
        this.regexList = regexList.stream() // test if working correctly
                .map(this::addAnyToQuery)
                .map(Util::ACTIONIFY)
                .collect(Collectors.toList());
    }

    /**
     * Returns regular expression with {@link #ANY_REGEX} appended to it.
     */
    private String addAnyToQuery(String regex) {
        return regex + ANY_REGEX;
    }
}
