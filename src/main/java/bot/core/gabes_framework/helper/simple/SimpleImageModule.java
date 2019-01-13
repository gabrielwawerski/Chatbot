package bot.core.gabes_framework.helper.simple;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.awt.*;
import java.util.List;

@Deprecated
public class SimpleImageModule extends SimpleModule {
    private Image image;
    private String message;

    // TODO 2nd consturctor with String image
    public SimpleImageModule(Chatbot chatbot, List<String> regexes, Image image, String message) {
        super(chatbot, regexes);
        this.image = image;
        this.message = message;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexes) {
            if (match.equals(regex)) {
                chatbot.sendImageWithMessage(image, this.message);
                return true;
            }
        }
        return false;
    }
}
