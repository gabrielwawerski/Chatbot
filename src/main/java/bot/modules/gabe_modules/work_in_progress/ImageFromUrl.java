package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.simple.SimpleUrlImageModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.helper.interfaces.Util.*;

public class ImageFromUrl extends ModuleBase {
    private List<String> subreddits;

    private final String MEMES_URL = ACTIONIFY("a");

    public ImageFromUrl(Chatbot chatbot) {
        super(chatbot);
        try {
            this.subreddits
                    = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + "subreddits.txt"));
            System.out.println(getClass().getSimpleName() + " online.");
        } catch (IOException e) { // TODO add global debugMessages field in Chatbot so this can be toggled.
            System.out.println(getClass().getSimpleName() + " niedostępne w bieżącej sesji.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (match.equals(MEMES_URL)) {
//            String redditPath = "https://www.reddit.com/r/" + GET_RANDOM(subreddits) + "/random.json";
            String redditPath = "https://www.reddit.com/r/" + "cats" + "/random.json";
            String data = GET_PAGE_SOURCE(redditPath);
            System.out.println(data);
            Matcher matcher = Pattern.compile("https://i\\.redd\\.it/\\S+?\\.jpg").matcher(redditPath);
            System.out.println(matcher);

//            if (matcher.find()) {
//                chatbot.sendImageUrlWaitToLoad(matcher.group());
//                    chatbot.sendImageUrlWaitToLoad("https://i.imgur.com/yKAXiyl.jpg");
//                return true;
//            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(MEMES_URL)) {
            return MEMES_URL;
        } else {
            return "";
        }
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(MEMES_URL));
        return commands;
    }
}
