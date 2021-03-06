package bot.modules.hollandjake;

import bot.core.Chatbot;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.helper.interfaces.RedditModule;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static bot.modules.hollandjake.Reddit.loadSubreddits;
import static bot.core.hollandjake_api.helper.interfaces.Util.*;

public class Birds implements RedditModule {
    //region Constants
    private final String BIRD_REGEX = ACTIONIFY("bird");
    private final String BIRB_REGEX = ACTIONIFY("birb");
    private final Chatbot chatbot;
    //endregion

    //region Variables
    private List<String> subreddits;
    private List<String> responses;
    private Image preloadedImage;
    //endregion

    public Birds(Chatbot chatbot) {
        this.chatbot = chatbot;
        subreddits = loadSubreddits(new File(appendModulePath("subreddits.txt")));
        try {
            responses = Files.readAllLines(Paths.get(appendModulePath("responses.txt")));
        } catch (IOException e) {
            System.out.println("Bird quotes are not available this session");
        }
        preloadedImage = Reddit.getSubredditPicture(subreddits);
    }

    //region Overrides
    @Override
    @SuppressWarnings("Duplicates")
    public boolean process(Message message) {
        String match = getMatch(message);
        if (match.equals(BIRD_REGEX) || match.equals(BIRB_REGEX)) {
            String quote = GET_RANDOM(responses);
            chatbot.sendImageWithMessage(preloadedImage, quote);
            preloadedImage = Reddit.getSubredditPicture(subreddits);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(BIRD_REGEX)) {
            return BIRD_REGEX;
        } else if (messageBody.matches(BIRB_REGEX)) {
            return BIRB_REGEX;
        } else {
            return "";
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(BIRD_REGEX));
        commands.add(DEACTIONIFY(BIRB_REGEX));
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
    //endregion
}