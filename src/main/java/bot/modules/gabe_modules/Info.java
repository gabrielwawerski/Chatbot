package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.message.SingleMessageModule;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static bot.core.helper.interfaces.Util.*;

public class Info extends SingleMessageModule {
    public Info(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
        message = getStats();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : regexList) {
            if (match.equals(command)) {
                this.message = getStats();
                chatbot.sendMessage(this.message);
                return true;
            }
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
        return "PcionBot " + chatbot.getVersion() + "\n";
    }

    private String getStats() {
        return getMinifiedStats()
                + "\nZaładowane moduły: " + chatbot.getModulesOnline()
                + getUptime()
                + "\n\nUnikatowe wiadomości bieżącej sesji: " + chatbot.getMessageLog().size()
                + "\nWiadomości leze: " + lezeStats()
                + "\n\n" + cmdInfo();
    }

    private String lezeStats() {
        double lezeMessageCounter = 0;
        ArrayList<Message> messages = chatbot.getMessageLog();

        if (Objects.isNull(chatbot.getMessageLog())) {
            return "błąd";
        }

        for (Message msg : messages) {
            if (msg.getSender().getName().equals("Jakub Smolak")) {
                lezeMessageCounter++;
            }
        }

        if (lezeMessageCounter > 0) {
            double lezeMsgPercent = (messages.size() - (lezeMessageCounter * messages.size())) / 100;
            NumberFormat format = new DecimalFormat("#00.0");

            return format.format(lezeMsgPercent) + "%";
        } else {
            return "0%";
        }
    }

    private String cmdInfo() {
        return "Wpisz !cmd aby zobaczyć listę komend";
    }
}