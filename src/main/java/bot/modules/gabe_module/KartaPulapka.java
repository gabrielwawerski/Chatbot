package bot.modules.gabe_module;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.utils.gabe_modules.util.module_library.SimpleModule;

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

        for (String command : commands) {
            if (match.equals(command)) {
                chatbot.sendImageWithMessage(kartaPulapka, "Wyciągam...");
                return true;
            }
        }
        return false;
    }
}
