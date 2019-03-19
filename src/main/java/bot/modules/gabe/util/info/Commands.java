package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

import static bot.core.gabes_framework.core.util.Emoji.INFO;

public class Commands extends ModuleBase {
    private static final String REGEX_CMD = Utils.TO_REGEX("cmd");
    private static final String REGEX_CMD_2 = Utils.TO_REGEX("cmd2");

    public Commands(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(REGEX_CMD)) {
            // TODO zrobic obrazek w photoshopie i go wysylac?
            StringBuilder sb = new StringBuilder(); // instead of making an instance and assigning build String to message.
            sb
                    .append("!<komenda> - info\n")
                    .append("================")
                    .append("\n")

//                    .append(Emoji.EXCL_MARK_RED)
                    .append(Emoji.NEW_BUTTON)
                    .append("!insult osoba\n     !insult me").append("\n")

                    .append(Emoji.NEW_BUTTON)
                    .append("!giveaway pkt\n     !claim").append("\n")

                    .append(Emoji.SLOT_MACHINE)
                    .append("!pull pkt\n     !pull all").append("\n")

                    .append(Emoji.ABC)
                    .append("!img tekst").append("\n")

                    .append(Emoji.RIGHT_ARROW)
                    .append("!give osoba pkt").append("\n")

                    .append(Emoji.DUEL)
                    .append("!duel osoba pkt\n     !duel pkt").append("\n")

                    .append(Emoji.STATS)
                    .append("!ladder").append("\n")

                    .append(INFO)
                    .append("!me\n\u2139\ufe0f!stats/points/pts\n     !(stats/points/pts) osoba").append("\n")

                    .append("\n")
                    .append(INFO)
                    .append("Więcej komend: !cmd2");

            String msg = sb.toString();
            chatbot.sendMessage(msg);
            return true;
        } else if (is(REGEX_CMD_2)) {
            StringBuilder sb = new StringBuilder(); // instead of making an instance and assigning build String to message.
            sb.append("Dostępne komendy:")
                    .append("\n")
                    .append("=============")
                    .append("\n")
                    .append(INFO).append("!atg").append("\n")
                    .append(INFO).append("!info").append("\n")
                    .append(INFO).append("!staty").append("\n")
                    .append(INFO).append("!uptime").append("\n")
                    .append(Emoji.PENCIL).append("!suggest  !pomysl").append("\n")

                    .append(Emoji.DOWNLOAD)
                    .append("!mp3\n     !mp3 yt link").append("\n")
                    .append(Emoji.DIVISION)
                    .append("!roulette <liczba> !roulette all !vabank").append("\n")
                    .append(Emoji.LIT_EMOJI)
                    .append("!wykop !wy").append("\n")
                    .append(Emoji.LIT_EMOJI)
                    .append("!wtf losowy obrazek z kat. wtf").append("\n")
                    .append(Emoji.B).append("!b <tekst>").append("\n")
                    .append(Emoji.MAGNIFYING).append("!torrent").append("\n")
                    .append(Emoji.MAGNIFYING).append("!wiki").append("\n")
                    .append(Emoji.MAGNIFYING).append("!youtube !yt ").append("\n")
                    .append(Emoji.MAGNIFYING).append("!google   !g ").append("\n")
                    .append(Emoji.MAGNIFYING).append("!allegro").append("\n")
                    .append(Emoji.MAGNIFYING).append("!pyszne").append("\n")
                    .append("\uD83C\uDF10").append("!pyszne mariano").append("\n")
                    .append("\uD83C\uDF10").append("!pyszne football").append("\n")
                    .append("\uD83C\uDF10").append("!pyszne haianh").append("\n")

                    .append("\uD83D\uDE03").append("!emotes").append("\n")
                    .append("\uD83D\uDD00").append("!random !r").append("\n")
                    .append(Emoji.LIT_EMOJI).append("!kwejk !kw").append("\n")
                    .append("\u2601\ufe0f").append("!pogoda !p").append("\n")
                    .append("\u2754").append("!8ball !ask !8 ").append("\n")
                    .append(Emoji.HOURGLASS).append("!roll !roll <max>").append("\n")
                    .append(Emoji.THINK).append("!think !think <liczba>").append("\n")
                    .append(Emoji.CAMERA).append("!karta !kartapulapka !myk").append("\n")
                    .append(Emoji.POPCORN).append("!popcorn").append("\n")
                    .append(Emoji.EXCL_MARK_RED).append("!g leze").append("\n")
                    .append(Emoji.EXCL_MARK_RED).append("!jebacleze !leze !spam").append("\n")
                    .append(INFO).append("!g help ").append("\n");

            String msg = sb.toString();
            chatbot.sendMessage(msg);
            return true;
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(REGEX_CMD, REGEX_CMD_2);
    }
}