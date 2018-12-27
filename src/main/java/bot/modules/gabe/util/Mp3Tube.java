package bot.modules.gabe.util;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.core.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.Utils.TO_REGEX;
import static bot.core.gabes_framework.core.Utils.getCommands;

public class Mp3Tube extends ModuleBase {
    private String url;
    private Date timestamp;

    private static final String PREFIX = "https://lolyoutube.com/download/mp3/";

    private final String MP3_REGEX = TO_REGEX("mp3 (.*)");
    private final String INFO_REGEX = TO_REGEX("mp3");

    public Mp3Tube(Chatbot chatbot) {
        super(chatbot);
    }

    private HttpURLConnection openConnection(URL url) {
        try { return (HttpURLConnection) url.openConnection(); }
        catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(INFO_REGEX)) {
            chatbot.sendMessage(Utils.INFRMATION_EMOJI + " Po !mp3 wklej link do youtube'a aby otrzymać url do pobrania.\n"
                    + Utils.EXCL_MARK_RED_EMOJI + " Link musi kończyć się ID filmu.");
        } else if (is(MP3_REGEX)) {
            if (found(message)) {
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

    private String getUrl(String id) {
        timestamp = new Date();
        return "\u2935Pobierz:\n" + PREFIX + id + "/" + timestamp.getTime();
    }

    // obsluguje proste linki: https://www.youtube.com/watch?v=xxx
    private String getId(String messageBody) {
        // TODO dodac obsluge linkow z tekstem po id
        // np. https://www.youtube.com/watch?v=pCWmHE6QpvQ&index=112&t=0s&list=PLRCByVlDWF9N-SCqUezx1xnLJzsTr9FZ8
        Matcher matcher = Pattern.compile("v=(.*)").matcher(messageBody);
        if (matcher.find() && matcher.group().length() < 15) {
            return matcher.group().substring(2);
        } else {
            return null;
        }
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, MP3_REGEX, INFO_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(MP3_REGEX, INFO_REGEX);
    }
}
