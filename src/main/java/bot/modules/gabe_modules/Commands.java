package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.gabes_framework.core.libs.Utils;
import bot.modules.gabe_modules.query.*;
import bot.gabes_framework.message.SingleMessageModule;

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
                .append("    ")
                .append("!info !status")
                .append("\n")

                .append(Utils.EMOJI_NEW_BUTTON).append("!suggest !pomysl")
                .append("\n")

                /** {@link MultiTorrentSearch} */
                .append(Utils.EMOJI_NEW_BUTTON).append("!torrent")
                .append("\n")

                /** {@link WikipediaSearch} */
                .append(Utils.EMOJI_NEW_BUTTON).append("!wiki")
                .append("\n")

                /** {@link YoutubeSearch} */
                .append("    ")
                .append("!youtube !yt")
                .append("\n")

                /** {@link GoogleSearch} */
                .append("    ")
                .append("!google !g")
                .append("\n")
                .append("!g help ").append("!g leze")
                .append("\n")

                /** {@link PyszneSearch} */
                .append("!pyszne ")
                .append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!pyszne mariano")
                .append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!pyszne football")
                .append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!pyszne hai")
                .append("\n")

                /** {@link AllegroSearch} */
                .append("!allegro...")
                .append("\n")

                /** {@link RandomGroupPhoto */
                .append("!random !r")
                .append("\n")

                /** {@link SimpleWeather} */
                .append("!pogoda !p")
                .append("\n")

                .append(Utils.EMOJI_NEW_BUTTON).append("!popcorn")
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
                .append(Utils.EMOJI_NEW_BUTTON).append("!sylwester")
                .append("\n")

                .toString();
    }
}