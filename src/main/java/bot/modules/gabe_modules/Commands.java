package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.message.SingleMessageModule;

import java.util.List;

import static bot.gabes_framework.core.libs.Utils.*;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        // instead of making an instance and assigning build String to message.
        /** {@link  } */
        switch (message
                = new StringBuilder() // instead of making an instance and assigning build String to message.

                .append("Dostępne komendy:")
                .append("\n")
                .append("==================")
                .append("\n")

                .append("\uD83D\uDE02")
                .append(EMOJI_NEW_BUTTON).append("!kwejk !kw").append("\n")

                .append(EMOJI_INFRMATION)
                .append(EMOJI_NEW_BUTTON).append("!sylwester").append("\n")

                .append(EMOJI_PENCIL)
                .append(EMOJI_NEW_BUTTON).append("!suggest !pomysl").append("\n")

                .append(EMOJI_RIGHT_ARRW)
                .append(EMOJI_NEW_BUTTON).append("!pyszne mariano").append("\n")

                .append(EMOJI_RIGHT_ARRW)
                .append(EMOJI_NEW_BUTTON).append("!pyszne football").append("\n")

                .append(EMOJI_RIGHT_ARRW)
                .append(EMOJI_NEW_BUTTON).append("!pyszne haianh").append("\n")

                .append(EMOJI_MAGNIFYING)
                .append(EMOJI_NEW_BUTTON).append("!torrent").append("\n")

                .append(EMOJI_MAGNIFYING).append("!wiki").append("\n")
                .append(EMOJI_MAGNIFYING).append("!youtube !yt ").append("\n")
                .append(EMOJI_MAGNIFYING).append("!google   !g ").append("\n")
                .append(EMOJI_MAGNIFYING).append("!pyszne").append("\n")
                .append(EMOJI_MAGNIFYING).append("!allegro").append("\n")
                .append(EMOJI_INFRMATION).append("!info").append("\n")
                .append(EMOJI_INFRMATION).append("!g help ").append("\n")
                .append("\uD83D\uDD00").append("!random !r").append("\n")
                .append("\u2601\ufe0f").append("!pogoda !p").append("\n")
                .append("\u2754").append("!8ball !ask !8 ").append("\n")

                .append(EMOJI_HOURGLASS).append("!roll !roll <max>").append("\n")
                .append(EMOJI_THINK).append("!think !think <liczba>").append("\n")
                .append(EMOJI_CAMERA).append("!karta !kartapulapka !myk").append("\n")
                .append(EMOJI_POPCORN)
                .append(EMOJI_NEW_BUTTON).append("!popcorn").append("\n")
                .append(EMOJI_EXCLAM_MRK).append("!g leze").append("\n")
                .append(EMOJI_EXCLAM_MRK).append("!jebacleze !leze ").append("!spam").append("\n")

                .toString()) {
        }
    }
}