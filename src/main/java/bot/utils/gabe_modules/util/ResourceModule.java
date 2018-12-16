package bot.utils.gabe_modules.util;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.modules_base.BaseModule;
import bot.utils.bot.helper_class.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceModule extends BaseModule {
    protected List<String> responses;

    public ResourceModule(Chatbot chatbot, List<String> commands, String responsesFile) {
        super(chatbot, commands);

            System.out.println(appendModulePath(responsesFile));
        try {
            responses = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + responsesFile));
        } catch (IOException e) {
            System.out.println(getClass().getSimpleName() + " niedostępne w bieżącej sesji.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        String match = getMatch(message);
        for (String command : commands) {
            if (match.equals(command)) {
                    Random randomizer = new Random();
                    String random = responses.get(randomizer.nextInt(responses.size()));
                    if (responses.get(randomizer.nextInt(responses.size())) == "A skoncz pierdolic") {
                        random = responses.get(randomizer.nextInt(responses.size()));

                        chatbot.sendMessage(random);
                        return true;
                    }
                else {
                    System.out.println("continue block");
                    continue;
                }
            }
        }
        return false;
    }
}
