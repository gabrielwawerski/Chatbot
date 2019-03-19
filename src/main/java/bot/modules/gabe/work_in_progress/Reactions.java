package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO !top - wiadomosci z najwieksza iloscia reakcji (w tym dniu)
public class Reactions extends ModuleBase {
    private static ArrayList<Message> messages;
    private static Date time = new Date();
    private static long now;

    private static int maxMessages = 50;
    private static int timeoutMins = 1;

    private static long deleteTimeout = 60000 * timeoutMins;

    public Reactions(Chatbot chatbot) {
        super(chatbot);
        messages = new ArrayList<>(maxMessages);
        now = time.getTime();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        // if more than 50 msg AND timeout passed
        if (messages.size() > maxMessages && timeoutPassed()) {
            messages.remove(0);
            messages.add(message);
            System.out.println("msg added.");
        }

        // messages not full yet, timeout not passed
        else if (messages.size() <= maxMessages) {
            messages.add(message);
            System.out.println("msg added.");
        }

//        if (timeoutPassed()) {
            for (Message msg : messages) {
                if (msg.reacted()) {
                    msg.getReactionButton().click();
                    System.out.println("clicked!");
                }
                System.out.println("not reacted");
            }
//            messages.clear();
//        }
        return false;
    }

    private boolean timeoutPassed() {
        now = time.getTime();
        return now > deleteTimeout;
    }

    @Override
    protected List<String> setRegexes() {
        return NO_REGEX();
    }
}
