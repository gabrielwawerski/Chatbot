package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.modules.gabe_modules.searcher.GoogleSearch;
import bot.impl.gabes_framework.message.SingleMessageModule;

import java.util.List;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        message
                = new StringBuilder() // instead of making an instance and assigning build String to message.
                .append("```") // tells messenger to start formatting text

                                        .append("\n")
                .append("Dostępne komendy:")
                                        .append("\n")
                .append("==================")
                                        .append("\n")
                                        .append("\n")

                .append("info")
                                        .append("\n")
                .append("status")
                                        .append("\n")
                                        .append("\n")

                /** {@link SimpleWeather} */
                .append("Aktualna pogoda")
                                        .append("\n")
                .append("pogoda")
                                        .append("\n")
                .append("p")
                                        .append("\n")
                .append("w")
                                        .append("\n")
                                        .append("\n")

                /** {@link GoogleSearch} */
                .append("Po komendzie wpisz szukaną frazę")
                                        .append("\n")
                .append("google <tekst>")
                                        .append("\n")
                .append("g <tekst>")
                                        .append("\n")
                .append("g help")
                                        .append("\n")
                .append("g leze")
                                        .append("\n")
                                        .append("\n")

                .append("Losowe zdjęcie grupki")
                                        .append("\n")
                .append("random")
                                        .append("\n")
                .append("r")
                                        .append("\n")
                                        .append("\n")

                /** {@link EightBall} */
                .append("Zadaj pytanie po komendzie")
                                        .append("\n")
                .append("8ball")
                                        .append("\n")
                .append("ask")
                                        .append("\n")
                .append("8")
                                        .append("\n")
                                        .append("\n")

                /** {@link EightBall} */
                .append("Losowa liczba z zakresu (1-100)")
                                        .append("\n")
                .append("roll")
                                        .append("\n")
                .append("roll <max>")
                                        .append("\n")
                                        .append("\n")

                /** {@link JebacLeze} */
                .append("jebacleze")
                                        .append("\n")
                .append("leze")
                                        .append("\n")

                /** {@link LezeSpam} */
                .append("spam")
                                        .append("\n")
                                        .append("\n")

                /** {@link Think */
                .append("think")
                                        .append("\n")
                .append("think <liczba>")
                                        .append("\n")
                                        .append("\n")

                /** {@link KartaPulapka} */
                .append("kartapulapka ")
                                        .append("\n")
                .append("karta")
                                        .append("\n")
                .append("myk")
                                        .append("\n")
                                        .append("\n")

                .append("```") // shows messenger where formatting ends

                .toString();
    }
}