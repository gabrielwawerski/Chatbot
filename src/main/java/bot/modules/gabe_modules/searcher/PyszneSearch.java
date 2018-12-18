package bot.modules.gabe_modules.searcher;

import bot.Chatbot;
import bot.impl.orig_impl.exceptions.MalformedCommandException;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.gabes_framework.search.SearchModuleBase;
import bot.impl.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @version 1.0

 * @since 0.30 (18.12.2018)
 */
public class PyszneSearch extends SearchModuleBase {
    private static final String SEARCH_URL = "pyszne.pl/restauracja-lublin-lublin-";
    private static final String SEPARATOR = "-";

    String re1="(\\d)";	// Any Single Digit 1
    String re2="(\\d)";	// Any Single Digit 2
    String re3="(-)";	// Any Single Character 1
    String re4="(\\d)";	// Any Single Digit 3
    String re5="(\\d)";	// Any Single Digit 4
    String re6="(\\d)";	// Any Single Digit 5



    private static final String PYSZNE_HELP_REGEX = makeRegex("pyszne|help");
    private static final String PYSZNE_ANY_REGEX = makeRegex("pyszne (.*)");

    public PyszneSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();
        String msgToAnalyze = null;

        if (msgToAnalyze.length() < 6 || msgToAnalyze.length() > 6) { // CHECK IF WORKS PROPERLY
            chatbot.sendMessage("Nieprawidłowy kod pocztowy.");
            return true; // TODO CHECK WHAT HAPPENS WHEN RETURNING FALSE

        } else if (!analyzeMessage(msgToAnalyze)) {
            chatbot.sendMessage("Nieprawidłowy kod pocztowy.");
            return true;
        } else {
            chatbot.sendMessage(getFinalMessage(messageBody));
        }

        for (String regex : regexList) {
            if (match.equals(regex)) {
                updateMatcher(messageBody);

                if (isMatchFound()) {
                    chatbot.sendMessage(getFinalMessage(messageBody));
                    return true;
                } else {
                    throw new MalformedCommandException();
                }
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.matches(PYSZNE_HELP_REGEX)) {
            return PYSZNE_HELP_REGEX;
        } else if (messageBody.matches(PYSZNE_ANY_REGEX)) {
            return PYSZNE_ANY_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.deactionify(PYSZNE_ANY_REGEX));
        commands.add(Utils.deactionify(PYSZNE_HELP_REGEX));
        return commands;
    }

    private boolean analyzeMessage(String messageBody) {
        return Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(0))).find()      // Yx-xxx
                && Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(1))).find()  // xY-xxx
                && (messageBody.charAt(2) == '-' && messageBody.charAt(2) == ' ')                      // - /spacja
                && Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(3))).find()  // xx-Yxx
                && Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(4))).find()  // xx-xYx
                && Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(5))).find(); // xx-xxY
//
//        Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
//        Matcher m = p.matcher(txt);
//        if (m.find())
//        {
//            String d1=m.group(1);
//            String d2=m.group(2);
//            String c1=m.group(3);
//            String d3=m.group(4);
//            String d4=m.group(5);
//            String d5=m.group(6);
//            System.out.print("("+d1.toString()+")"+"("+d2.toString()+")"+"("+c1.toString()+")"+"("+d3.toString()+")"+"("+d4.toString()+")"+"("+d5.toString()+")"+"\n");
//        }
//    }
    }

    @Override
    protected String setSearchUrl() {
        return SEARCH_URL;
    }

    @Override
    protected String setSeparator() {
        return SEPARATOR;
    }
}
