package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.impl.orig_impl.exceptions.MalformedCommandException;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.gabes_framework.simple.SimpleModule;

import java.util.List;

public class SuggestFeature extends SimpleModule {
    // dodaje tresc wiadomosci do pliku suggestions.txt

    public SuggestFeature(Chatbot chatbot, List<String> regexList) {
        super(chatbot, regexList);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }
}
