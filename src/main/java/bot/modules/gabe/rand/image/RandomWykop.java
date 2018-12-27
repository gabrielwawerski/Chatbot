package bot.modules.gabe.rand.image;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomWykop extends ModuleBase {
    private String currImgUrl;
    private Document doc;
    private Elements els;

    private ArrayList<String> urls;
    private int index;

    private static final String MEMY_URL = "https://www.wykop.pl/tag/memy/";
    private static final String POLSKA_URL = "https://www.wykop.pl/tag/polska/";

    private final String WYKOP_REGEX = Utils.TO_REGEX("wykop");
    private final String WY_REGEX = Utils.TO_REGEX("wy");

    private final List<String> REGEXES;

    public RandomWykop(Chatbot chatbot) {
        super(chatbot);
        REGEXES = List.of(WYKOP_REGEX, WY_REGEX);
        index = 0;
        getDocument(MEMY_URL);
        els = doc.getElementsByClass("media-content");
        initUrls(els.toString());
        currImgUrl = getNextMeme();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOr(WYKOP_REGEX, WY_REGEX)) {
            chatbot.sendImageUrlWaitToLoad(currImgUrl);
            currImgUrl = getNextMeme();
            return true;
        }
        return false;
    }

    private String getNextMeme() {
        if (index < urls.size()) {
            return urls.get(index++);
        } else {
            getDocument(MEMY_URL);
            initUrls(doc.getElementsByClass("media-content").toString());
            index = 0;
            System.out.println("Koniec arraylist z memami.");
            return urls.get(index++);
        }
    }

    private void initUrls(String els) {
        urls = new ArrayList<>(50);
        Pattern pattern = Pattern.compile("a href=\".*?\"");
        Matcher matcher = pattern.matcher(els);
        String lastUrl = "";

        while (matcher.find()) {
            String url = matcher.group();
            if (url.equals(lastUrl) || url.contains("imgflip")) {
                continue;
            }
            urls.add(url.substring(8, url.length() - 1));
            lastUrl = url;
        }
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

    @Override
    public String getMatch(Message message) {
        return findMatch(message, REGEXES);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(REGEXES);
    }
}
