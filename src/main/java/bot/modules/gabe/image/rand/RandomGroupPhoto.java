package bot.modules.gabe.image.rand;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.util.Utils;
import org.openqa.selenium.WebElement;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bot.core.gabes_framework.util.Utils.TO_REGEX;

public class RandomGroupPhoto extends ModuleBase {
    private ArrayList<File> files;
    Image image;
    private ImageIcon imageIcon;
    private List<String> alreadySeen;
    private LocalTime now;
    private LocalTime timeoutRelease;

    private final String PHOTOS_PATH = "D:\\Dokumenty\\Data Backup\\Backup\\facebook-gabrielwawerski\\messages\\JakbedziewCorsieSekcjazjebow_96428634ae\\photos\\";
    private final int MESSAGE_TIMEOUT = 5;

    private final String RANDOM_REGEX = TO_REGEX("andom");
    private final String R_REGEX = TO_REGEX("r");
    private final String R = ("r");


    public RandomGroupPhoto(Chatbot chatbot) {
        super(chatbot);

        File f = new File(PHOTOS_PATH);
        files = new ArrayList<>(Arrays.asList(f.listFiles()));
        now = LocalTime.now();
        int seconds;

        if (now.getSecond() + (MESSAGE_TIMEOUT - 1) > 59) {
            seconds = 59;
        } else {
            seconds = now.getSecond() + (MESSAGE_TIMEOUT - 1);
        }
        timeoutRelease = LocalTime.of(now.getHour(), now.getMinute(), seconds);
        newRandomImage();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOr(RANDOM_REGEX, R_REGEX, R)) {
//            if (now.isBefore(timeoutRelease)) {
////                    List<String> randomResponses
////                            = List.of("Musisz jeszcze poczekać.",
////                            "nie spamuj leze",
////                            "bo cie zaraz kasprzak wypierdoli");
//                chatbot.sendMessage("Musisz chwilę poczekać.");
//                return false;
//            } else {
                chatbot.sendImageWithMessage(image, Utils.SHUFFLE_EMOJI + " Losuję...");
                /** {@link Message#sendMessageWithImage(WebElement, String, Image)}  */ // TODO try to run on new thread, trace calls and decide where to do it
                timeoutRelease = LocalTime.of(now.getHour(), now.getMinute(), now.getSecond() + MESSAGE_TIMEOUT - 1);
                newRandomImage();
                return true;
//            }
        }
        return false;
    }

    private void newRandomImage() {
        imageIcon = new ImageIcon(Util.GET_RANDOM(files).getPath());
        image = imageIcon.getImage();
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, RANDOM_REGEX, R_REGEX, R);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(RANDOM_REGEX, R_REGEX, R);
    }
}
