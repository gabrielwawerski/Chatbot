package bot.core.helper.interfaces;

import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;

import java.util.ArrayList;

public interface Module {
    boolean process(Message message) throws MalformedCommandException;

    String getMatch(Message message);

    String appendModulePath(String message);

    ArrayList<String> getCommands();
}