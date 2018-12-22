package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.resource.SaveResourceModule;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sylwester extends SaveResourceModule {
    private final String INFO_MESSAGE = "Link do piosenki którą chcesz usłyszeć w sylwestra! !sylwester link";

    private final String INFO_REGEX = Utils.TO_REGEX("sylwester");
    private final String PIOSENKA_REGEX = Utils.TO_REGEX("sylwester (.*)");

    public Sylwester(Chatbot chatbot, String fileName) {
        super(chatbot, fileName);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (match.equals(INFO_REGEX)) {
            chatbot.sendMessage(INFO_MESSAGE);
            return true;
        } else if (match.equals(PIOSENKA_REGEX)) {
            Matcher matcher = Pattern.compile(PIOSENKA_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                String msg = message.getMessage().substring(11);
                appendStringToFile(msg);
                chatbot.sendMessage("Zapisano.");
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(INFO_REGEX)) {
            return INFO_REGEX;
        } else if (messageBody.matches(PIOSENKA_REGEX)) {
            return PIOSENKA_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.TO_COMMAND(INFO_REGEX));
        commands.add(Utils.TO_COMMAND(PIOSENKA_REGEX));
        return commands;
    }


}
