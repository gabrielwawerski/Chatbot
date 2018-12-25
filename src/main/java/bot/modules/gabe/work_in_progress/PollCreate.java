package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.ModuleBase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.libs.Utils.TO_COMMAND;
import static bot.core.gabes_framework.core.libs.Utils.TO_REGEX;

public class PollCreate extends ModuleBase {
    private static final String HELP_REGEX = TO_REGEX("poll");
    private static final String HELP_2_REGEX = TO_REGEX("poll help");
    private static final String POLL_1 = TO_REGEX("poll (.*) . (.*) . (.*)");

    public PollCreate(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (match.equals(HELP_REGEX) || match.equals(HELP_2_REGEX)) {
            chatbot.sendMessage("");
            return true;
        } else if (match.equals(POLL_1)) {
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

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.matches(HELP_REGEX)) {
            return HELP_REGEX;
        } else if (messageBody.matches(HELP_2_REGEX)) {
            return HELP_2_REGEX;
        } else if (messageBody.matches(POLL_1)) {
            return POLL_1;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(TO_COMMAND(HELP_REGEX));
        commands.add(TO_COMMAND(HELP_2_REGEX));
        commands.add(TO_COMMAND(POLL_1));
        return commands;
    }
}
