package bot.modules.gabe_module;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.utils.gabe_modules.module_library.SimpleModule;
import bot.utils.gabe_modules.module_library.SingleMessageModule;

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
