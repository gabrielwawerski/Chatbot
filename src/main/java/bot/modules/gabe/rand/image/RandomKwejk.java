package bot.modules.gabe.rand.image;

import bot.core.Chatbot;
import bot.modules.gabe.point_system.util.Points;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.ModuleBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.hollandjake_api.helper.interfaces.Util.*;

/**
 * @version 1.0
 * @since v0.31
 */
public class RandomKwejk extends ModuleBase {
    private String currentImageUrl;
    private Document doc;
    private Elements els;

    private static final String KWEJK_URL = "https://kwejk.pl/losowy";

    private static final String KWEJK_REGEX = ACTIONIFY("kwejk");
    private static final String KW_REGEX = ACTIONIFY("kw");

    public RandomKwejk(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isRegex()) {
            pushPoints(message, Points.POINTS_RANDOMKWEJK_REGEX);
            chatbot.sendImageUrlWaitToLoad(currentImageUrl);
            getNextMeme();
            return true;
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(KWEJK_REGEX, KW_REGEX);
    }

    private void getNextMeme() {
        getDocument(KWEJK_URL);
        els = doc.getElementsByClass("figure");
        currentImageUrl = getUrl(els.toString());
    }

    private String getUrl(String string) {
        Pattern pattern = Pattern.compile("src=\".*?\"");
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            String imgUrl = matcher.group();
            imgUrl = imgUrl.substring(5, imgUrl.length() - 1);
            return imgUrl;
        }
        return "";
    }

    private void getDocument(String url) {
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .get();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }
}
