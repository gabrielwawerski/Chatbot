package bot.core.gabes_framework.framework.resource;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.ModuleBase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
            // TODO writing should not be performed on .jars. Write to external files!
            // location can be dynamic - with sout below! so get jar path and read/write files from there.

            // returns proper path to .jar
            System.out.println(this.getClass().getProtectionDomain().getCodeSource().getLocation());

            File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());

            this.file = Files.readAllLines(Paths.get(null));
            setOnline();
        } catch (IOException e) { // TODO add global debugMessages field in Chatbot so this can be toggled.
            setOffline();
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
