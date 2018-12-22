package bot.core;

import bot.modules.gabe_modules.query.*;
import bot.modules.gabe_modules.Think;
import bot.modules.gabe_modules.*;
import bot.gabes_framework.core.libs.api.Module;
import bot.core.helper.misc.Human;
import bot.core.helper.misc.Message;
import bot.core.web_controller.WebController;
import bot.core.exceptions.MalformedCommandException;
import bot.modules.gabe_modules.work_in_progress.Popcorn;
import bot.modules.gabe_modules.Inspire;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class Chatbot {
    private final String version = "v0.3014";
    protected final HashMap<String, Module> modules = new HashMap<>();
    protected final WebController webController;
    private final ArrayList<Message> messageLog = new ArrayList<>();
    private final ArrayList<Human> people = new ArrayList<>();
    private final String shutdownCode = Integer.toString(new Random().nextInt(99999));
    private final LocalDateTime startupTime = LocalDateTime.now();
    private final Duration messageTimeout = Duration.ofMinutes(1);
    private final long refreshRate = 100;

    private boolean running = true;
    private String threadId;
    private Human me;
    private int modulesOnline;

    protected void loadModules() {
        modules.put("Commands", new Commands(this, List.of("cmd", "help", "regexList")));
        modules.put("Info", new Info(this, List.of("info", "uptime", "status")));
        modules.put("Shutdown", new Shutdown(this));
        modules.put("Sylwester", new Sylwester(this, "piosenki.txt"));
        modules.put("FeatureSuggest", new FeatureSuggest(this, "sugestie.txt")); // TODO add info

        modules.put("GoogleSearch", new GoogleSearch(this));
        modules.put("YoutubeSearch", new YoutubeSearch(this, List.of("youtube", "yt")));
        modules.put("WikipediaSearch", new WikipediaSearch(this, List.of("wiki", "w")));
        modules.put("MultiTorrentSearch", new MultiTorrentSearch(this));
        modules.put("AllegroSearch", new AllegroSearch(this, List.of("allegro")));
        modules.put("PyszneSearch", new PyszneSearch(this));

        modules.put("RandomGroupPhoto", new RandomGroupPhoto(this, List.of("random", "r")));
        modules.put("SimpleWeather", new SimpleWeather(this, List.of("pogoda", "p")));
        modules.put("Popcorn", new Popcorn(this, List.of("popcorn", "rajza")));
        modules.put("KartaPulapka", new KartaPulapka(this, List.of("karta", "kartapulapka", "myk"), "kartapulapka.jpg"));
        modules.put("Inspire", new Inspire(this));
        modules.put("Roll", new Roll(this));
        modules.put("Think", new Think(this));

        modules.put("EightBall", new EightBall(this, "responses.txt"));
        modules.put("JebacLeze", new JebacLeze(this, List.of("jebacleze", "leze"),
                "responses.txt"));
        modules.put("LezeSpam", new LezeSpam(this, List.of("spam", "kurwa"),
                "responses.txt"));



//        modules.put("ImageFromUrl", new ImageFromUrl(this));
//        modules.put("TorrentSearch", new TorrentSearch(this));
//        modules.put("Memes", new Memes(this));

//        modules.put("Dogs", new Dogs(this));

//        modules.put("PollCreate", new PollCreate(this)); // work in progress
    }

    public void reloadModules() {
        loadModules();
    }


    public Chatbot(String username, String password, String threadId, boolean debugMode, boolean silentMode, boolean debugMessages, boolean headless, boolean maximised) {
        webController = new WebController(this, debugMessages, headless, maximised);
        run(username, password, threadId, debugMode, silentMode);
    }

    public Chatbot(String configName, String threadId, boolean debugMode, boolean silentMode, boolean debugMessages, boolean headless, boolean maximised) {
        webController = new WebController(this, debugMessages, headless, maximised);
        runFromConfigWithThreadId(configName, threadId, debugMode, silentMode);
    }

    public Chatbot(String configName, boolean debugMode, boolean silentMode, boolean debugMessages, boolean headless, boolean maximised) {
        webController = new WebController(this, debugMessages, headless, maximised);
        runFromConfig(configName, debugMode, silentMode);
    }

    public Chatbot() {
        webController = new WebController(this, false, false, false);
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

    public void reRun(String username, String password, String threadId, boolean debugMode, boolean silentMode) {
        run(username, password, threadId, debugMode, silentMode);
    }

    private void run(String username, String password, String threadId, boolean debugMode, boolean silentMode) {
        this.threadId = threadId;

        // Output Shutdown code
        System.out.println("PcionBot " + version);
        System.out.println("Shutdown: " + shutdownCode);
        System.out.println("-----------------");
        System.out.println("Ładowanie modułów...");
        loadModules();

        int totalModules = modules.size();
        modulesOnline = 0;
        List<String> modulesOffline = null;

        for (Module module : modules.values()) {
            if (module.isOnline()) {
                module.echoOnline();
                modulesOnline++;
            } else {
                if (modulesOffline == null) {
                    modulesOffline = List.of(module.getClass().getSimpleName());
                }
                modulesOffline.add(module.getClass().getSimpleName());
            }
        }

        if (modulesOnline == totalModules) {
            System.out.println("Wszystkie " + totalModules + " moduły pomyślnie załadowane.");
        } else {
            System.out.print("\nNie wszystkie moduły zostały pomyślnie załadowane."
                    + "\nModuły niedostępne w bieżącej sesji: ");
            for (String module : modulesOffline) {
                System.out.print(module.getClass().getSimpleName() + " ");
            }
            System.out.println();
            System.out.println(modulesOnline + " / " + totalModules + "(" + (double)(totalModules - (modulesOnline *  totalModules)) / 100 + "%)");
        }

        System.out.println("-----------------");

        //Run setup
        webController.login(username, password);
        System.out.println("Messenger - zalogowano.");
        webController.gotoFacebookThread(threadId);
        System.out.println("Przełączam na wątek nr. " + threadId);

        //Wait until messages have loaded
        webController.waitForMessagesToLoad();
        System.out.println("Wiadomości załadowane.");

        //Init message
        if (!silentMode) {
            initMessage();
        }
        System.out.println("-----------------\n");
        System.out.println("PCIONBOT ONLINE");

        while (running) {
            try {
                webController.waitForNewMessage();
                Message newMessage = webController.getLatestMessage();
                messageLog.add(newMessage);

                if (debugMode) {
                    System.out.println(newMessage);
                }

                //Handle options
                try {
                    for (Module module : modules.values()) {
                        module.process(newMessage);
                    }
                } catch (MalformedCommandException e) {
                    sendMessage("There seems to be an issue with your command");
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
        webController.sendMessage("PcionBot " + getVersion() + " online!"
                + "\nZaładowane moduły: " + modulesOnline
                + "\n\n!suggest"
                + "\nWpisz !cmd aby zobaczyć listę komend");
        System.out.println();
    }

    public int getModulesOnline() {
        return modulesOnline;
    }

    public void sendMessage(String message) {
        webController.sendMessage(message);
    }

    public void sendImageWithMessage(String image, String message) {
        webController.sendImageWithMessage(image, message);
    }

    public void sendImageWithMessage(Image image, String message) {
        webController.sendMessage(new Message(me, message, image));
    }

    public void sendImage(Image image) {

    }

    public void sendLoadedImage(Image image) {

    }

    public void sendImageFromURLWithMessage(String url, String message) {
        webController.sendImageFromURLWithMessage(url, message);
    }

    public void sendMessage(Message message) {
        webController.sendMessage(message);
    }

    public void sendImageUrlWaitToLoad(String imageUrl) {
        webController.sendImageUrlWaitForLoad(imageUrl);
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
