package bot.modules.gabe.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.User;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.helper.ModuleBase;
import bot.core.gabes_framework.core.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.util.Utils.TO_REGEX;
import static bot.core.gabes_framework.core.util.Utils.getCommands;

public class Mp3Tube extends ModuleBase {
    private static final String PREFIX = "https://lolyoutube.com/download/mp3/";

    private final String MP3_REGEX = TO_REGEX("mp3 (.*)");
    private final String INFO_REGEX = TO_REGEX("mp3");

    public Mp3Tube(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(INFO_REGEX)) {
            User user = db.getUser(message);
            user.addPoints(1);
            db.update(user);
            System.out.println("(+2) " + user.getName());
            chatbot.sendMessage(Utils.EMOJI_INFO + " Po !mp3 wklej link do youtube'a aby otrzymać url do pobrania.\n"
                    + Utils.EMOJI_EXCL_MARK_RED + " Link musi kończyć się ID filmu.");

        } else if (is(MP3_REGEX)) {
            if (patternFound(message)) {
                User user = db.getUser(message);
                user.addPoints(2);
                db.update(user);
                String id = getId(message.getMessage());
                System.out.println("id = " + id);
                if (id != null) {
                    chatbot.sendMessage(getUrl(id));
                    return true;
                } else {
                    chatbot.sendMessage("Nieprawidłowy link.");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(MP3_REGEX, INFO_REGEX);
    }

    private String getUrl(String id) {
        Date timestamp = new Date();
        return "\u2935 Link do pobrania:\n" + PREFIX + id + "/" + timestamp.getTime();
    }

    // obsluguje proste linki: https://www.youtube.com/watch?v=xxx
    // TODO dodac obsluge roznych linkow
    // np. https://www.youtube.com/watch?v=pCWmHE6QpvQ&index=112&t=0s&list=PLRCByVlDWF9N-SCqUezx1xnLJzsTr9FZ8
    private String getId(String messageBody) {
        Matcher matcher = Pattern.compile("v=(.*)").matcher(messageBody);
        if (matcher.find() && matcher.group().length() < 15) {
            return matcher.group().substring(2);
        } else {
            return null;
        }
    }
}
