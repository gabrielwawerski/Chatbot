package bot.modules.gabe.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mention extends ModuleBase {
    private static final String MENTION_REGEX = Utils.TO_REGEX("call (.*?)");

    public Mention(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        User user;
        String msg;

        if (is(MENTION_REGEX)) {
            Matcher matcher = Pattern.compile(MENTION_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                user = db.findUser(matcher.group(1));

                if (user == User.EMPTY_USER) {
                    System.out.println("empty user");
                    return false;
                }
                chatbot.sendMentionMessage(user.getName());
                return true;
            }
            System.out.println("not found");
            return false;
        }
        System.out.println("not regex");
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(MENTION_REGEX);
    }
}
