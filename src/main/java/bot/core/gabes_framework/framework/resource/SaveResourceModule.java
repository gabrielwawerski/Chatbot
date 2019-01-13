package bot.core.gabes_framework.framework.resource;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.ModuleBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @see #appendStringToFile(String, String)
 */
public abstract class SaveResourceModule extends ModuleBase {
    private String fileName;
    private List<String> file;

    public SaveResourceModule(Chatbot chatbot, String fileName) {
        super(chatbot);

        try {
            this.file = Files.readAllLines(Paths.get("modules/" + getClass().getSimpleName() + "/" + fileName));
            setOnline(true);
        } catch (IOException e) { // TODO add global debugMessages field in Chatbot so this can be toggled.
            setOnline(false);
            e.printStackTrace();
        }
        this.fileName = "modules/" + getClass().getSimpleName() + "/" + fileName;
    }

    public void appendStringToFile(String stringToAppend) {
        stringToAppend += "\n";
        try {
            Files.write(Paths.get(fileName), stringToAppend.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
