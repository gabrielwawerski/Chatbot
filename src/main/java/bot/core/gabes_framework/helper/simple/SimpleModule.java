package bot.core.gabes_framework.helper.simple;

import bot.core.Chatbot;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.helper.ModuleBase;
import bot.core.gabes_framework.core.api.Module;
import bot.core.gabes_framework.helper.message.MessageModule;
import bot.core.gabes_framework.helper.message.SingleMessageModule;
import bot.core.gabes_framework.helper.resource.RandomResourceModule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Offers a simple way to create modules. Does not provide messaging capabilities. If you need your module to be able to
 * communicate back, see {@link MessageModule}, {@link RandomResourceModule}, {@link SingleMessageModule}. If these do not
 * satisfy your needs, you can extend "lower" level classes, such as this one, or {@link ModuleBase} class, which gives
 * you only basic connection to the chatbot's API. You can of course implement {@link Module} interface and it's methods
 * yourself or use hollandjake's implementation. See {@link bot.core.hollandjake_api.helper.interfaces.Module}
 * <p>
 * If you need more control over regexes, see {@link ModuleBase}. All {@link Module} methods have been taken care of
 * for you, except {@link Module#process(Message)}, which you have to implement yourself.
 *
 * @see Module
 * @see #process(Message)
 * @see ModuleBase
 *
 * @version 1.1
 * @since 0.30
 */
public abstract class SimpleModule extends ModuleBase {
    /**
     * Best use {@code List.of()} to provide the regex/es.
     *
     * @param chatbot chatbot reference
     * @param regexes trigger commands that bot will react to, in {@link #process(Message)} method, which you have to
     *                implement.
     */
    public SimpleModule(Chatbot chatbot, List<String> regexes) {
        super(chatbot);
        this.regexes = regexes.stream().map(bot.core.hollandjake_api.helper.interfaces.Util::ACTIONIFY).collect(Collectors.toList());
    }

    @Override
    protected List<String> setRegexes() {
        return null;
    }
}
