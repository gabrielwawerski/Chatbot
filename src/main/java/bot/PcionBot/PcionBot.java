package bot.PcionBot;

import bot.Chatbot;

public class PcionBot extends Chatbot {

    public static void main(String[] args) {
        Chatbot pcionbot;
        String username = "ezelbot66@gmail.com";
        String password = "lezetykurwo";

        boolean debugMode = false;
        boolean silentMode = true;
        boolean debugMessages = false;
        boolean headless = false;
        boolean maximized = true;

        String GRUPKA_ID = "1158615960915822";
        String GRZAGSOFT_ID = "1506449319457834";

//        System.out.println(String.format("%12s%12s%1s%n", "Temperatura","-2", "oC"));
//        System.out.println(String.format("%12s%12s%1s%n", "Wiatr", "26", "km/h"));

        pcionbot = new Chatbot(
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

    @Override
    public String appendRootPath(String path) {
        return "src/main/resources/" + path;
    }
}
