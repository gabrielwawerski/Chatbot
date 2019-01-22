package bot.modules.gabe.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.User;
import bot.modules.gabe.point_system.util.Points;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.ModuleBase;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.util.Utils.TO_REGEX;
import static bot.core.gabes_framework.core.util.Utils.getCommands;

public class Mp3Tube extends ModuleBase {
    private static final String PREFIX = "https://lolyoutube.com/download/mp3/";

    private static final String MP3_REGEX = TO_REGEX("mp3 (.*)");
    private static final String INFO_REGEX = TO_REGEX("mp3");

    public Mp3Tube(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        User user = db.getUser(message);
        boolean userFound = user != User.INVALID_USER;

        if (is(INFO_REGEX)) {
            if (userFound) {
                user.addPoints(Points.POINTS_MP3_INFO);
                db.update(user);

                System.out.println("(+2) " + user.getName());
                chatbot.sendMessage(Emoji.INFO + " Po !mp3 wklej link do youtube'a aby otrzymać url do pobrania.\n"
                        + Emoji.EXCL_MARK_RED + " Link musi kończyć się ID filmu.");
                return true;
            } else {
                chatbot.sendMessage(Emoji.INFO + " Po !mp3 wklej link do youtube'a aby otrzymać url do pobrania.\n"
                        + Emoji.EXCL_MARK_RED + " Link musi kończyć się ID filmu.");
                return true;
            }
        } else if (is(MP3_REGEX)) {
            if (patternFound(message)) {
                if (userFound) {
                    user.addPoints(2);
                    db.update(user);
                }

                String id = getVideoId(message.getMessage());
                if (id != null) {
                    System.out.println("id = " + id);
                    chatbot.sendMessage(getUrl(id));
                    return true;
                } else {
                    chatbot.sendMessage("Nieprawidłowy link.");
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    private boolean patternFound(Message message) {
        Matcher matcher = Pattern.compile(match).matcher(message.getMessage());
        return matcher.find();
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
    private String getVideoId(String messageBody) {
        Matcher matcher = Pattern.compile("v=(.*)").matcher(messageBody);
        if (matcher.find() && matcher.group().length() < 15) {
            return matcher.group().substring(2);
        } else {
            return null;
        }
    }
}
