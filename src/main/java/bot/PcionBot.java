package bot;

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

    public static void main(String[] args) {
        System.out.println(args.toString());
        Chatbot pcionbot;
        String username = "ezelbot66@gmail.com";
        String password = "lezetykurwo";

        boolean debugMode = false;      // debugs messages to console
        boolean silentMode = true;    // does not greet itself
        boolean debugMessages = false; // adds bot's full name before it's message, and a ":" after.
        boolean headless = false;
        boolean maximized = true;

        String GRUPKA_ID = "1158615960915822";
        String GRZAGSOFT_ID = "1506449319457834";
        String PATRO_ID = "2275107775897967";

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
