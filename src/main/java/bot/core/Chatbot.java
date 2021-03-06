package bot.core;

import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.gabes_framework.core.util.Listener;
import bot.core.gabes_framework.core.util.Utils;
import bot.modules.gabe.text.B;
import bot.modules.gabe.util.info.*;
import bot.modules.gabe.point_system.PointSystem;
import bot.modules.gabe.rand.Roll;
import bot.modules.gabe.image.KartaPulapka;
import bot.modules.gabe.rand.EightBall;
import bot.modules.gabe.util.search.*;
import bot.modules.gabe.util.*;
import bot.modules.gabe.image.Think;
import bot.core.gabes_framework.core.api.Module;
import bot.core.hollandjake_api.helper.misc.Human;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.web_controller.WebController;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.modules.gabe.image.Popcorn;
import bot.modules.gabe.rand.image.RandomGroupPhoto;
import bot.modules.gabe.rand.image.RandomKwejk;
import bot.modules.gabe.rand.JebacLeze;
import bot.modules.gabe.rand.LezeSpam;
import bot.modules.gabe.util.twitch_emotes.TwitchEmotes;
import bot.modules.gabe.util.Mp3Tube;
import bot.modules.gabe.rand.image.RandomWTF;
import bot.modules.gabe.rand.image.RandomWykop;
import bot.modules.gabe.point_system.submodule.SlotMachine;
import bot.modules.gabe.util.TextToImage;
import bot.modules.gabe.util.Insult;
import bot.modules.gabe.util.Mention;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static bot.core.PcionBot.*;

public class Chatbot {
    private final String version = "v0.34";
    protected final HashMap<String, Module> modules = new HashMap<>();
    public final WebController webController;
    private final ArrayList<Message> messageLog = new ArrayList<>();
    private final ArrayList<Human> people = new ArrayList<>();
    private final String shutdownCode = Integer.toString(new Random().nextInt(99999));
    private final LocalDateTime startupTime = LocalDateTime.now();
    private final Duration messageTimeout = Duration.ofMinutes(1);
    private final long refreshRate = 100;
    private DBConnection dbConnection;

    private boolean running = true;
    private String threadId;
    private Human me;
    private int modulesOnline;
    private int totalModules;
    private boolean logMode = false;

    private Listener listener;

    protected void loadModules() {
        listener = new Listener(this);
        modules.put("Commands", new Commands(this));
        modules.put("Info", new Info(this));
        modules.put("Shutdown", new Shutdown(this));
//        modules.put("FeatureSuggest", new FeatureSuggest(this,
//                "sugestie.txt"));

        modules.put("MultiTorrentSearch", new MultiTorrentSearch(this));
        modules.put("WikipediaSearch", new WikipediaSearch(this));
        modules.put("YoutubeSearch", new YoutubeSearch(this));
        modules.put("GoogleSearch", new GoogleSearch(this));
        modules.put("AllegroSearch", new AllegroSearch(this));
        modules.put("PyszneSearch", new PyszneSearch(this));

        modules.put("RandomGroupPhoto", new RandomGroupPhoto(this));
        modules.put("SimpleWeather", new SimpleWeather(this));
        modules.put("Popcorn", new Popcorn(this));
        modules.put("KartaPulapka", new KartaPulapka(this));
//        modules.put("Inspire", new Inspire(this));
        modules.put("Roll", new Roll(this));
        modules.put("Think", new Think(this));
        modules.put("EightBall", new EightBall(this, "responses.txt"));
        modules.put("JebacLeze", new JebacLeze(this, "responses.txt"));
        modules.put("LezeSpam", new LezeSpam(this, "responses.txt"));
        modules.put("RandomKwejk", new RandomKwejk(this));
        modules.put("TwitchEmotes", new TwitchEmotes(this));
        modules.put("Mp3Tube", new Mp3Tube(this));
        modules.put("B", new B(this));
        // !atg taxi numery telefonow
        modules.put("ATG", new ATG(this, "\u2705 OPEN")); // ✅ OPEN ❌ CLOSED
        modules.put("RandomWykop", new RandomWykop(this));
        modules.put("RandomWTF", new RandomWTF(this));
        modules.put("Mention", new Mention(this)); // TODO fix - crashed last time
        modules.put("SlotMachine", new SlotMachine(this));
        modules.put("PointSystem", new PointSystem(this));
//        modules.put("Reactions", new Reactions(this));
        modules.put("TextToImage", new TextToImage(this));
        modules.put("Insult", new Insult(this));
    }

    // TODO refactor
    public List<String> getRegexes() {
        List<String> list
                = List.of("cmd", "help", "info", "staty", "uptime", "echo", "shutdown", "suggest",
                "pomysl", "suggest (.*)", "pomysl (.*)", "torrent (.*)", "t (.*)", "wiki (.*)", "youtube (.*)",
                "yt (.*)", "google (.*)", "g (.*)", "g help", "g leze", "google", "g", "allegro (.*)", "pyszne help",
                "pyszne", "pyszne (.*)", "pyszne haianh", "pyszne hai-anh", "pyszne hai", "pyszne mariano",
                "pyszne italiano", "pyszne football", "pyszne footbal", "pyszne footballpizza", "r", "random", "pogoda",
                "p", "popcorn", "rajza", "karta", "kartapulapka", "myk", "roll", "roll (\\d+)", "think", "think (\\d+)",
                "8ball (.*)", "ask (.*)", "8 (.*)", "jebacleze", "jebac leze", "spam", "kurwa", "kwejk", "kw",
                "mp3 (.*)", "mp3", "b (.*)", "atg", "wykop", "wy", "wtf");
        return list.stream().map(Utils::TO_REGEX).collect(Collectors.toList());
    }

    public void reloadModules() {
        loadModules();
    }

    public Chatbot(String username, String password,
                   String threadId, boolean debugMode,
                   boolean silentMode, boolean debugMessages,
                   boolean headless, boolean maximised,
                   boolean logMode) {
        dbConnection = DBConnection.getInstance();
        this.logMode = logMode;
        webController = new WebController(this, debugMessages, headless, maximised, dbConnection);
        run(username, password, threadId, debugMode, silentMode);
    }

    public Chatbot(String configName, String threadId, boolean debugMode, boolean silentMode, boolean debugMessages, boolean headless, boolean maximised) {
        dbConnection = DBConnection.getInstance();
        webController = new WebController(this, debugMessages, headless, maximised, dbConnection);
        runFromConfigWithThreadId(configName, threadId, debugMode, silentMode);
    }

    public Chatbot(String configName, boolean debugMode, boolean silentMode, boolean debugMessages, boolean headless, boolean maximised) {
        dbConnection = DBConnection.getInstance();
        webController = new WebController(this, debugMessages, headless, maximised, dbConnection);
        runFromConfig(configName, debugMode, silentMode);
    }

    public Chatbot() {
        dbConnection = DBConnection.getInstance();
        webController = new WebController(this, false, false, false, dbConnection);
        runFromConfig("config", false, false);
    }

    private void runFromConfig(String configName, boolean debugMode, boolean silentMode) {
        ResourceBundle config = ResourceBundle.getBundle(configName);
        String threadId = config.getString("threadId");

        runFromConfigWithThreadId(configName, threadId, debugMode, silentMode);
    }

    private void runFromConfigWithThreadId(String configName, String threadId, boolean debugMode, boolean silentMode) {
        ResourceBundle config = ResourceBundle.getBundle(configName);
        String username = config.getString("username");
        String password = config.getString("password");

        run(username, password, threadId, debugMode, silentMode);
    }

    private void init(String username, String password, String threadId, boolean debugMode, boolean silentMode) {
        log("Initializing...");
        log("Loading modules...");

        loadModules();

        log("Echo modules...");

        totalModules = modules.size();
        modulesOnline = 0;
        ArrayList<String> modulesOffline = new ArrayList<>();

        for (Module module : modules.values()) {
            if (module.isOnline()) {
                modulesOnline++;
            } else {
                modulesOffline.add(module.getClass().getSimpleName());
            }
            module.echoOnline();
        }

        System.out.println("-----------------");
        if (modulesOnline < totalModules) {
            System.out.println("Not all modules have been loaded.\nModules unavailable this session: ");
            for (String module : modulesOffline) {
                System.out.print(module + ", ");
            }
            DecimalFormat df = new DecimalFormat("###");
            df.setRoundingMode(RoundingMode.HALF_UP);
            double onlinePercent = (double) modulesOnline * 100 / totalModules;
            System.out.println(modulesOnline + "/" + totalModules + "(" + df.format(onlinePercent) + "%)");
        } else {
            System.out.println("ONLINE | " + modulesOnline + "/" + totalModules);
        }

        log("Initializing platform...");
        log("Logging in...");
        webController.login(username, password);

        log("Successfully logged in.\nTarget ID: " + threadId);
        System.out.print("Looking for favourites... ");

        String msg = "";
        if (threadId.equals(GRUPKA_ID)) {
            System.out.print("found.\n\n");
            msg += "Grupka (" + threadId + ")";
        }  else if (threadId.equals(PATRO_ID)) {
            System.out.print("found.\n\n");
            msg += "Patro (" + threadId + ")";
        } else {
            System.out.print("not found.\n\n");
            msg += threadId;
        }

        log("Switching to " + msg);
        webController.gotoFacebookThread(threadId);

        log("Switched to " + msg);
        log("Waiting for messages to load...");
        webController.waitForMessagesToLoad();

        log("Messages loaded.");
        log("Finished loading.");

        log("PcionBot online.\nRunning from config:\n"
                + "max wait time  : " + WebController.TIMEOUT_IN_SEC + " sec.\n"
                + "poll sleep time: " + getRefreshRate() + " ms.");

        System.out.println("----------------");
        System.out.println("PcionBot " + version);
        System.out.println("Shutdown:  " + shutdownCode);
        System.out.println("----------------");

        //Init message
        if (!silentMode) {
            initMessage();
        }
    }

    private void log(String message) {
        Date dateNow = new Date();
        String timeNow = dateNow.toString();
        int dateLength = timeNow.length();
        final int datePostfix = 9;

        timeNow = timeNow.substring(0, dateLength - datePostfix);
        System.out.print("- " + timeNow + "\n");
        System.out.println(message);
        System.out.print("\n");
    }

    private void weather() {

    }

    private void run(String username, String password, String threadId, boolean debugMode, boolean silentMode) {
        this.threadId = threadId;
        init(username, password, threadId, debugMode, silentMode);
        Calendar now = Calendar.getInstance();

        while (running) {
            // todo weather every 6 hours
            // Weather now 12:00 (Today) 01.02.18
            // Next weather forecast at: 16:00 (Today) 01.02.18
//            now.
            try {
                webController.waitForNewMessage();
                Message newMessage = webController.getLatestMessage();

                if (newMessage == null) {
                    continue;
                }

                if (newMessage.getMessage().length() > 1000) {
                    System.out.println("Message too long, ignoring...");
                    continue;
                }
                messageLog.add(newMessage);

                if (debugMode) {
                    System.out.println(newMessage);
                }

                if (logMode) {
                    try { listener.process(newMessage); }
                    catch (MalformedCommandException e) { e.printStackTrace(); }
                } else {
                    try {
                        for (Module module : modules.values()) {
                            module.process(newMessage);
                        }
                    } catch (MalformedCommandException e) {
                        sendMessage("There seems to be an issue with your command");
                    }
                }
            } catch (TimeoutException e) {
                if (debugMode) {
                    System.out.println("No messaged received in the last " + messageTimeout);
                }
            } catch (WebDriverException e) {
                e.printStackTrace();
                System.out.println("Browser was closed, program is ended");
                webController.quit(true);
                System.exit(1);
            }

        }
    }

    public String getVersion() {
        return version;
    }

    protected void initMessage() {
        webController.sendMessage("PcionBot " + getVersion() + " ONLINE \u2705\n"
                + "Załadowane moduły:  " + Emoji.NEW_BUTTON + " " + modulesOnline + "/" + totalModules
                + "\n" + Emoji.NEW_BUTTON + "!pull"
                + "\n" + "!give <uzytkownik> <pkt>"
                + "\n" + "!mp3 !mp3 <youtube url> generuje link do pobrania!"
                + "\n"
                + "\n" + Emoji.INFO + " Wpisz !cmd !cmd2 aby zobaczyć listę komend.");
    }

    public String getModulesOnline() {
        return modulesOnline + " / " + totalModules;
    }

    public void sendMessage(String message) {
        webController.sendMessage(message);
    }

    public void sendImageWithMessage(Image image, String message) {
        webController.sendMessage(new Message(me, message, image));
    }

    public void sendMentionMessage(String user, String message) {
        webController.sendMentionMessage(user, message);
    }

    public void sendMentionMessage(String user) {
        webController.sendMentionMessage(user);
    }

    public void sendImageFromURLWithMessage(String url, String message) {
        webController.sendImageFromURLWithMessage(url, message);
    }

    public void sendMessage(Message message) {
        webController.sendMessage(message);
    }

    public void sendImageUrlWaitToLoad(String imageUrl) {
        webController.sendImageUrlWaitToLoad(imageUrl);
    }

    public void sendImageUrlLoadURL(String imageUrl) {
        webController.sendImageUrlLoadURL(imageUrl);
    }

    public String appendRootPath(String path) {
        return "/" + path;
    }

    public ArrayList<Message> getMessageLog() {
        return messageLog;
    }

    public Duration getMessageTimeout() {
        return messageTimeout;
    }

    public Human getMe() {
        return me;
    }

    public void setMe(Human me) {
        this.me = me;
    }

    public ArrayList<Human> getPeople() {
        return people;
    }

    public String getThreadId() {
        return threadId;
    }

    public HashMap<String, Module> getModules() {
        return modules;
    }

    public LocalDateTime getStartupTime() {
        return startupTime;
    }

    public String getShutdownCode() {
        return shutdownCode;
    }

    public long getRefreshRate() {
        return refreshRate;
    }

    public boolean containsCommand(Message message) {
        for (Module module : modules.values()) {
            if (!module.getMatch(message).equals("")) {
                return true;
            }
        }
        return false;
    }

    public void screenshot() {
        webController.screenshot();
    }

    public void quit() {
        webController.quit(true);
    }
}
