package bot.modules.gabe.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.util.Utils;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;

public class BIntoEmote extends ModuleBase {
    private final String B_ANY_REGEX = Utils.TO_REGEX("b (.*)");

    public BIntoEmote(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();
        String processedMessage = "";

        if (is(B_ANY_REGEX)) {
            for (int i = 0; i < messageBody.length(); i++) {

            }
            return true;
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, B_ANY_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(B_ANY_REGEX);
    }
}
