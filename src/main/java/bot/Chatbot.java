package bot;

import bot.modules.gabe_modules.Weather.Eightball;
import bot.modules.gabe_modules.Weather.SimpleWeather;
import bot.utils.gabe_modules.interfaces.Module;
import bot.utils.bot.helper.helper_class.Human;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.bot.web_controller.WebController;
import bot.utils.bot.exceptions.MalformedCommandException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class Chatbot {
    private final String version = "v0.20";
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

    protected void loadModules() {
        modules.put("SimpleWeather", new SimpleWeather(this, List.of("pogoda", "p", "weather", "w")));
        modules.put("Eightball", new Eightball(this, List.of("8ball", "ask", "?"), "responses.txt"));

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

    private void run(String username, String password, String threadId, boolean debugMode, boolean silentMode) {
        //Output Shutdown code
        System.out.println("Shutdown code: " + shutdownCode);

        this.threadId = threadId;
        loadModules();

        //Run setup
        webController.login(username, password);
        webController.gotoFacebookThread(threadId);

        //Wait until messages have loaded
        webController.waitForMessagesToLoad();

        //Init message
        if (!silentMode) {
            initMessage();
        }
        System.out.println("System is running");

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
                        System.out.println("called");
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
        webController.sendMessage("PcionBot " + getVersion() + " jest online!");
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

    public void sendImageFromURLWithMessage(String url, String message) {
        webController.sendImageFromURLWithMessage(url, message);
    }

    public void sendMessage(Message message) {
        webController.sendMessage(message);
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
