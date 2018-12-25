package bot.core.hollandjake_api.helper.interfaces;

import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;

public interface Module {
    boolean process(Message message) throws MalformedCommandException;

    String getMatch(Message message);

    String appendModulePath(String message);

    ArrayList<String> getCommands();
}