package bot.utils.gabe_modules.module_library.simple;

import bot.core.Chatbot;
import bot.core.helper.misc.Message;
import bot.utils.gabe_modules.module_base.ModuleBase;
import bot.utils.gabe_modules.module_library.search.SearchModule;
import com.google.errorprone.annotations.ForOverride;

import java.lang.annotation.Inherited;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for modules which return search result url, for search query given by the user. For basic functionality,
 * see {@link SearchModule}.
 *
 * @version 1.0
 * @since 0.30
 * @see SearchModule
 */
public abstract class SearchModuleBase extends ModuleBase {
    protected final String SEARCH_URL;
    protected final String SEPARATOR;

    protected Matcher matcher;

    public SearchModuleBase(Chatbot chatbot) {
        super(chatbot);
        SEARCH_URL = setSearchUrl();
        SEPARATOR = setSeparator();
        matcher = null;
    }
    @Override
    public abstract String getMatch(Message message);

    @Override
    public abstract ArrayList<String> getCommands();

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
     * Returns a complete URL with properly formatted user message.
     *
     * @param formattedMessage formatted message ready to be sent
     */
    protected String getFinalUrl(String formattedMessage) {
        return SEARCH_URL + toQuery(formattedMessage);
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
    protected void setMatcher(String messageBody) {
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
     * Returns user's message, without the regex.
     */
    private String getUserMessage() {
        return matcher.group(1); // not null-safe!!!!
    }
}
