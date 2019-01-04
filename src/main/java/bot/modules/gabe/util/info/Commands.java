package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.message.SingleMessageModule;

import java.util.List;

import static bot.core.gabes_framework.core.Utils.*;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);

        // TODO zrobic obrazek w photoshopie i go wysylac?
        message = new StringBuilder() // instead of making an instance and assigning build String to message.
                .append("Dostępne komendy:")
                .append("\n")
                .append("=============")
                .append("\n")

                .append(EMOJI_NEW_BUTTON).append("!duel @uzytkownik pkt").append("\n")

//                .append("\u2935")
                .append(EMOJI_NEW_BUTTON).append("!mp3 !mp3 <yt link>").append("\n")

//                .append("\uD83D\uDD22")
                .append(EMOJI_NEW_BUTTON).append("!ladder !msg").append("\n")

//                .append(EMOJI_INFO)
                .append(EMOJI_NEW_BUTTON).append("!stats !stats uzytkownik").append("\n")

                .append(EMOJI_NEW_BUTTON).append("!wykop !wy").append("\n")

//                .append("\u2797")
                .append(EMOJI_NEW_BUTTON).append("!roulette <liczba> !roulette all").append("\n")

                .append(EMOJI_NEW_BUTTON).append("!wtf losowy obrazek z kat. wtf").append("\n")
//                .append(EMOJI_B)
                .append(EMOJI_NEW_BUTTON).append("!b <tekst>").append("\n")

//                .append(EMOJI_INFO).append("!sylwester").append("\n")
                .append(EMOJI_MAGNIFYING).append("!torrent").append("\n")
                .append(EMOJI_MAGNIFYING).append("!wiki").append("\n")
                .append(EMOJI_MAGNIFYING).append("!youtube !yt ").append("\n")
                .append(EMOJI_MAGNIFYING).append("!google   !g ").append("\n")
                .append(EMOJI_MAGNIFYING).append("!allegro").append("\n")
                .append(EMOJI_MAGNIFYING).append("!pyszne").append("\n")
                .append("\uD83C\uDF10").append("!pyszne mariano").append("\n")
                .append("\uD83C\uDF10").append("!pyszne football").append("\n")
                .append("\uD83C\uDF10").append("!pyszne haianh").append("\n")

                .append("\uD83D\uDE03").append("!emotes").append("\n")
                .append("\uD83D\uDD00").append("!random !r").append("\n")
                .append("\uD83D\uDE02").append("!kwejk !kw").append("\n")
                .append("\u2601\ufe0f").append("!pogoda !p").append("\n")
                .append("\u2754").append("!8ball !ask !8 ").append("\n")
                .append(EMOJI_HOURGLASS).append("!roll !roll <max>").append("\n")
                .append(EMOJI_THINK).append("!think !think <liczba>").append("\n")
                .append(EMOJI_CAMERA).append("!karta !kartapulapka !myk").append("\n")
                .append(EMOJI_POPCORN).append("!popcorn").append("\n")
                .append(EMOJI_EXCL_MARK_RED).append("!g leze").append("\n")
                .append(EMOJI_EXCL_MARK_RED).append("!jebacleze !leze !spam").append("\n")

                .append(EMOJI_NEW_BUTTON).append("!atg").append("\n")
                .append(EMOJI_INFO).append("!g help ").append("\n")
                .append(EMOJI_INFO).append("!info").append("\n")
                .append(EMOJI_INFO).append("!staty").append("\n")
                .append(EMOJI_INFO).append("!uptime").append("\n")
                .append(EMOJI_PENCIL).append("!suggest  !pomysl").append("\n")

                .toString();
        }
}