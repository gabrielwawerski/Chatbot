package bot.impl.orig_impl.helper.interfaces;

import bot.impl.orig_impl.exceptions.MalformedCommandException;
import bot.impl.orig_impl.helper.misc.Message;

import java.util.ArrayList;

public interface Module {
    boolean process(Message message) throws MalformedCommandException;

    String getMatch(Message message);

    String appendModulePath(String message);

    ArrayList<String> getCommands();
}