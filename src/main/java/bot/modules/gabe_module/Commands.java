package bot.modules.gabe_module;

import bot.core.Chatbot;
import bot.modules.gabe_module.searcher.GoogleSearch;
import bot.utils.gabe_modules.util.module_library.SingleMessageModule;

import java.util.List;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        message
                = new StringBuilder() // instead of making an instance and assigning build String to message.
                .append("```\n")

                .append("Dostępne komendy:")
                .append("\n==================\n")

                /** {@link SimpleWeather} */
                .append("pogoda")
                .append("\n")
                .append("p")
                .append("\n")
                .append("w")
                .append("\n")

                /** {@link GoogleSearch} */
                .append("google <tekst>")
                .append("\n")
                .append("g <tekst>")
                .append("\n")
                .append("g help")
                .append("\n")

                /** {@link EightBall} */
                .append("8ball <pytanie>")
                .append("ask")
                .append("8")
                .append("\n")
                .append("roll      | roll <liczba>")
                .append("\n")
                .append("inspire")
                .append("\n")
                .append("jebacleze | leze")
                .append("\n")
                .append("spam")
                .append("\n")
                .append("think     | think <liczba>")
                .append("\n")
                .append("kartapulapka ")
                .append("\n")
                .append("karta     | pulapka")
                .append("\n")
                .append("myk")

                .append("\n```").toString();
    }
}