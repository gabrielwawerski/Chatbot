package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.api.Module;
import bot.gabes_framework.reddit.Reddit;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static bot.core.helper.interfaces.Util.*;
import static bot.core.helper.interfaces.Util.DEACTIONIFY;

public class Memes implements Module {
    private final Chatbot chatbot;
    private List<String> subreddits;
    private String preloadedUrl;

    private final String DOG_REGEX = ACTIONIFY("test");
    private final String DOGGO_REGEX = ACTIONIFY("t");

    public Memes(Chatbot chatbot) {
        this.chatbot = chatbot;
        try {
            this.subreddits
                    = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + "subreddits.txt"));
            System.out.println(getClass().getSimpleName() + " online.");
        } catch (IOException e) { // TODO add global debugMessages field in Chatbot so this can be toggled.
            System.out.println(getClass().getSimpleName() + " niedostępne w bieżącej sesji.");
            e.printStackTrace();
        }

        preloadedUrl = Reddit.getSubredditPictureUrl(subreddits);
        // HighResNSFW
        // gonewild
        // nsfwpics
    }

    //region Overrides
    @Override
    @SuppressWarnings("Duplicates")
    public boolean process(Message message) {
        String match = getMatch(message);
        if (match.equals(DOG_REGEX) || match.equals(DOGGO_REGEX)) {
            System.out.println(preloadedUrl);
            chatbot.sendImageUrlWaitToLoad(preloadedUrl);
            preloadedUrl = Reddit.getSubredditPictureUrl(subreddits);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public void echoOnline() {
        System.out.println(getClass().getSimpleName() + " online");
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(DOG_REGEX)) {
            return DOG_REGEX;
        } else if (messageBody.matches(DOGGO_REGEX)) {
            return DOGGO_REGEX;
        } else {
            return "";
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(DOG_REGEX));
        commands.add(DEACTIONIFY(DOGGO_REGEX));
        return commands;
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }

    @Override
    public String toString() {
        String message = getClass().getSimpleName() + ": \n";
        for (String subreddit : subreddits) {
            message += "\t" + subreddit + "\n";
        }
        return message;
    }
    // sends message with link to random meme from specified subbredits. See {@link RedditModule}.
    // check if when sending the link, bot's message has image attached
    // (messenger auto attaches image to link with image (if it has been loaded before sending the message)
}
