package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;

import java.util.ArrayList;

import static bot.gabes_framework.core.libs.Utils.TO_REGEX;

public class Mp3tube extends ModuleBase {
    private static final String YTMP3_REGEX = TO_REGEX("ytmp3 (.*)");
    private static final String MP3_REGEX = TO_REGEX("mp3 (.*)");

    public Mp3tube(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOrOther(YTMP3_REGEX, MP3_REGEX)) {

        }
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, YTMP3_REGEX, MP3_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return getCommandsList(YTMP3_REGEX, MP3_REGEX);
    }
}
