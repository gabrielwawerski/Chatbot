package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.simple.SimpleModule;

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
