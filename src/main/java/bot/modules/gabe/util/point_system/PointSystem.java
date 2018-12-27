package bot.modules.gabe.util.point_system;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.Utils;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

// TODO make more modular. If some module wants to use the system, find a way to pass it
// PointSystem system = PointSystem.getInstance() ?

/**
 * Main class for anything that uses points.
 *
 * <p><p>v0.2
 * <br>- added !ladder
 * <br>- added !roulette, !roulette all
 *
 * @version 0.2
 * @since v0.3201
 */
public class PointSystem extends ModuleBase {
    private DBConnection db;
    private Ladder ladder;
    private ArrayList<User> users;
    private ArrayList<Duel> activeDuels;

    private boolean userFound = false;
    private Matcher matcher;

    // STATS
    private final String STATS_REGEX = Utils.TO_REGEX("stats");
    private final String STATS_ANY_REGEX = Utils.TO_REGEX("stats (.*)");
    private final String STATS_MENTION_ANY_REGEX = Utils.TO_REGEX("stats @(.*)");

    // PTS AND MSGS LADDER
    private final String LADDER_REGEX = Utils.TO_REGEX("ladder");
    private final String LADDER_MSG_REGEX = Utils.TO_REGEX("ladder msg");

    // ROULETTE
    private final String ROULETTE_REGEX = Utils.TO_REGEX("roulette (\\d+)");
    private final String ROULETTE_ALL_REGEX = Utils.TO_REGEX("roulette all");
    // TODO add these
//    private final String RO_REGEX = Utils.TO_REGEX("ro (\\d+)");
//    private final String RO_ALL_REGEX = Utils.TO_REGEX("ro all");

    // BET
    private final String DUEL_REGEX = Utils.TO_REGEX("duel (.*) (\\d+)"); // TODO
//    private final String DUEL_REGEX = Utils.TO_REGEX("duel (\\d+) (.*)"); // TODO

    private final String DUEL_ANY_REGEX = Utils.TO_REGEX("duel (\\d+)"); // TODO

    private final String DUEL_ACCEPT_REGEX = Utils.TO_REGEX("y");
    private final String DUEL_ACCEPT_SIMPLE = "y";
    private final String DUEL_REFUSE_REGEX = Utils.TO_REGEX("n");
    private final String DUEL_REFUSE_SIMPLE = "n";

    private final String BET_MORE_THAN_REGEX = Utils.TO_REGEX("bet >(\\d+) (.*)");
    private final String BET_FEWER_THAN_REGEX = Utils.TO_REGEX("bet <(\\d+) (.*)");

    // !give <punkty> @uzytkownik przekazuje punkty uzytkownikowi
    private final List<String> REGEXES;

    public PointSystem(Chatbot chatbot, DBConnection db) {
        super(chatbot);
        this.db = db;
        initialize();
        REGEXES = List.of(
                STATS_REGEX,
                STATS_ANY_REGEX,
                STATS_MENTION_ANY_REGEX,
                LADDER_REGEX,
                LADDER_MSG_REGEX,
                ROULETTE_REGEX,
                ROULETTE_ALL_REGEX,
                DUEL_REGEX,
                DUEL_ACCEPT_REGEX,
                DUEL_ACCEPT_SIMPLE,
                DUEL_REFUSE_REGEX,
                DUEL_REFUSE_SIMPLE);
    }

    private void initialize() {
        users = new ArrayList<>();
        activeDuels = new ArrayList<>();
        System.out.println("Fetching users from database...");
        users = db.getUsers();
        System.out.println("Users fetched.");
    }

    private String getLadderMsg() {
        System.out.println("Generating ladder...");
        Ladder ladder = Ladder.getLadder(users);
        System.out.println("Ladder generated");
        return ladder.toString();
    }

    private Duel getDuelIfActive(User user) {
        long now = new Date().getTime();

        for (Duel duel : activeDuels) {
            if (now - duel.getTimeStarted() > 60000) {
                return null;
            }

            if (duel.getOpponent() == user) {
                return duel;
            }
        }
        return null;
    }

    private void updateDuels() {
        if (!activeDuels.isEmpty()) {
            long now = new Date().getTime();
            for (int i = 0; i < activeDuels.size(); i++) {
                if (now - activeDuels.get(i).getTimeStarted() > 60000) {
                    activeDuels.remove(i);
                }
            }
        } else {
            return;
        }
    }

    public void logOnly(Message message) {
        refreshUsers();
        User user = null;

        if (!isNull(message) && !isNull(message.getSender())) {
            user = getUser(message);
            processMessage(user, message);
        } else {
            return;
        }
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        refreshUsers();
        User user = null;

        if (!isNull(message) && !isNull(message.getSender())) {
            user = getUser(message);
        } else {
            return false;
        }

        updateMatch(message);
        // if msg isn't a command, process msg and return
        if (!is(REGEXES)) {
            processMessage(user, message);
            return true;
        }

        updateDuels();
        Duel duel = null;
        if ((duel = getDuelIfActive(user)) != null) {
            System.out.println("found active duel!");
            if (isOr(DUEL_REFUSE_REGEX, DUEL_REFUSE_SIMPLE)) {
                chatbot.sendMessage(user.getName()
                        + " odrzucił wyzwanie " + duel.getInitiator().getName());
                activeDuels.remove(duel);
                return true;
            } else if (isOr(DUEL_ACCEPT_REGEX, DUEL_ACCEPT_SIMPLE)) {
                db.refresh(duel.getWinner());
                db.refresh(duel.getLoser());
                if (!duel.resolve()) {
                    chatbot.sendMessage("\u274c Wasze punkty uległy zmianie. Pojedynek anulowany.");
                }

                duel.getWinner().addPoints(duel.getBet() * 2);
                // TODO winner steals points up to 0 if loser doesn't have enough
                duel.getLoser().subPoints(duel.getBet() * 2);

                update(duel.getWinner());
                update(duel.getLoser());
                chatbot.sendMessage(duel.getInitiator().getName() + " \uD83C\uDD9A " + duel.getOpponent().getName()
                        + "\n"
                        + duel.getWinner().getName() + " wygrywa pojedynek i zdobywa " + duel.getBet() * 2 + " pkt! (" + duel.getWinner().getPoints() + ")"
                        + "\n"
                        + duel.getLoser().getName() + " traci " + duel.getBet() * 2 + " pkt. (" + duel.getLoser().getPoints() + ")");

                activeDuels.remove(duel);
                return true;
            }
        }

        addMessageCount(user);
        // stat check, we don't add points here
        if (is(STATS_REGEX)) {
            String msg = user.getName()
                    + "\nPunkty: " + user.getPoints()
                    + "\nWiadomości: " + user.getMessageCount();
            update(user);
            chatbot.sendMessage(msg);
            return true;

        } else if (isOr(STATS_ANY_REGEX, STATS_MENTION_ANY_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(message.getMessage());
            if (matcher.find()) {
                String desiredUser = matcher.group(1);

                // remove @ tag if user used messenger's mention feature
                if (desiredUser.charAt(0) == '@') {
                    desiredUser = desiredUser.substring(1);
                }

                User wantedUser = null;
                for (User usr : users) {
                    if (usr.getName().equalsIgnoreCase(desiredUser)) {
                        wantedUser = usr;
                    }
                }

                if (wantedUser == null) {
                    update(user);
                    chatbot.sendMessage("Użytkownik nie istnieje w bazie danych.");
                    return false;
                }
                String msg = wantedUser.getName()
                        + "\nPunkty: " + wantedUser.getPoints()
                        + "\nWiadomości: " + wantedUser.getMessageCount();
                update(user);
                chatbot.sendMessage(msg);
                return true;
            }
            return false;
        } else if (is(LADDER_REGEX)) {
            update(user);
            chatbot.sendMessage(getLadderMsg());
            return true;

        } else if (is(LADDER_MSG_REGEX)) {
            update(user);
            chatbot.sendMessage(Ladder.getMsgLadder(users).toString());
            return true;

        } else if (is(ROULETTE_REGEX)) {
            if (message.getMessage().length() > 16) {
                update(user);
                chatbot.sendMessage("Podałeś zbyt dużą liczbę!");
                return false;
            }
            Matcher matcher = Pattern.compile(ROULETTE_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                int desiredRoll = Integer.parseInt(matcher.group(1));

                if (desiredRoll <= 0) {
                    chatbot.sendMessage("Nieprawidłowa komenda.");
                    update(user);
                    return false;

                } else if (user.getPoints() == 0) {
                    chatbot.sendMessage("Nie masz punktów.");
                    update(user);
                    return false;

                } else if (desiredRoll > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów! (" + user.getPoints() + ")");
                    update(user);
                    return false;
                }

                if (getFiftyFifty()) {
                    user.addPoints(desiredRoll);
                    update(user);
                    chatbot.sendMessage("\uD83D\uDE4F Wygrałeś " + desiredRoll + " pkt! (" + user.getPoints() + ")");
                    return true;
                } else {
                    if (user.getPoints() - desiredRoll >= 0) {
                        user.subPoints(desiredRoll);
                        update(user);
                        chatbot.sendMessage("Przejebałeś " + desiredRoll + " pkt. (" + user.getPoints() + ")");
                        return true;
                    } else {
                        user.setPoints(0);
                        update(user);
                        chatbot.sendMessage("\uD83C\uDD7E️ Przejebałeś wszystkie punkty. Brawo!");
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

            if (getFiftyFifty()) {
                user.addPoints(userPoints);
                update(user);
                chatbot.sendMessage("\uD83D\uDCAF Wygrałeś " + userPoints + " pkt! (" + user.getPoints() + ")");
                return true;
            } else {
                user.setPoints(0);
                update(user);
                chatbot.sendMessage(userPoints + " pkt poszło się jebać. Gratulacje!");
                return true;
            }
        } // ROULETTE_ALL_REGEX

        else if (is(DUEL_REGEX)) {
            Matcher matcher = Pattern.compile(DUEL_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                String desiredUser = matcher.group(1);
                int bet = Integer.parseInt(matcher.group(2));
                User opponent;

                if (desiredUser.charAt(0) == '@') {
                    desiredUser = desiredUser.substring(1);
                }

                System.out.println("bet: " + bet + "\nopponent: " + desiredUser);
                if (user.getPoints() < bet) {
                    chatbot.sendMessage("\u274c Nie masz tylu punktów! (" + user.getPoints() + ")");
                    update(user);
                    return false;
                }

                if ((opponent = getUser(desiredUser)) != null) {
                    if (opponent.getPoints() < bet) {
                        chatbot.sendMessage("\u274c Przeciwnik nie ma tylu punktów! (" + opponent.getPoints() + ")");
                        update(user);
                        return false;
                    }
                } else {
                    chatbot.sendMessage("\u274c Użytkownik nie istnieje w bazie danych.");
                    update(user);
                    return false;
                }

                activeDuels.add(new Duel(user, opponent, bet));
                chatbot.sendMessage(user.getName() + " \u2694\ufe0f wyzywa \uD83D\uDEE1 " + opponent.getName() + " na pojedynek!"
                        + "\n\u23f3 Czekam 60s. na odpowiedź przeciwnika. (y/n)");
                return true;
            }
            return false;
        }
//        else if (is(DUEL_ANY_REGEX)) {
//            Matcher matcher = Pattern.compile(DUEL_REGEX).matcher(message.getMessage());
//            if (matcher.find()) {
//                int bet = Integer.parseInt(matcher.group(1));
//            }
//
//        }
        return false;
    }

    private void processMessage(User user, Message message) {
        String messageBody = message.getMessage();
        int msgLength = messageBody.length();
        db.refresh(user);

        if (isCmd(message, chatbot.getRegexes()) || isCmd(message, REGEXES)) {
            addMessageCount(user);
            addPoints(user, 1);
            System.out.println("(+1)(CMD)");
            return;
        } else {
        }

        if (message.getImage() != null) {
            user.addPoints(3);
            System.out.println("(+3)(IMG) " + user.getName());
        }

        // if message is a url, no points are added.
        if (messageBody.contains("http") || messageBody.contains("www.") || messageBody.contains("//")) {
            addMessageCount(user);
            update(user);
            return;
        }

        if (msgLength <= 20 && msgLength >= 3) {
            user.addPoints(1);
            System.out.println("(+1) " + user.getName());

        } else if (msgLength <= 60 && msgLength > 20) {
            user.addPoints(2);
            System.out.println("(+2) " + user.getName());

        } else if (msgLength <= 100 && msgLength > 60) {
            user.addPoints(5);
            System.out.println("(+5) " + user.getName());

        } else if (msgLength > 100 && msgLength < 300) {
            user.addPoints(10);
            System.out.println("(+10) " + user.getName());
        }

        addMessageCount(user);
        update(user);
    }

    public static boolean getFiftyFifty() {
        int min = 0;
        int max = 1;
        int range = (max - min) + 1;

        int result = (int) (Math.random() * range) + min;
        if (result == 1) {
            return true;
        } else {
            return false;
        }
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
        System.out.print("(+1 MSG) " + user.getName().substring(0, 8) + "\n");
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

    private User getUser(String str) {
        for (User user : users) {
            if (user.getName().equals(str)) {
                return user;
            }
        }
        return null;
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
        return findMatch(message, REGEXES);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(REGEXES);
    }

    private void addUser(Message message) {
        User user = new User(users.size() - 1, message.getSender().getName(), 0, 0);
        db.createUser(user);
        users.add(user);
    }

    private void createUsers() {
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
