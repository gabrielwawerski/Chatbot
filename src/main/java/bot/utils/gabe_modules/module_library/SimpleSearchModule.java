package bot.utils.gabe_modules.module_library;

import bot.core.Chatbot;
import bot.core.helper.interfaces.Util;
import bot.core.helper.misc.Message;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides a quick way of creating modules which will return user query. Extend only if the process is as simple as
 * adding the query to predefined URL. Example of such URL: {@code https://www.google.com/search?q=}<p>
 *
 * Replaces spaces between words, with plus signs.
 * @version 1.0
 * @since 0.30
 * @author Gabe
 */
public abstract class SimpleSearchModule extends SimpleModule {
    protected final String SEARCH_URL;
    protected Matcher matcher;

    public SimpleSearchModule(Chatbot chatbot, List<String> regexes, String searchUrl) {
        super(chatbot);
        this.SEARCH_URL = searchUrl;

        mapRegexes(regexes);
    }

    /**
     * Remember to call {@link #updateMatch(Message)} first. You can then use {@link #setMatcher(Message)}, which tries
     * to match the message with your regexes
     */
    @Override
    public abstract boolean process(Message message);

    /**
     * Sets {@link #matcher} to match given regex. Use in {@link #process(Message)} to find match to your regexes.
     * @param message
     */
    private void setMatcher(Message message) {
        for (String regex : regexList) {
            if (match.equals(regex)) {
                this.matcher = Pattern.compile(regex).matcher(message.getMessage());
            }
        }
    }

    private boolean matchFound() {
        return matcher.find(); // TODO && !matcher.group(1).isEmpty()?
    }

    private String getCompleteUrl(String message) {
        return SEARCH_URL + " " + formatToUrl(message);
    }

    /**
     * Replaces every space ( ) with a plus sign, which is the most common way for websites to make the search result URL.
     *
     * @param userMessage user message to be formatted
     * @return formatted user message, ready to be
     */
    private String formatToUrl(String userMessage) {
        return getUserQuery().replaceAll("\\s+","+");
    }

    /**
     * Returns user's message, without regex.
     */
    private String getUserQuery() {
        return matcher.group(1);
    }

    /**
     * Helper method. Sets {@link #matcher} to user query. Use when providing regexes inside the class, but if doing so,
     * remember to also override {@link #getMatch(Message)}, {@link #getCommands()} methods. Use after
     * {@link #updateMatch(Message)} in overriden {@link #process(Message)} method.
     *
     * @param message
     * @param regex
     */
    private void setMatcher(Message message, String regex) {
        this.matcher = Pattern.compile(regex).matcher(message.getMessage());
    }

    /**
     * Firstly adds regular expression to all elements of the list, via {@link #addAnyToQuery(String)} and then
     * "actionifies" them, with {@link Util#ACTIONIFY(String)}.
     *
     * @param regexList
     */
    private void mapRegexes(List<String> regexList) {
        this.regexList = regexList.stream() // test if working correctly
                .map(this::addAnyToQuery)
                .map(Util::ACTIONIFY)
                .collect(Collectors.toList());
    }

    /**
     * Adds regular expression to the end of a regex. What (.*) regex means, is "any text"
     *
     * @param regex
     * @return
     */
    private String addAnyToQuery(String regex) {
        return regex + " (.*)";
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        for (String regex : regexList) {
            if (messageBody.matches(regex)) {
                return regex;
            }
        }
        return "";
    }
}
