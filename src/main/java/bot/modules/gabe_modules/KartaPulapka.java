package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.simple.SimpleModule;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class KartaPulapka extends SimpleModule {
    private final Image kartaPulapka;

    public KartaPulapka(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
        kartaPulapka = new ImageIcon("modules/" + getClass().getSimpleName() + "/" + "kartapulapka.jpg").getImage();
        System.out.println("loaded");
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : regexList) {
            if (match.equals(command)) {
                chatbot.sendImageWithMessage(kartaPulapka, "Wyciągam...");
                return true;
            }
        }
        return false;
    }
}
