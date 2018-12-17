package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.gabe_modules.util.ModuleBase;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class KartaPulapka extends ModuleBase {
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
