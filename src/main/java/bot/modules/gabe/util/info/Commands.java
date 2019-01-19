package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class Commands extends ModuleBase {
    public Commands(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isRegex()) {
            // TODO zrobic obrazek w photoshopie i go wysylac?
            StringBuilder sb = new StringBuilder(); // instead of making an instance and assigning build String to message.
                    sb.append("Dostępne komendy:")
                    .append("\n")
                    .append("=============")
                    .append("\n")

                    .append(Emoji.NEW_BUTTON).append("!duel @uzytkownik pkt").append("\n")

//                .append("\u2935")
                    .append(Emoji.NEW_BUTTON).append("!mp3 !mp3 <yt link>").append("\n")

//                .append("\uD83D\uDD22")
                    .append(Emoji.NEW_BUTTON).append("!ladder !msg").append("\n")

//                .append(INFO)
                    .append(Emoji.NEW_BUTTON).append("!stats !stats uzytkownik").append("\n")

                    .append(Emoji.NEW_BUTTON).append("!wykop !wy").append("\n")

//                .append("\u2797")
                    .append(Emoji.NEW_BUTTON).append("!roulette <liczba> !roulette all").append("\n")

                    .append(Emoji.NEW_BUTTON).append("!wtf losowy obrazek z kat. wtf").append("\n")
//                .append(B)
                    .append(Emoji.NEW_BUTTON).append("!b <tekst>").append("\n")

//                .append(INFO).append("!sylwester").append("\n")
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
                    .append("\uD83D\uDE02").append("!kwejk !kw").append("\n")
                    .append("\u2601\ufe0f").append("!pogoda !p").append("\n")
                    .append("\u2754").append("!8ball !ask !8 ").append("\n")
                    .append(Emoji.EMOJI_HOURGLASS).append("!roll !roll <max>").append("\n")
                    .append(Emoji.THINK).append("!think !think <liczba>").append("\n")
                    .append(Emoji.CAMERA).append("!karta !kartapulapka !myk").append("\n")
                    .append(Emoji.POPCORN).append("!popcorn").append("\n")
                    .append(Emoji.EMOJI_EXCL_MARK_RED).append("!g leze").append("\n")
                    .append(Emoji.EMOJI_EXCL_MARK_RED).append("!jebacleze !leze !spam").append("\n")

                    .append(Emoji.NEW_BUTTON).append("!atg").append("\n")
                    .append(Emoji.INFO).append("!g help ").append("\n")
                    .append(Emoji.INFO).append("!info").append("\n")
                    .append(Emoji.INFO).append("!staty").append("\n")
                    .append(Emoji.INFO).append("!uptime").append("\n")
                    .append(Emoji.PENCIL).append("!suggest  !pomysl").append("\n");

                    String msg = sb.toString();

                    chatbot.sendMessage(msg);
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return getMappedRegexes("cmd", "help");
    }
}