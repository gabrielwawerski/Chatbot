package bot.modules.gabe.util.point_system;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.Database;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

/**
 * @since v0.3201
 */
public class PointSystem extends ModuleBase {
    private Database db;
    private ArrayList<User> users;
    private Ladder ladder;

    private boolean userFound = false;
    private Matcher matcher;

    private final String STATS_REGEX = Utils.TO_REGEX("stats");
    private final String STATS_ANY_REGEX = Utils.TO_REGEX("stats (.*)");

    private final String LADDER_REGEX = Utils.TO_REGEX("ladder");

    private final String ROULETTE_ALL_REGEX = Utils.TO_REGEX("roulette all");
    private final String ROULETTE_REGEX = Utils.TO_REGEX("roulette (\\d+)");

    private final String BET_REGEX = Utils.TO_REGEX("bet (\\d+) (.*)"); // TODO

    public PointSystem(Chatbot chatbot, Database db) {
        super(chatbot);
        this.db = db;
        initialize();
    }

    private void initialize() {
        users = new ArrayList<>();
        System.out.println("Fetching users from database...");
        users = db.getUsers();
        System.out.println("Users fetched.");
    }

    private String getLadderMsg() {
        System.out.println("generating ladder..");
        Ladder ladder = Ladder.getLadder(users);
        System.out.println("ladder generated. returning");
        return ladder.toString();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        User user = null;

        if (!isNull(message) && !isNull(message.getSender())) {
            user = getUser(message);
        } else {
            return false;
        }

        updateMatch(message);
        refreshUsers();
        // if msg isn't a command, process msg and return
        if (!is(STATS_REGEX) && !is(LADDER_REGEX) && !is(ROULETTE_ALL_REGEX) && !is(ROULETTE_REGEX)) {
            processMessage(user, message);
            System.out.println("is not regex");
            return false;
        }
        addMessageCount(user);

        // stat check, we don't add points here
        if (is(STATS_REGEX)) {
            System.out.println("stats regex!");
            String msg = user.getName()
                    + "\nPunkty: " + user.getPoints()
                    + "\nWiadomości: " + user.getMessageCount();
            update(user);
            chatbot.sendMessage(msg);
            return true;

        } else if (is(LADDER_REGEX)) {
            update(user);
            chatbot.sendMessage(getLadderMsg());
            return true;

        } else if (is(ROULETTE_REGEX)) {
            Matcher matcher = Pattern.compile(ROULETTE_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                int desiredRoll = Integer.parseInt(matcher.group(1));
                System.out.println("user input: " + desiredRoll);

                if (desiredRoll <= 0) {
                    chatbot.sendMessage("Nieprawidłowa komenda.");
                    update(user);
                    return false;
                } else if (user.getPoints() == 0) {
                    chatbot.sendMessage("Nie masz punktów.");
                    update(user);
                    return false;
                } else if (desiredRoll > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów!");
                    update(user);
                    return false;
                }
                boolean result = getFiftyFifty();
                System.out.println("pts before: " + user.getPoints());

                if (result) {
                    user.addPoints(desiredRoll * 2);
                    update(user);
                    chatbot.sendMessage("Wygrałeś " + desiredRoll * 2 + " pkt!");
                    return true;
                } else {
                    if (user.getPoints() - desiredRoll >= 0) {
                        user.subPoints(desiredRoll);
                        update(user);
                        System.out.println("pts after: " + user.getPoints());
                        chatbot.sendMessage("Przejebałeś " + desiredRoll + " pkt.");
                        return true;
                    } else {
                        System.out.println("also here!!!");
                        user.setPoints(0);
                        update(user);
                        chatbot.sendMessage("Przejebałeś wszystkie punkty. Brawo!");
                        return true;
                    }
                }
            } // matcher.find()
            return false;
        } // ROULETTE_REGEX
        else if (is(ROULETTE_ALL_REGEX)) {
            int userPoints = user.getPoints();
            if (userPoints == 0) {
                chatbot.sendMessage("Nie masz punktów.");
                return true;
            }

            boolean result = getFiftyFifty();
            if (result) {
                user.setPoints(userPoints * 2);
                update(user);
                chatbot.sendMessage("Wygrałeś " + userPoints + " pkt!");
                return true;
            } else {
                System.out.println("tu jestesmy?");
                user.setPoints(0);
                update(user);
                chatbot.sendMessage(userPoints + " pkt poszło się jebać. Brawo!");
                return true;
            }
        }
        return false;
    }

    private boolean getFiftyFifty() {
        int min = 0;
        int max = 1;
        int range = (max - min) + 1;

        int result = (int) (Math.random() * range) + min;
        if (result == 1) {
            System.out.println("wygrana");
            return true;
        } else {
            System.out.println("przegrana");
            return false;
        }
    }

    private void processMessage(User user, Message message) {
        User currUser = user;
        String messageBody = message.getMessage();
        int msgLength = messageBody.length();


        if (message.getImage() != null) {
            user.addPoints(2);
            System.out.println("+2 PTS (IMG) " + user.getName());
        }

        if (messageBody.contains("http") || messageBody.contains("www.") || messageBody.contains("//")) {
            addMessageCount(currUser);
            update(currUser);
            return;
        }

        if (msgLength <= 20 && msgLength >= 3) {
            user.addPoints(1);
            System.out.println("+1 PTS " + user.getName());

        } else if (msgLength <= 60 && msgLength > 20) {
            user.addPoints(2);
            System.out.println("+2 PTS " + user.getName());

        } else if (msgLength <= 100 && msgLength > 60) {
            user.addPoints(5);
            System.out.println("+5 PTS " + user.getName());

        } else if (msgLength > 100) {
            user.addPoints(10);
            System.out.println("+10 PTS " + user.getName());
        }


        addMessageCount(currUser);
        update(currUser);
    }

    private void addMessage(User user, Message message) {
//        db.addMessage()
    }

    private void addPoints(User user, int points) {
        user.addPoints(points);
        db.update(user);
    }

    private void addMessageCount(User user) {
        user.addMessagecount(1);
        System.out.print("  +1 MSG " + user.getName().substring(0, 8) + "\n");
    }

    private void update(User user) {
        db.update(user);
    }

    private boolean isUser(Message message) {
        for (User user : users) {
            if (message.getSender().getName().equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

    private User getUser(Message message) {
        String sender = message.getSender().getName();

        for (User user : users) {
            if (user.getName().equals(sender)) {
                return user;
            }
        }
        return null;
    }

    private void refreshUsers() {
        db.refreshAll(users);
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, STATS_REGEX, LADDER_REGEX, ROULETTE_REGEX, ROULETTE_ALL_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(STATS_REGEX, LADDER_REGEX, ROULETTE_REGEX, ROULETTE_ALL_REGEX);
    }

    private void addUser(Message message) {
        User user = new User(users.size() - 1, message.getSender().getName(), 0, 0);
        db.createUser(user);
        users.add(user);
    }

    public void createUsers() {
        User Gabe = new User("Gabriel Wawerski");
        User Bot = new User("BOT");
        User Hube = new User("Hubert Hubert");
        User Leze = new User("Jakub Smolak");
        User Mege = new User("Mikołaj Batyra");
        User Jakubow = new User("Kamil Jakubowski");
        User Melchior = new User("Michał Melchior");
        User Kaspe = new User("Kamil Kasprzak");
        User Petek = new User("Piotr Bartoszek");
        User Wiesio = new User("Jarek Rodak");
        db.createUser(Gabe);
        db.createUser(Bot);
        db.createUser(Hube);
        db.createUser(Leze);
        db.createUser(Mege);
        db.createUser(Jakubow);
        db.createUser(Melchior);
        db.createUser(Kaspe);
        db.createUser(Petek);
        db.createUser(Wiesio);
    }
}
