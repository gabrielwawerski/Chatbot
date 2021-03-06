package bot.core.hollandjake_api.web_controller;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.hollandjake_api.helper.misc.Human;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.helper.interfaces.ScreenshotUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;

import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;

import static bot.core.hollandjake_api.helper.interfaces.Util.CLIPBOARD;
import static bot.core.hollandjake_api.helper.interfaces.Util.PASTE;
import static bot.core.hollandjake_api.helper.interfaces.XPATHS.*;

public class WebController {
    public static final int TIMEOUT_IN_SEC = 10;
    private Chatbot chatbot;
    private final ChromeDriverService service;
    private final WebDriver webDriver;
    private final Actions keyboard;
    private final WebDriverWait wait;
    private final WebDriverWait messageWait;
    private final boolean debugMessages;
    private final DBConnection db;

    private final int imgLoadTime = 3000;

    public WebController(Chatbot chatbot, boolean debugMessages, boolean headless, boolean maximised, DBConnection db) {
        this.chatbot = chatbot;
        this.db = db;
        this.debugMessages = debugMessages;
        File driver;
        ClassLoader classLoader = getClass().getClassLoader();

        driver = System.getProperty("os.name").toLowerCase().contains("windows") ?
                new File("C:/chromedriver.exe") :
                new File(classLoader.getResource("drivers/linux/chromedriver").getFile());
        driver.setExecutable(true);

        //Create service
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(driver)
                .usingAnyFreePort()
                .build();
        try {
            service.start();
        } catch (IOException e) {
            System.out.println("Cannot start ChromeDriverService.");
            e.printStackTrace();
        }

        //Setup drivers
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("mute-audio", "console");
        if (headless) {
            chromeOptions.addArguments("headless", "window-size=1920,1080");
        } else if (maximised) {
            chromeOptions.addArguments("start-maximized");
        }
        webDriver = new RemoteWebDriver(service.getUrl(), chromeOptions);
        keyboard = new Actions(webDriver);

        //Setup waits
        wait = new WebDriverWait(webDriver, TIMEOUT_IN_SEC);
        messageWait = new WebDriverWait(webDriver, chatbot.getMessageTimeout().getSeconds(), chatbot.getRefreshRate());

        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            this.db.close();
//            service.stop();
//            webDriver.quit();

            System.out.println("Coś poszło nie tak:");
            e.printStackTrace();
            quit(false);
//            this.chatbot = PcionBot.newBot();

//            if (PcionBot.SILENT_MODE || PcionBot.LOG_MODE) {
//                e.printStackTrace();
//                quit(false);
//            } else {
//                e.printStackTrace();
//                quit(true);
//            }
        });
    }

    public void quit(boolean withMessage) {
        if (withMessage) {
//            sendMessage("Przechodzę offline.");
        }
        db.close();
        webDriver.quit();
        System.exit(0);
    }

    //region Login
    public void login(String username, String password) {
        //Goto page
        webDriver.get("https://www.messenger.com");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(LOGIN_EMAIL)));
        webDriver.findElement(By.xpath(LOGIN_EMAIL)).sendKeys(username);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(LOGIN_PASS)));
        webDriver.findElement(By.xpath(LOGIN_PASS)).sendKeys(password);

        webDriver.findElement(By.xpath(COOKIES_CLOSE)).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(LOGIN_BUTTON)));
        webDriver.findElement(By.xpath(LOGIN_BUTTON)).click();
    }

    public void gotoFacebookThread(String threadId) {
        chatbot.setMe(Human.createForBot(getMyRealName()));
        webDriver.get("https://www.messenger.com/t/" + threadId);
    }

    public void switchThreads(String threadId) {
        chatbot.setMe(Human.createForBot(getMyRealName())); // todo check if necessary
        webDriver.get("https://www.messenger.com/t/" + threadId);
        waitForMessagesToLoad();
    }
    //endregion

    //region Sending messages
    public void sendMessage(Message message) {
        int myMessageCount = getNumberOfMyMessagesDisplayed();

        WebElement inputBox = selectInputBox();
        if (debugMessages) {
            message.sendDebugMessage(inputBox);
        } else {
            message.sendMessage(inputBox);
        }
        //Wait for message to be sent
        // TODO check if can be removed for less errors
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath(MESSAGES_MINE),
                myMessageCount));
    }

    private WebElement selectInputBox() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(INPUT_FIELD)));
        WebElement inputBoxElement = webDriver.findElement(By.xpath(INPUT_FIELD));

        try {
            inputBoxElement.click();
            return inputBoxElement;
        } catch (WebDriverException e) {
            webDriver.navigate().refresh();
            return selectInputBox();
        }
    }

    /**
     * Pastes image url to input box and sends it as soon as it loads.
     *
     * @author hollandjake
     * @author gabrielwawerski
     */
    public void sendImageUrlWaitToLoad(String imageUrl) {
        int myMessageCount = getNumberOfMyMessagesDisplayed();
        WebElement inputBox = selectInputBox();

        /** {@link Message#sendMessage(WebElement, String)}  */
        // wkleja link do zdjęcia
        CLIPBOARD.setContents(new StringSelection((imageUrl)), null);

        // TODO move some code to Message class where an object of it should be created
        // paste link
        inputBox.sendKeys(PASTE);
        // waits until image fully loads as an attachment
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(LOADED_THUMBNAIL))).isDisplayed();

        // clear input box
        inputBox.sendKeys(Keys.CONTROL + "a");
        inputBox.sendKeys(Keys.DELETE);

        inputBox.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath(MESSAGES_MINE),
                myMessageCount));
    }

    public void kickUser() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(INFO_BUTTON)));
        WebElement infoMenu = webDriver.findElement(By.xpath(INFO_BUTTON));

        infoMenu.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(LOADED_THUMBNAIL))).isDisplayed();

        //_364g
    }

    public void sendImageUrlLoadURL(String imageUrl) {
        int myMessageCount = getNumberOfMyMessagesDisplayed();
        WebElement inputBox = selectInputBox();

        /** {@link Message#sendMessage(WebElement, String)}  */
        // wkleja link do zdjęcia
        CLIPBOARD.setContents(new StringSelection((imageUrl)), null);

        // TODO move some code to Message class where an object of it should be created
        // paste link
        inputBox.sendKeys(PASTE);
        // waits until image fully loads as an attachment
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(LOADED_THUMBNAIL))).isDisplayed();

        inputBox.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath(MESSAGES_MINE),
                myMessageCount));
    }

    public void sendMentionMessage(String USER) {
        WebElement inputBox = selectInputBox();
        inputBox.sendKeys("@");
        for (int i = 0; i < 5; i++) {
            inputBox.sendKeys(Character.toString(USER.charAt(i)));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        inputBox.sendKeys(Keys.ENTER);
        inputBox.sendKeys(Keys.SPACE);
//        setClipboardContents(message);
//        inputBox.sendKeys(PASTE);
        inputBox.sendKeys(Keys.ENTER);
    }

    public void sendMentionMessage(String USER, String message) {
        WebElement inputBox = selectInputBox();
        inputBox.sendKeys("@");
        for (int i = 0; i < 8; i++) {
            inputBox.sendKeys(Character.toString(USER.charAt(i)));
            try {
                Thread.sleep(50); // TODO increase if not mentioning properly
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        inputBox.sendKeys(Keys.ENTER);
        setClipboardContents(message);
        inputBox.sendKeys(PASTE);
        inputBox.sendKeys(Keys.ENTER);
    }

    protected void setClipboardContents(String contents) {
        CLIPBOARD.setContents(new StringSelection(contents), null);
    }

    public void screenshot() {
        ScreenshotUtil.capture(webDriver);
    }

    public void sendMessage(String message) {
        sendMessage(new Message(chatbot.getMe(), message));
    }

    public void sendImageWithMessage(String image, String message) {
        sendMessage(new Message(chatbot.getMe(), message, image));
    }

    public void sendImageFromURLWithMessage(String url, String message) {
        sendMessage(Message.withImageFromURL(chatbot.getMe(), message, url));
    }

    public void sendImageFromURL(String url) {
        sendMessage(new Message(url, chatbot.getMe()));
    }
    //endregion

    //region Getters
    public Message getLatestMessage() {
        WebElement messageElement = webDriver.findElement(By.xpath(MESSAGES_OTHERS_RECENT));
        //Move mouse over message so messenger marks it as read
        selectInputBox();
        return new Message(messageElement, chatbot);
    }

    public int getNumberOfMessagesDisplayed() {
        return webDriver.findElements(By.xpath(MESSAGES_OTHERS)).size();
    }

    public int getNumberOfMyMessagesDisplayed() {
        return webDriver.findElements(By.xpath(MESSAGES_MINE)).size();
    }

    public String getMyRealName() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SETTINGS_COG)));
        webDriver.findElement(By.xpath(SETTINGS_COG)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SETTINGS_DROPDOWN)));
        webDriver.findElement(By.xpath(SETTINGS_DROPDOWN)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(MY_REAL_NAME)));
        String name = webDriver.findElement(By.xpath(MY_REAL_NAME)).getText();
        webDriver.findElement(By.xpath(SETTINGS_DONE)).click();
        return name;
    }
    //endregion

    //region Waits
    public void waitForMessagesToLoad() {
        messageWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(MESSAGE_CONTAINER)));
    }

    // TODO switch thread if new message in second thread
    public void waitForNewMessage() throws TimeoutException {
        messageWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath(MESSAGES_OTHERS),
                getNumberOfMessagesDisplayed()));
    }
    //endregion
}

