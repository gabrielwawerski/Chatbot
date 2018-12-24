package bot.modules.gabe_modules.util.info;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static bot.core.helper.interfaces.Util.*;

public class Info extends ModuleBase {
    private final String ECHO_COMMAND = "echo";
    private final String INFO_REGEX = Utils.TO_REGEX("info");
    private final String STATS_REGEX = Utils.TO_REGEX("stats");
    private final String UPTIME_REGEX = Utils.TO_REGEX("uptime");

    public Info(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(INFO_REGEX)) {
            chatbot.sendMessage(getInfo());
            return true;
        } else if (is(STATS_REGEX)) {
            chatbot.sendMessage(getStats());
            return true;
        } else if (is(UPTIME_REGEX)) {
            chatbot.sendMessage(getUptime());
            return true;
        } else if (is(ECHO_COMMAND)) {
            chatbot.sendMessage(getMinifiedStats());
            return true;
        }
        return false;
    }

    private String getUptime() {
        LocalDateTime startupTime = chatbot.getStartupTime();
        LocalDateTime now = LocalDateTime.now();
        long diff = now.toEpochSecond(ZoneOffset.UTC) - startupTime.toEpochSecond(ZoneOffset.UTC);

        long diffSeconds = TimeUnit.SECONDS.convert(diff, TimeUnit.SECONDS) % 60;
        long diffMinutes = TimeUnit.MINUTES.convert(diff, TimeUnit.SECONDS) % 60;
        long diffHours = TimeUnit.HOURS.convert(diff, TimeUnit.SECONDS) % 24;
        long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);

        return "Online od " + DATE_TIME_FORMATTER.format(startupTime)
                + "\n["
                + (diffDays > 0 ? diffDays + " dni" + " " : "")
                + (diffHours > 0 ? diffHours + " godzin" + " " : "")
                + (diffMinutes > 0 ? diffMinutes + " minut" + " " : "")
                + diffSeconds + " sekund" + "]";
    }

    public String getMinifiedStats() {
        return "PcionBot " + chatbot.getVersion();
    }

    private String getInfo() {
        return getMinifiedStats()
                + "\n"
                + getUptime()
                + "\n\n"

                + getStats()
                + "\n\n"

                + cmdInfo();
    }

    private String getStats() {
        return "Załadowane moduły: " + chatbot.getModulesOnline()
                + "\n"
                + "Unikatowe wiadomości bieżącej sesji: " + chatbot.getMessageLog().size()
                + "\n"
                + "Wiadomości leze: " + lezeStats();
    }

    private String cmdInfo() {
        return "Wpisz !cmd aby zobaczyć listę komend.";
    }

    private String lezeStats() {
        double lezeMsgCount = 0;
        ArrayList<Message> messages = chatbot.getMessageLog();

        if (Objects.isNull(chatbot.getMessageLog())) {
            return "";
        } else {
            for (Message msg : messages) {
                if (msg.getSender().getName().equals("Jakub Smolak")) { // fixme
                    lezeMsgCount++;
                }
            }

            if (lezeMsgCount > 0) {
                double lezeMsgPercent = (messages.size() - (lezeMsgCount * messages.size())) / 100;
                NumberFormat format = new DecimalFormat("#00.0");

                return lezeMsgCount + "(" + format.format(lezeMsgPercent) + "%)";
            } else {
                return "0";
            }
        }
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, INFO_REGEX, STATS_REGEX, UPTIME_REGEX, ECHO_COMMAND);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(INFO_REGEX, STATS_REGEX, UPTIME_REGEX, ECHO_COMMAND);
    }
}