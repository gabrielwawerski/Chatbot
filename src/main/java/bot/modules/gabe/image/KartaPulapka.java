package bot.modules.gabe.image;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.simple.SimpleModule;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class KartaPulapka extends SimpleModule {
    private final Image kartaPulapka;

    public KartaPulapka(Chatbot chatbot, List<String> commands, String fileName) {
        super(chatbot, commands);
        kartaPulapka = new ImageIcon("modules/" + getClass().getSimpleName() + "/" + fileName).getImage();
        System.out.println("loaded");
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : regexList) {
            if (match.equals(command)) {
                chatbot.sendImageWithMessage(kartaPulapka, "");
                return true;
            }
        }
        return false;
    }
}
