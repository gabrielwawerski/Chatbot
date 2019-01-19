package bot.core;

import bot.core.gabes_framework.core.util.Emoji;

import java.util.ArrayList;

public class PcionBot extends Chatbot {
    public static final String DATABASE_USERNAME = "gabe";
    public static final String DATABASE_PASSWORD = "lezetykurwo";

    public static final String IMGUR_SECRET = "c7aae8e29fbd8eff21c742648125667abdcd579e";
    public static final String IMGUR_ID = "ba98f25f45c4ac9";
    public static final String YANDEX_API_KEY = "trnsl.1.1.20181218T072725Z.426c8360e3601118.ce0d07709d8e3173e8005a6e6e266a65090c04fe";
    public static final String NEWS_API_KEY = "14a5cf3980a1408e9a4e78b002c2b3cd";

    public static final String GRUPKA_ID = "1158615960915822";
    public static final String GRZAGSOFT_ID = "1506449319457834";
    public static final String PATRO_ID = "2275107775897967";
    public static final String MOONSHINERS_ID = "1771365296265469";
    public static final String PCIONBOT_MAIN_ID = "2388008607938113";

    public static final String KARTAPULAPKA_IMG_URL = "https://res.cloudinary.com/drpmvrlik/image/upload/v1547334869/assets/kartapulapka/kartapulapka.jpg";

    public static final boolean SILENT_MODE = true; // does not greet itself or send msg when exception occurs
    public static final boolean LOG_MODE = false;    // doesn't respond to commands - updates database only.

    // TODO reading username, password, thread id and everything else from a file
    // TODO database on a different thread - will allow for multiple database connections! (thread lock - synchronized)
    public static void main(String[] args) {
        System.out.println(args.toString());
        Chatbot pcionbot;
        String username = "ezelbot66@gmail.com";
        String password = "lezetykurwo";

        boolean debugMode = false;      // debugs messages to console
        boolean debugMessages = false; // adds bot name before it's message and " : " after.
        boolean headless = false;
        boolean maximized = true;

        pcionbot = new Chatbot(username, password, GRZAGSOFT_ID,
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
