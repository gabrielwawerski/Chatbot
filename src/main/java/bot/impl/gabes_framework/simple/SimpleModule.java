package bot.impl.gabes_framework.simple;

import bot.Chatbot;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.orig_impl.helper.interfaces.Util;
import bot.impl.gabes_framework.core.ModuleBase;
import bot.impl.gabes_framework.core.libs.api.Module;
import bot.impl.gabes_framework.message.MessageModule;
import bot.impl.gabes_framework.message.SingleMessageModule;
import bot.impl.gabes_framework.resource.ResourceModule;
import bot.impl.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Offers a simple way to create modules. Does not provide messaging capabilities. If you need your module to be able to
 * communicate back, see {@link MessageModule}, {@link ResourceModule}, {@link SingleMessageModule} libraries. If these
 * do not satisfy your needs, you can extend "lower" level classes, such as this one, or {@link ModuleBase} class, which
 * gives you only basic connection to the chatbot's API. You can of course implement {@link Module} interface and
 * it's methods yourself or even use hollandjake's implementation. See {@link bot.impl.orig_impl.helper.interfaces.Module}
 * <p>
 * Extend from this class if you will be supplying regexList through constructor. All {@link Module} methods have been
 * taken care of for you, except {@link Module#process(Message)}, which you have to implement yourself.<p>
 *
 * If you need more control over your regexes, see {@link ModuleBase}. See {@link Module#process(Message)}, {@link Module}
 * and {@link ModuleBase} for more info.
 *
 * @version 1.0
 * @since 0.30
 */
public abstract class SimpleModule extends ModuleBase {
    /** Your module trigger regexes, provided by you in the constructor. See {@link #SimpleModule(Chatbot, List)} */
    protected List<String> regexList;

    /**
     * Use {@code List.of()} to provide the regex/es.
     *
     * @param chatbot chatbot reference
     * @param regexList trigger regexes that bot will react to in {@link #process(Message)} method
     */
    public SimpleModule(Chatbot chatbot, List<String> regexList) {
        super(chatbot);
        this.regexList = regexList.stream().map(Util::ACTIONIFY).collect(Collectors.toList());
    }

    public SimpleModule(Chatbot chatbot) {
        super(chatbot);
    }

    /**
     * Tries to match received message against all {@link #regexList} List entries. If a match is found, returns a
     * corresponding entry from {@link #regexList}.
     *
     * @return matched regex
     */
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

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(regexList);
    }
}
