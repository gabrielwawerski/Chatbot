package bot.modules.gabe.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since v0.034
 */
public class Insult extends ModuleBase {
    private static final String URL = "https://evilinsult.com/generate_insult.php?lang=en&type=text";

    private static final String INSULTS_REGEX = Utils.TO_REGEX("insult (.*)");
    private static final String INSULTS_ME_REGEX = Utils.TO_REGEX("insult me");

    public Insult(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        User user;

        if (is(INSULTS_ME_REGEX)) {
            user = db.getUser(message);

            System.out.println("in");
            if (user != User.EMPTY_USER) {
                String response;
                try {
                    java.net.URL url = new URL(URL);
                    URLConnection con = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    response = in.readLine();
                    in.close();
                    System.out.println(response);

                    String msg = ", " + Character.toString(response.charAt(0)).toLowerCase() + response.substring(1);

                    chatbot.sendMentionMessage(user.getName(), msg);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            System.out.println("empty user");
            return false;
        } else if (is(INSULTS_REGEX)) {
            Matcher matcher = Pattern.compile(INSULTS_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                user = db.findUser(matcher.group(1));

                if (user == User.EMPTY_USER) {
                    return false;
                }
                String response;
                try {
                    java.net.URL url = new URL(URL);
                    URLConnection con = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    response = in.readLine();
                    in.close();
                    System.out.println(response);

                    String msg = ", " + Character.toString(response.charAt(0)).toLowerCase() + response.substring(1);

                    chatbot.sendMentionMessage(user.getName(), msg);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(INSULTS_ME_REGEX, INSULTS_REGEX);
    }
}
