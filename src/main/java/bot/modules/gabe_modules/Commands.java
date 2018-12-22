package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.modules.gabe_modules.query.AllegroSearch;
import bot.modules.gabe_modules.query.GoogleSearch;
import bot.gabes_framework.message.SingleMessageModule;
import bot.modules.gabe_modules.query.PyszneSearch;
import bot.modules.gabe_modules.query.YoutubeSearch;

import java.util.List;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        message
                = new StringBuilder() // instead of making an instance and assigning build String to message.

                .append("Dostępne komendy:")
                .append("\n")
                .append("==================")
                .append("\n")

                /** {@link  } */
                .append("!info !status")
                .append("\n")

                .append("!suggest !pomysl")
                .append("\n")



                /** {@link YoutubeSearch} */
                .append("!youtube... !yt...")
                .append("\n")

                /** {@link PyszneSearch} */
                .append("!pyszne")
                .append("\n")

                /** {@link AllegroSearch} */
                .append("!allegro...")
                .append("\n")

                /** {@link GoogleSearch} */
                .append("!google !g ").append("!g help ").append("!g leze")
                .append("\n")

                /** {@link RandomGroupPhoto */
                .append("!random !r")
                .append("\n")

                /** {@link SimpleWeather} */
                .append("!pogoda !p")
                .append("\n")

                .append("!popcorn")
                .append("\n")

                /** {@link KartaPulapka} */
                .append("!karta !kartapulapka !myk")
                .append("\n")

                /** {@link EightBall} */
                .append("!8ball... !ask... !8... ")
                .append("\n")

                /** {@link Roll} */
                .append("!roll !roll <max>")
                .append("\n")

                /** {@link JebacLeze} */
                .append("!jebacleze !leze ").append("!spam")
                .append("\n")

                /** {@link Think */
                .append("!think !think <liczba>")
                .append("\n")


                .append("\n")
                .append("!sylwester")
                .append("\n")

                .toString();
    }
}