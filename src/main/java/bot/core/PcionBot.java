package bot.core;

import bot.gabes_framework.core.libs.Utils;

public class PcionBot extends Chatbot {
    // to do:
    // !restart
    // !info
    //      wersja pcionbot
    //      uptime
    //      top 3 ladder

    // !stats:
    //     statystyki (unikatowe wiadomosci, etc.)
    //     top 3 ladder

    // !ladder
    // ranking wszystkich na grupce

    // !uptime
    // czas dzialania bota

    // !give <punkty> @uzytkownik przekazuje punkty uzytkownikowi

    // !roulette <punkty>
    // !roulette all

    public static final String imgurSecret = "c7aae8e29fbd8eff21c742648125667abdcd579e";
    public static final String imgurId = "ba98f25f45c4ac9";
    public static final String yandexApiKey = "trnsl.1.1.20181218T072725Z.426c8360e3601118.ce0d07709d8e3173e8005a6e6e266a65090c04fe";
    public static final String newsapiKey = "14a5cf3980a1408e9a4e78b002c2b3cd";

    public static void main(String[] args) {
        System.out.println(args.toString());
        Chatbot pcionbot;
        String username = "ezelbot66@gmail.com";
        String password = "lezetykurwo";

        boolean debugMode = false;      // debugs messages to console
        boolean silentMode = false;    // does not greet itself
        boolean debugMessages = false; // adds bot's full name before it's message, and a ":" after.
        boolean headless = false;
        boolean maximized = true;

        String GRUPKA_ID = "1158615960915822";
        String GRZAGSOFT_ID = "1506449319457834";
        String PATRO_ID = "2275107775897967";

        String message
                = new StringBuilder() // instead of making an instance and assigning build String to message.

                .append("Dostępne komendy:")
                .append("\n")
                .append("==================")
                .append("\n")

                /** {@link  } */
                .append("\n").append(Utils.EMOJI_INFRMATION).append("!sylwester").append("\n")
                .append(Utils.EMOJI_PUSHPIN).append("!suggest ... !pomysl ...").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!popcorn").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON)
                .append(Utils.EMOJI_MAGNIFYING).append("!torrent").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!wiki").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!youtube !yt ").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!google !g ").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!allegro").append("\n")
                .append(Utils.EMOJI_MAGNIFYING).append("!pyszne").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!pyszne mariano").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!pyszne football").append("\n")
                .append(Utils.EMOJI_NEW_BUTTON).append("!pyszne hai").append("\n")
                .append(Utils.EMOJI_INFRMATION).append("!g help ").append("\n")
                .append("\u2601\ufe0f").append("!pogoda !p").append("\n")
                .append(Utils.EMOJI_INFRMATION).append("!info !status").append("\n")
                .append("\uD83D\uDD00").append("!random !r").append("\n")
                .append("     ").append("!karta !kartapulapka !myk").append("\n")
                .append("\u2754").append("!8ball ... !ask ... !8 ... ").append("\n")
                .append(Utils.EMOJI_HOURGLASS).append("!roll !roll <max>").append("\n")
                .append(Utils.EMOJI_EXCLAM_MRK).append("!jebacleze !leze ").append("!spam").append("\n")
                .append("\u2757").append("!g leze").append("\n")
                .append("\uD83E\uDD14").append("!think !think <liczba>").append("\n")


                .toString();

        System.out.println(message);

        pcionbot = new Chatbot(username, password,
                PATRO_ID,

                debugMode,
                silentMode,
                debugMessages,
                headless,
                maximized
        );
    }

    @Override
    public String appendRootPath(String path) {
        return "src/main/resources/" + path;
    }
}
