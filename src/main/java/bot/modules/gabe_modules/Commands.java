package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.gabes_framework.core.libs.Utils;
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
                .append("\n").append(Utils.EMOJI_INFRMATION).append("!sylwester").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!suggest ... !pomysl ...").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!torrent").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!wiki").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON)
                .append(Utils.EMOJI_MAGNIFYING).append("!pyszne mariano").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!pyszne football").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!pyszne hai").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!youtube !yt ").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!google !g ").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!pyszne").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!allegro").append("\n")
                .append(Utils.EMOJI_INFRMATION).append("!info !status").append("\n")
                .append(Utils.EMOJI_INFRMATION).append("!g help ").append("\n")
                .append("\uD83D\uDD00").append("!random !r").append("\n")
                .append("\u2601\ufe0f").append("!pogoda !p").append("\n")
                .append(Utils.EMOJI_POPCORN).append("!popcorn").append("\n")
                .append("\u2754").append("!8ball !ask !8 ").append("\n")
                .append(Utils.EMOJI_HOURGLASS).append("!roll !roll <max>").append("\n")
                .append("\uD83E\uDD14").append("!think !think <liczba>").append("\n")
                .append("     ").append("!karta !kartapulapka !myk").append("\n")
                .append(Utils.EMOJI_EXCLAM_MRK).append("!jebacleze !leze ").append("!spam").append("\n")
                .append("\u2757").append("!g leze").append("\n")


                .toString();
    }
}