package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.ModuleBase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.util.Utils.TO_REGEX;

public class PollCreate extends ModuleBase {
    private final String HELP_REGEX = TO_REGEX("poll");
    private final String HELP_2_REGEX = TO_REGEX("poll help");
    private final String POLL_1 = TO_REGEX("poll (.*) . (.*) . (.*)");

    public PollCreate(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(HELP_REGEX, HELP_2_REGEX, POLL_1);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOr(HELP_REGEX, HELP_2_REGEX)) {
            chatbot.sendMessage("");
            return true;
        } else if (is(POLL_1)) {
            Matcher matcher = Pattern.compile(POLL_1).matcher(message.getMessage());
            if (matcher.find()) {


                String question = matcher.group(0);
                String answer1 = matcher.group(1);
                String answer2 = matcher.group(2);
                System.out.println(question + "\n" + answer1 + "\n" + answer2);

                chatbot.sendMessage(question + "\n" + answer1 + "\n" + answer2);
                return true;
            }
        }
        return false;
    }
}
