package bot.core;

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

    public static final String imgurSecret = "c7aae8e29fbd8eff21c742648125667abdcd579e";
    public static final String imgurId = "ba98f25f45c4ac9";
    public static final String yandexApiKey = "trnsl.1.1.20181218T072725Z.426c8360e3601118.ce0d07709d8e3173e8005a6e6e266a65090c04fe";
    public static final String newsapiKey = "14a5cf3980a1408e9a4e78b002c2b3cd";

    public static final String ID_GRUPKA = "1158615960915822";
    public static final String ID_GRZAGSOFT = "1506449319457834";
    public static final String ID_PATRO = "2275107775897967";
    public static final String ID_MOONSHINERS = "1771365296265469";

    public static final boolean SILENT_MODE = true; // does not greet itself or send msg when exception occurs
    public static final boolean LOG_MODE = false;    // doesn't respond to commands - updates database only.

    public static void main(String[] args) {
        System.out.println(args.toString());
        Chatbot pcionbot;
        String username = "ezelbot66@gmail.com";
        String password = "lezetykurwo";

        boolean debugMode = false;      // debugs messages to console
        boolean debugMessages = false; // adds bot name before it's message and " : " after.
        boolean headless = false;
        boolean maximized = true;

        pcionbot = new Chatbot(username, password, ID_GRUPKA,
                debugMode,
                SILENT_MODE,
                debugMessages,
                headless,
                maximized,
                LOG_MODE
        );
    }

    @Override
    public String appendRootPath(String path) {
        return "src/main/resources/" + path;
    }
}
