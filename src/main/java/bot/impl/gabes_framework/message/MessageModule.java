package bot.impl.gabes_framework.message;

import bot.Chatbot;
import bot.impl.orig_impl.exceptions.MalformedCommandException;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.gabes_framework.simple.SimpleModule;

import java.util.List;

import static bot.impl.orig_impl.helper.interfaces.Util.GET_RANDOM;

/**
 * Easily create new modules if you only need following functionality: preset trigger regexes and a single resource
 * file that. Takes care of API backend, if you only need mentioned functionality.<p>
 *
 * Simply extend from this class if above-mentioned features satisfy your needs, provide a list of regexes and a resource
 * file name in the constructor and you're set!
 *
 * @version 1.0
 * @since 0.29
 */
public abstract class MessageModule extends SimpleModule {
    protected List<String> messages;

    /**
     * Provides an easy way of getting the bot to respond to command/s. If there is more that one message, it gets
     * picked at random.
     *
     * @param chatbot chatbot reference
     * @param regexes trigger regexes for your module
     * @param messages message, or multiple messages (picked at random) for bot to send, after trigger is matched
     */
    public MessageModule(Chatbot chatbot, List<String> regexes, List<String> messages) {
        super(chatbot, regexes);
        this.messages = messages;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
                chatbot.sendMessage(GET_RANDOM(messages));
                return true;
            }
        }
        return false;
    }
}