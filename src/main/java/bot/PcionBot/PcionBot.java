package bot.PcionBot;

import bot.Chatbot;

public class PcionBot extends Chatbot {

    public static void main(String[] args) {
        String username = "ezelbot66@gmail.com";
        String password = "lezetykurwo";

        boolean debugMode = false;
        boolean silentMode = true;
        boolean debugMessages = false;
        boolean headless = false;
        boolean maximized = true;

        String GRUPKA_ID = "1158615960915822";
        String GRZAGSOFT_ID = "1506449319457834";

        Chatbot pcionBot = new Chatbot(
                username,
                password,

                GRZAGSOFT_ID,

                debugMode,
                silentMode,
                debugMessages,
                headless,
                maximized
        );
    }
}
