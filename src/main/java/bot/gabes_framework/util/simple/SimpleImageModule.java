package bot.gabes_framework.util.simple;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;

import java.awt.*;
import java.util.List;

public class SimpleImageModule extends SimpleModule {
    private Image image;
    private String message;

    // TODO 2nd consturctor with String image
    public SimpleImageModule(Chatbot chatbot, List<String> regexList, Image image, String message) {
        super(chatbot, regexList);
        this.image = image;
        this.message = message;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
                chatbot.sendImageWithMessage(image, this.message);
                return true;
            }
        }
        return false;
    }
}
