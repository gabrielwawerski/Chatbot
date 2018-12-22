package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;

import static bot.gabes_framework.core.libs.Utils.TO_REGEX;
import static bot.gabes_framework.core.libs.Utils.regexToList;

public class Mp3tube extends ModuleBase {
    private URL url;
    protected HttpURLConnection connection;

    private static final String API_URL = "";

    private static final String YTMP3_REGEX = TO_REGEX("ytmp3 (.*)");
    private static final String MP3_REGEX = TO_REGEX("mp3 (.*)");

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

        if (isOrOther(YTMP3_REGEX, MP3_REGEX)) {
            Matcher matcher = getMatcher(message);
            if (found(message)) {
                url = getFinalUrl();
            }
        }
        return false;
    }

    private URL getFinalUrl() {
        return null;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, YTMP3_REGEX, MP3_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return regexToList(YTMP3_REGEX, MP3_REGEX);
    }
}
