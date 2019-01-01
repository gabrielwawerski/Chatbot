package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JakDojade extends ModuleBase {
    private final String URL = "https://jakdojade.pl/lublin/trasa/";

    private final String JD_HELP_REGEX = Utils.TO_REGEX("jd");
    private final String JAK_DOJADE_HELP_REGEX = Utils.TO_REGEX("jakdojade");

    private final String JAKDOJDE_ANY_REGEX = Utils.TO_REGEX("jakdojade (.*) (.*)");
    private final String JD_ANY_REGEX = Utils.TO_REGEX("jd (.*) (.*)");

    private final List<String> REGEXES;

    public JakDojade(Chatbot chatbot) {
        super(chatbot);
        REGEXES = List.of(JD_HELP_REGEX, JAK_DOJADE_HELP_REGEX, JAKDOJDE_ANY_REGEX, JD_ANY_REGEX);
    }


    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        Matcher matcher;

        if (isOr(JAK_DOJADE_HELP_REGEX, JD_HELP_REGEX)) {
            chatbot.sendMessage("Podaj przystanek z którego jedziesz, a po spacji podaj przystanek docelowy." +
                    "\nNp. !jd reja świętochowskiego");
        } else if (isOr(JAKDOJDE_ANY_REGEX, JD_ANY_REGEX)) {
            matcher = Pattern.compile(JAKDOJDE_ANY_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                String stop = matcher.group(1);
                String destination = matcher.group(2);
                chatbot.sendMessage(URL + "z" + "--" + stop.substring(0, 1).toUpperCase() + stop.substring(1)
                        + "--" + "do" + "--" + destination.substring(0, 1).toUpperCase() + destination.substring(1)
                        + "?fn=" + stop + "&tn=" + destination);
            }
        }

        return false;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, REGEXES);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(REGEXES);
    }
}
