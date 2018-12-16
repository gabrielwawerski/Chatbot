package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.gabe_modules.modules_base.BareModule;
import bot.utils.gabe_modules.modules_base.ModuleBase;
import bot.utils.gabe_modules.util.SingleMessageModule;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static bot.utils.bot.helper.helper_interface.Util.*;

public class Info extends SingleMessageModule {
    public Info(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
        message = getStats();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : commands) {
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
        return "Online od " + DATE_TIME_FORMATTER.format(startupTime) + "\n[" +
                (diffDays > 0 ? diffDays + " dni" + diffDays + " " : "") +
                (diffHours > 0 ? diffHours + " godzin" + diffHours + " " : "") +
                (diffMinutes > 0 ? diffMinutes + " minut" + diffMinutes + " " : "") +
                diffSeconds + " sekund" + "]";
    }

    public String getMinifiedStats() {
        return "PcionBot " + chatbot.getVersion() + "\n";
    }

    private String getStats() {
        return getMinifiedStats()
                + getUptime()
                + "\n\nUnikatowe wiadomości bieżącej sesji: " + chatbot.getMessageLog().size()
                + "\nWiadomości leze: " + lezeStats()
                + "\n\n" + cmdInfo();
    }

    private int lezeStats() {
        int counter = 0;
        if (Objects.isNull(chatbot.getMessageLog())) {
            return 0;
        }
        ArrayList<Message> messages = chatbot.getMessageLog();
        for (Message msg : messages) {
            if (msg.getSender().getName().equals("Jakub Smolak")) {
                counter++;
            }
        }
        return counter;
    }

    private String cmdInfo() {
        return "Wpisz !cmd aby zobaczyć listę komend";
    }
}