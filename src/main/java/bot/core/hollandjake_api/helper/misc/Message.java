package bot.core.hollandjake_api.helper.misc;

import bot.core.Chatbot;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static bot.core.hollandjake_api.helper.interfaces.Util.*;
import static bot.core.hollandjake_api.helper.interfaces.XPATHS.*;
import static org.apache.commons.lang.StringEscapeUtils.unescapeHtml;

public class Message {
    private final Human sender;
    private final String message;
    private final Image image;
    private final Date date = new Date();
    private final WebElement webElement;

    private boolean containsCommand = false;

    public Message(WebElement webElement, Chatbot chatbot) {
        List<WebElement> imageElements = webElement.findElements(By.xpath(MESSAGE_IMAGE));
        if (imageElements.size() > 0) {
            String href = imageElements.get(0).getAttribute("src");
            this.image = imageFromUrl(href);
            this.sender = null;
            this.message = "";
            this.webElement = webElement;
        } else {
            this.image = null;
            this.sender = Human.getFromElement(webElement, chatbot.getPeople());

            List<WebElement> messageBodies = webElement.findElements(By.xpath(MESSAGE_TEXT));
            if (messageBodies.size() > 0) {
                this.message = messageBodies.get(0).getAttribute("aria-label");
            } else {
                this.message = "";
            }
            this.webElement = webElement;
        }
        this.containsCommand = chatbot.containsCommand(this);
    }

    public WebElement getReactionButton() {
        return webElement.findElement(By.xpath(REACTION_MENU_BUTTON));
    }

    public boolean reacted() {
        return webElement.findElement(By.xpath(REACTION_PRESENT)) != null;
    }

    public WebElement getWebElement() {
        return webElement;
    }

    public Message(String url, Human me) {
        this.sender = me;
        this.image = imageFromUrl(url);
        message = null;
        webElement = null;
    }

    public Message(Human me, String message) {
        this.sender = me; //Sender is the bot
        this.message = unescapeHtml(message);
        this.image = null;
        webElement = null;
    }

    public Message(Human me, String message, String image) {
        this.sender = me; //Sender is the bot
        this.message = unescapeHtml(message);
        this.image = imageFromUrl(image);
        webElement = null;
    }

    public Message(Human me, String message, Image image) {
        this.sender = me; //Sender is the bot
        this.message = unescapeHtml(message);
        this.image = image;
        webElement = null;
    }

    public static Message withImageFromURL(Human me, String message, String url) {
        Image image = imageFromUrl(url);
        if (image != null) {
            return new Message(me, message, image);
        } else {
            return null;
        }
    }

    // TODO FUCKING MOST IMPORTANT METHOD HERE
    private static BufferedImage imageFromUrl(String url) {
        ImageInputStream imageInputStream = null;

        try {
            URL U = new URL(url);
            URLConnection urlConnection = U.openConnection();
            urlConnection.connect();
            imageInputStream = ImageIO.createImageInputStream(urlConnection.getInputStream());
            BufferedImage image = ImageIO.read(imageInputStream);

            if (image == null) {
                return null;
            }
            double size = urlConnection.getContentLength();

            //Scale image to fit in size
            double scaleFactor = Math.min(1, 200000 / size);

            if ((image.getWidth() <= 0 || image.getWidth() > 9999)
                    && (image.getHeight() <= 0 || image.getHeight() > 9999)) {
                return null;
            }

            int scaledWidth = (int) (image.getWidth() * scaleFactor);
            int scaledHeight = (int) (image.getHeight() * scaleFactor);
            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

            BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();
            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //region Send message
    private void sendMessage(WebElement inputBox, String message) {
        CLIPBOARD.setContents(new StringSelection(unescapeHtml(message)), null);
        inputBox.sendKeys(PASTE + Keys.ENTER);
    }

    private void sendMessageWithImage(WebElement inputBox, String message, Image image) {
        if (image != null) {
            CLIPBOARD.setContents(new ImageTransferable(image), null);
            inputBox.sendKeys(PASTE);
        }
        if (!message.isEmpty()) {
            CLIPBOARD.setContents(new StringSelection(unescapeHtml(message)), null);
            inputBox.sendKeys(PASTE);
        }
        inputBox.sendKeys(Keys.ENTER);
    }

    public void sendMessage(WebElement inputBox) {
        if (image != null) {
            sendMessageWithImage(inputBox, message, image);
        } else {
            sendMessage(inputBox, message);
        }
    }

    public void sendDebugMessage(WebElement inputBox) {
        sendMessage(inputBox, toString());
    }
    //endregion

    public boolean doesContainsCommand() {
        return containsCommand;
    }

    //region Getters and Setters
    public Human getSender() {
        return sender;
    }

    /**
     * Returns actual message content.
     */
    public String getMessage() {
        return message;
    }

    public Image getImage() {
        return image;
    }
    //endregion

    public JSONObject toJSON() {
        JSONObject me = new JSONObject();
        me.put("sender", sender.toJSON());
        me.put("message", message);
        me.put("timestamp", DATE_FORMATTER.format(date));
        if (image != null) {
            me.put("image", image);
        }

        return me;
    }

    public String toString() {
        return (sender != null ? sender + " : " : "") +
                (message != null ? message + " : " : "") +
                (image != null ? image : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message otherMessage = (Message) o;
        return Objects.equals(sender, otherMessage.sender) &&
                Objects.equals(message, otherMessage.message) &&
                Objects.equals(image, otherMessage.image) &&
                Objects.equals(date, otherMessage.date);
    }
}
