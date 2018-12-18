package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.modules.gabe_modules.searcher.GoogleSearch;
import bot.gabes_framework.message.SingleMessageModule;

import java.util.List;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        message
                = new StringBuilder() // instead of making an instance and assigning build String to message.

                                        .append("\n")
                .append("Dostępne komendy:")
                                        .append("\n")
                .append("==================")
                                        .append("\n")
                .append("info, status")
                                        .append("\n")

                /** {@link SimpleWeather} */
                .append("pogoda, p, w")
                                        .append("\n")

                /** {@link GoogleSearch} */
                .append("google, g")
                                        .append("\n")
                .append("g help")
                                        .append("\n")
                .append("g leze")
                                        .append("\n")

                /** {@link RandomGroupPhoto */
                .append("random,  r")
                                        .append("\n")

                /** {@link EightBall} */
                .append("8ball, ask, 8")
                                        .append("\n")

                /** {@link EightBall} */
                .append("roll, roll <max>")
                                        .append("\n")

                /** {@link JebacLeze} */
                .append("jebacleze, leze")

                /** {@link LezeSpam} */
                .append(", spam")
                                        .append("\n")

                /** {@link Think */
                .append("think, think <liczba>")
                                        .append("\n")

                /** {@link KartaPulapka} */
                .append("kartapulapka, karta, myk")
                                        .append("\n")

                .toString();
    }
}