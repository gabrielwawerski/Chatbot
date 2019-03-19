package bot.modules.gabe.util.trivia;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

// TODO inna klasa quizowa: emotequiz - scrape ze strony:
// https://samequizy.pl/czy-uda-ci-sie-rozwiazac-emoji-quiz/
public class Trivia extends ModuleBase {
    //https://opentdb.com/api_config.php
    private static String SESSION_TOKEN;

    /** Returned results successfully. */
    private static final int SUCCESS = 0;
    /** Could not return results. The API doesn't have enough questions for your query. (Ex. Asking for 50 Questions
     *  in a Category that only has 20.) */
    private static final int NO_RESULTS = 1;
    /** Contains an invalid parameter. Arguements passed in aren't valid. (Ex. Amount = Five) */
    private static final int INVALID_PARAMETER = 2;
    /** Session Token does not exist. */
    private static final int TOKEN_NOT_FOUND = 3;
    /** Session Token has returned all possible questions for the specified query. Resetting the Token is necessary. */
    private static final int TOKEN_EMPTY = 4;

    private static final String PUBLIC_QUERY = "https://opentdb.com/api.php?amount=10";

    public Trivia(Chatbot chatbot) {
        super(chatbot);
        SESSION_TOKEN = requestToken();
    }

    private String requestToken() {
        return null;
    }

    private HttpURLConnection getConnection() {
        URL url;
        HttpURLConnection con;

        try {
            url = new URL("https://opentdb.com/api_token.php?command=request");
            con = (HttpURLConnection) url.openConnection();
            return con;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected List<String> setRegexes() {
        return null;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        // TODO get questions only when regex is fired?
        return false;
    }
}
