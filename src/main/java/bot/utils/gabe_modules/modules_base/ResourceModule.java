package bot.utils.gabe_modules.modules_base;

import bot.Chatbot;
import bot.utils.exceptions.MalformedCommandException;
import bot.utils.helper_class.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ResourceModule extends SimpleModule {
    private List<String> responses;

    public ResourceModule(Chatbot chatbot, List<String> commands, String responsesFile) {
        super(chatbot, commands);

        try {
            responses = Files.readAllLines(Paths.get(appendModulePath(responsesFile)));
        } catch (IOException e) {
            System.out.println(getClass().getSimpleName() + " messages are not available this session");
        }
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }
}
