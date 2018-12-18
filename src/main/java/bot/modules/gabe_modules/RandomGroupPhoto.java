package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.core.helper.interfaces.Util;
import bot.gabes_framework.simple.SimpleModule;
import org.openqa.selenium.WebElement;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomGroupPhoto extends SimpleModule {
    protected ArrayList<File> files;
    protected ImageIcon imageIcon;

    public RandomGroupPhoto(Chatbot chatbot, List<String> regexes) {
        super(chatbot, regexes);

        File f = new File("D:\\Dokumenty\\Data Backup\\Backup\\facebook-gabrielwawerski\\messages\\JakbedziewCorsieSekcjazjebow_96428634ae\\photos\\");
        files = new ArrayList<File>(Arrays.asList(f.listFiles()));
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        imageIcon = new ImageIcon(Util.GET_RANDOM(files).getPath());
        Image image = imageIcon.getImage();

        for (String regex : regexList) {
            if (match.equals(regex)) {
                chatbot.sendImageWithMessage(image, "Losuję...");
                /** {@link Message#sendMessageWithImage(WebElement, String, Image)}  */ // TODO try to run on new thread, trace calls and decide where to do it
                return true;
            }
        }
        return false;
    }
}
