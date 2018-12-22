package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.PcionBot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.core.helper.interfaces.Util;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.simple.SimpleModule;
import com.github.imgur.ImgUr;
import com.github.imgur.api.image.ImageResponse;
import com.github.imgur.api.upload.UploadRequest;
import org.openqa.selenium.WebElement;
import org.scribe.builder.api.ImgUrApi;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomGroupPhoto extends SimpleModule {
    private ArrayList<File> files;
    Image image;
    private ImageIcon imageIcon;
    private List<String> alreadySeen;

    private final long TIMEOUT = 15000;

    private final String PHOTOS_PATH = "D:\\Dokumenty\\Data Backup\\Backup\\facebook-gabrielwawerski\\messages\\JakbedziewCorsieSekcjazjebow_96428634ae\\photos\\";

    public RandomGroupPhoto(Chatbot chatbot, List<String> regexes) {
        super(chatbot, regexes);
        File f = new File(PHOTOS_PATH);
        files = new ArrayList<File>(Arrays.asList(f.listFiles()));

        imageIcon = new ImageIcon(Util.GET_RANDOM(files).getPath());
        image = imageIcon.getImage();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
                imageIcon = new ImageIcon(Util.GET_RANDOM(files).getPath());
                image = imageIcon.getImage();

                chatbot.sendImageWithMessage(image, Utils.EMOJI_SHUFFLE + " Losuję...");
                /** {@link Message#sendMessageWithImage(WebElement, String, Image)}  */ // TODO try to run on new thread, trace calls and decide where to do it
                return true;
            }
        }
        image = imageIcon.getImage();
        return false;
    }
}
