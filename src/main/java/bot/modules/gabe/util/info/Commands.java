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

//                .append("\u2935")
                .append(NEW_BUTTON_EMOJI).append("!mp3 !mp3 <yt link>").append("\n")

//                .append("\uD83D\uDD22")
                .append(NEW_BUTTON_EMOJI).append("!ladder !ladder msg").append("\n")

//                .append(INFO_EMOJI)
                .append(NEW_BUTTON_EMOJI).append("!stats !stats uzytkownik").append("\n")

                .append(NEW_BUTTON_EMOJI).append("!wykop !wy").append("\n")

//                .append("\u2797")
                .append(NEW_BUTTON_EMOJI).append("!roulette <liczba> !roulette all").append("\n")

                .append(NEW_BUTTON_EMOJI).append("!wtf losowy obrazek z kat. wtf").append("\n")
//                .append(B_EMOJI)
                .append(NEW_BUTTON_EMOJI).append("!b <tekst>").append("\n")

//                .append(INFO_EMOJI).append("!sylwester").append("\n")
                .append(MAGNIFYING_EMOJI).append("!torrent").append("\n")
                .append(MAGNIFYING_EMOJI).append("!wiki").append("\n")
                .append(MAGNIFYING_EMOJI).append("!youtube !yt ").append("\n")
                .append(MAGNIFYING_EMOJI).append("!google   !g ").append("\n")
                .append(MAGNIFYING_EMOJI).append("!allegro").append("\n")
                .append(MAGNIFYING_EMOJI).append("!pyszne").append("\n")
                .append("\uD83C\uDF10").append("!pyszne mariano").append("\n")
                .append("\uD83C\uDF10").append("!pyszne football").append("\n")
                .append("\uD83C\uDF10").append("!pyszne haianh").append("\n")

                .append("\uD83D\uDE03").append("!emotes").append("\n")
                .append("\uD83D\uDD00").append("!random !r").append("\n")
                .append("\uD83D\uDE02").append("!kwejk !kw").append("\n")
                .append("\u2601\ufe0f").append("!pogoda !p").append("\n")
                .append("\u2754").append("!8ball !ask !8 ").append("\n")
                .append(HOURGLASS_EMOJI).append("!roll !roll <max>").append("\n")
                .append(THINK_EMOJI).append("!think !think <liczba>").append("\n")
                .append(CAMERA_EMOJI).append("!karta !kartapulapka !myk").append("\n")
                .append(POPCORN_EMOJI).append("!popcorn").append("\n")
                .append(EXCL_MARK_RED_EMOJI).append("!g leze").append("\n")
                .append(EXCL_MARK_RED_EMOJI).append("!jebacleze !leze !spam").append("\n")

                .append(NEW_BUTTON_EMOJI).append("!atg").append("\n")
                .append(INFO_EMOJI).append("!g help ").append("\n")
                .append(INFO_EMOJI).append("!info").append("\n")
                .append(INFO_EMOJI).append("!staty").append("\n")
                .append(INFO_EMOJI).append("!uptime").append("\n")
                .append(PENCIL_EMOJI).append("!suggest  !pomysl").append("\n")

                .toString();
        }
}