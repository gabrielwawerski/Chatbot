package bot.utils.gabe_modules.modules_base;

import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper_class.Message;

import java.util.ArrayList;

/**
 * @author Gabe
 */
public interface Module {
    /**
     * Use this snippet if your module will send only one, same message.
     * <pre>{@code
     * String match = getMatch(message);
     *     for (String command : commands) {
     *         if (match.equals(command)) {
     *             chatbot.sendMessage(this.message);
     *                 return true;
     *         }
     *     }
     *     return false;
     *     }</pre>
     * @param message latest message from API
     * @return true, if request has been successfully processed, false otherwies
     * @throws MalformedCommandException
     */
    public boolean process(Message message) throws MalformedCommandException;

    public String getMatch(Message message);

    public void updateMatch(Message message);

    public String appendModulePath(String message);

    public ArrayList<String> getCommands();
}
