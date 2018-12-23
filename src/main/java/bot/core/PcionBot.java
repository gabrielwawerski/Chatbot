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

        boolean debugMode = true;      // debugs messages to console
        boolean silentMode = true;    // does not greet itself
        boolean debugMessages = false; // adds bot's full name before it's message, and a ":" after.
        boolean headless = false;
        boolean maximized = true;

        String GRUPKA_ID = "1158615960915822";
        String GRZAGSOFT_ID = "1506449319457834";
        String PATRO_ID = "2275107775897967";

        pcionbot = new Chatbot(username, password, PATRO_ID,
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
