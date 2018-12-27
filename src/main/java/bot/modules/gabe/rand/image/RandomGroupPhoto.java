package bot.modules.gabe.rand.image;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.core.Utils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static bot.core.gabes_framework.core.Utils.TO_REGEX;

/**
 * <p><p>v1.1
 * <br>- added timeout for rolling new image
 *
 * <p>v2.0
 * <br>- files are now uploaded to Cloudinary - message is now only an attachment with image!
 *
 * @version 2.0
 * @since 0.30
 */
public class RandomGroupPhoto extends ModuleBase {
    private Cloudinary cloudinary;

    private ArrayList<File> files;
    private List<String> alreadySeen; // TODO implement seen already
    private String currentFileUrl;

    private long now;
    private long timeoutRelease;
    private static final long TIMEOUT = 5000;


    private static final String PHOTOS_PATH = "D:\\Dokumenty\\Data Backup\\Backup\\facebook-gabrielwawerski\\messages\\JakbedziewCorsieSekcjazjebow_96428634ae\\photos\\";

    private static final List<String> TIMEOUT_RESPONSES = List.of(
            "Musisz jeszcze poczekać.", "Leze nie spamuj.",
            "Bo cie kaspe zaraz wypierdoli.", "Musisz chwilę poczekać.",
            "Poczekaj chwilę.", "Chwila...", "Bo mnie przegrzejesz!",
            "Nie bądź taki hop.", "Jebneee", "Bo się przegrzeje!");

    private final String RANDOM_REGEX = TO_REGEX("random");
    private final String R_REGEX = TO_REGEX("r");
    private final String R = ("r");


    public RandomGroupPhoto(Chatbot chatbot) {
        super(chatbot);
        File f = new File(PHOTOS_PATH);
        files = new ArrayList<>(Arrays.asList(f.listFiles()));
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "drpmvrlik",
                "api_key", "844188849853993",
                "api_secret", "yutae3uoXkDwxKhhc9u2eEs5GE8"));

        String fileUrl;
        if ((fileUrl = uploadFile()) == null) {
            System.out.println("Nie udało się zdobyć URL obrazka. Próbuję ponownie...");
            fileUrl = uploadFile();
            if (fileUrl == null) {
                System.out.println("Niepowodzenie.");
            }
        } // zamienic na while jesli beda bledy
        currentFileUrl = fileUrl;
        now = new Date().getTime();
        timeoutRelease = now + TIMEOUT;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        now = new Date().getTime();

        if (isOr(RANDOM_REGEX, R_REGEX, R)) {
            if (now < timeoutRelease) {
                chatbot.sendMessage(randTimeoutMsg());
                return false;
            } else {
                chatbot.sendImageUrlWaitToLoad(currentFileUrl);
                currentFileUrl = uploadFile();
                timeoutRelease = new Date().getTime() + TIMEOUT;
                return true;
            }
        }
        return false;
    }

    private String uploadFile() {
        Map uploadResult = null;
        File random = null;
        int maxTries = 5;
        int curr = 0;

        while (curr < maxTries) {
            random = Util.GET_RANDOM(files);
            if (random.exists()) {
                break;
            }
            curr++;
        }

        try {
            uploadResult = cloudinary.uploader().upload(random, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (uploadResult == null) {
            return null;
        } else {
            return (String)uploadResult.get("url");
        }
    }

    private String randTimeoutMsg() {
        int min = 0;
        int max = TIMEOUT_RESPONSES.size() - 1;
        int range = (max - min) + 1;
        int result = (int) (Math.random() * range) + min;

        return TIMEOUT_RESPONSES.get(result);
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
