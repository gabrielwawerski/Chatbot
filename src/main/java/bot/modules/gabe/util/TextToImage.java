package bot.modules.gabe.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// https://www.baeldung.com/java-http-request
// http://img4me.com/developer
//
// &fcolor=000000 - font color
// &size=10 - 5 - 35
// &bcolor=FFFFFF - background color - not added = transparent

/**
 * @since v0.332
 */
public class TextToImage extends ModuleBase {
    private static final int MAX_TEXT_LENGTH = 300;

    private static final String PREFIX = "http://api.img4me.com/?text=";
    // arial, impact, courier, ...
    private static final String FONT = "&font=impact";

    private static final String SIZE_NORMAL = "&size=33";

    private static final String EXTENSION = "&type=jpg";

    private static final String REGEX_NORMAL = Utils.TO_REGEX("img (.*)");

    public TextToImage(Chatbot chatbot) {
        super(chatbot);
    }


    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(REGEX_NORMAL)) {
            Matcher matcher = Pattern.compile(REGEX_NORMAL).matcher(message.getMessage());

            if (matcher.find() && matcher.group(1).length() <= MAX_TEXT_LENGTH) {
                String userMsg = matcher.group(1);

                userMsg = userMsg.trim();

                if (StringUtils.countMatches(userMsg, "/") > 15) {
                    chatbot.sendMessage("Max. 15 nowych linii!");
                    return false;
                }

                userMsg = userMsg.replace("/", "%0A");
                userMsg = userMsg.replace(" ", "%20");

                String apiURL = getUrl(userMsg);
                String response;

                try {
                    URL url = new URL(apiURL);
                    URLConnection con = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    response = in.readLine();
                    in.close();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                Image image = null;

                try {
                    image = ImageIO.read(new URL(response));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                chatbot.sendImageWithMessage(image, "");
                return true;
            } else {
                chatbot.sendMessage("Nieprawidłowy format.");
                return false;
            }
        }
        return false;
    }

    private String getUrl(String text) {
        System.out.println(PREFIX + text + SIZE_NORMAL);
        return PREFIX + text + FONT + SIZE_NORMAL + "&bcolor=FFFFFF" + EXTENSION;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(REGEX_NORMAL);
    }
}
