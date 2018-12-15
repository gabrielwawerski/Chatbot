package bot.utils.helper_interface;

import bot.utils.exceptions.MalformedCommandException;
import bot.utils.helper_class.Message;

import java.util.ArrayList;

public interface Module {
    boolean process(Message message) throws MalformedCommandException;

    String getMatch(Message message);

    String appendModulePath(String message);

    ArrayList<String> getCommands();
}