package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.interfaces.RedditModule;
import bot.core.helper.interfaces.Util;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.reddit.Reddit;
import bot.gabes_framework.simple.SimpleModule;

import java.awt.*;
import java.io.File;
import java.util.List;

public class Memes extends SimpleModule implements RedditModule {
    private final String MEME_REGEX = Utils.TO_REGEX("meme");
    private final String MEM_REGEX = Utils.TO_REGEX("mem");

    private List<String> subreddits;

    public Memes(Chatbot chatbot) {
        super(chatbot);
        regexList = List.of(MEME_REGEX, MEM_REGEX);
        subreddits = Reddit.loadSubreddits(new File(appendModulePath("subreddits.txt")));
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
//                String meme = Reddit.getSubredditPictureUrl(subreddits);
//                chatbot.sendImageFromURLWithMessage(meme, "");
//                chatbot.sendImageUrlWaitToLoad(meme);
                String img = Util.GET_PAGE_SOURCE(Reddit.getSubredditPictureUrl(subreddits));
                System.out.println(img);
                chatbot.sendImageWithMessage(img, "");
                return true;
            }
        }
        return false;
    }
    // sends message with link to random meme from specified subbredits. See {@link RedditModule}.
    // check if when sending the link, bot's message has image attached
    // (messenger auto attaches image to link with image (if it has been loaded before sending the message)
}
