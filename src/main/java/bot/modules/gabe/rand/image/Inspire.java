package bot.modules.gabe.rand.image;

import bot.core.Chatbot;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.helper.simple.SimpleModule;

import java.util.List;

import static bot.core.hollandjake_api.helper.interfaces.Util.*;

public class Inspire extends SimpleModule {
    private final String INSPIRE_REGEX = ACTIONIFY("inspire");

    public Inspire(Chatbot chatbot) {
        super(chatbot, List.of("inspire"));
    }

    @Override
    public boolean process(Message message) {
        updateMatch(message);

        if (is(INSPIRE_REGEX)) {
            String imgURL = GET_PAGE_SOURCE("http://inspirobot.me/api?generate=true");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            chatbot.sendImageUrlWaitToLoad(imgURL);
            return true;
        }
        return false;
    }
}