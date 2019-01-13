package bot.modules.gabe.rand.image;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

import static bot.core.hollandjake_api.helper.interfaces.Util.*;

public class Inspire extends ModuleBase {
    private static final String INSPIRE_REGEX = ACTIONIFY("inspire");

    public Inspire(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(INSPIRE_REGEX);
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