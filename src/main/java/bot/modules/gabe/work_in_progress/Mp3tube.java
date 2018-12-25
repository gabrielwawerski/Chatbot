package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.core.libs.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.libs.Utils.TO_REGEX;
import static bot.core.gabes_framework.core.libs.Utils.getCommands;

public class Mp3tube extends ModuleBase {
    private String url;
    protected HttpURLConnection connection;

    private static final String API_URL = "";
    private static final String PREFIX = "https://lolyoutube.com/download/";
    private static final String MP3 = "mp3";
    private Date timestamp;

    private static final String YTMP3_REGEX = TO_REGEX("ytmp3 (.*)");
    private static final String MP3_REGEX = TO_REGEX("mp3 (.*)");
    private static final String REGEX = ("mp3");

    public Mp3tube(Chatbot chatbot) {
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

        if (isOr(YTMP3_REGEX, MP3_REGEX) || match.equals(REGEX)) {
            Matcher matcher = Pattern.compile("v=").matcher(message.getMessage());
            if (found(message)) {
//                url = getFinalUrl(message.getMessage());
            }
        }
        return false;
    }

    private String getMp3(String url) {
        return MP3;
    }



    private String getFinalUrl(String messageBody) {
        timestamp = new Date();
        return PREFIX + getId(messageBody) + timestamp.toString();
    }

    private String getId(String messageBody) {
//        Matcher matcher = Pattern.compile("");
        return null;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, YTMP3_REGEX, MP3_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(YTMP3_REGEX, MP3_REGEX);
    }
}
