package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.utils.gabe_modules.util.SingleMessageModule;

import java.util.List;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        StringBuilder builder = new StringBuilder();
        builder.append("```\n")
                .append("Dostępne komendy:")
                .append("\n==================\n")
                .append("commands\ncmd\nhelp").append("\n")
                .append("(p)ogoda  | (w)eather").append("\n")
                .append("(g)oogle  | (s)earch").append("\n")
                .append("(8)ball   | (a)sk").append("\n")
                .append("roll      | roll <liczba>").append("\n")
                .append("inspire").append("\n")
                .append("jebacleze | leze").append("\n")
                .append("spam").append("\n")
                .append("think     | think <liczba>").append("\n")
                .append("kartapulapka ").append("\n")
                .append("karta     | pulapka").append("\n")
                .append("myk")
                .append("\n```");
        message = builder.toString();
    }
}