package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.simple.SimpleUrlImageModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static bot.core.helper.interfaces.Util.GET_RANDOM;

public class ImageFromUrl extends SimpleUrlImageModule {
    private List<String> subreddits;

    public ImageFromUrl(Chatbot chatbot, List<String> regexList, String url, String message) {
        super(chatbot, regexList, url, message);
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

        for (String regex : regexList) {
            if (match.equals(regex)) {
                String subreddit = GET_RANDOM(subreddits);


                chatbot.sendImageUrlWaitToLoad("https://i.imgur.com/yKAXiyl.jpg");
                return true;
            }
        }
        return false;
    }
}
