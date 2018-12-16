package bot.utils.bot.helper.helper_interface;

import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_class.Message;

import java.util.ArrayList;

public interface Module {
    boolean process(Message message) throws MalformedCommandException;

    String getMatch(Message message);

    String appendModulePath(String message);

    ArrayList<String> getCommands();
}