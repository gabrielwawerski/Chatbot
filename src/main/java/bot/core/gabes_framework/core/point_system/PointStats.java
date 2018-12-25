package bot.core.gabes_framework.core.point_system;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static bot.core.gabes_framework.core.point_system.Users.*;

/**
 * @since v0.3201
 */
public class PointStats extends ModuleBase implements DataHandler {
    HashMap<String, DataContainer> users;

    public PointStats(Chatbot chatbot) {
        super(chatbot);
        users = new HashMap<>();
    }


    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }

    @Override
    public void load() {
        for (Users user : USER_LIST) {
            users.put(user.name(), new User(user));
        }
    }

//    public List<String> getData() {
//    }

    @Override
    public void save() {
        for (DataContainer data : users.values()) {
        }
    }

    private List<Users> USER_LIST
            = List.of(BOT,
            GABE,
            LEZE,
            HUBE,
            KASPE,
            WIESIO,
            JAKUBOW,
            MELCHIOR,
            MEGE,
            PETEK);

    @Override
    public String getMatch(Message message) {
        return null;
    }

    @Override
    public ArrayList<String> getCommands() {
        return null;
    }
}
