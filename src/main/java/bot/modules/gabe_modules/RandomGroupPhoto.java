package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.bot.helper.helper_interface.Util;
import bot.utils.gabe_modules.modules_base.ModuleBase;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomGroupPhoto extends ModuleBase {
    protected ArrayList<File> files;
    protected ImageIcon imageIcon;

    public RandomGroupPhoto(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        File f = new File("D:\\Dokumenty\\Data Backup\\Backup\\facebook-gabrielwawerski\\messages\\JakbedziewCorsieSekcjazjebow_96428634ae\\photos\\");
        files = new ArrayList<File>(Arrays.asList(f.listFiles()));
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        imageIcon = new ImageIcon(Util.GET_RANDOM(files).getPath());
        Image image = imageIcon.getImage();

        for (String command : commands) {
            if (match.equals(command)) {
                chatbot.sendImageWithMessage(image, "Losuję...");
                return true;
            }
        }
        return false;
    }
}
