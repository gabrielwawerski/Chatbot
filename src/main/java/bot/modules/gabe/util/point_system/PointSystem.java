package bot.modules.gabe.util.point_system;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.util.Utils.POINT_SYSTEM_URL;
import static java.util.Objects.isNull;

// TODO make more modular. If some module wants to use the system, find a way to pass it
// PointSystem system = PointSystem.getInstance() ?

/**
 * Main class for anything that uses points.
 *
 * <p><p>v2.0
 * <br>- added !ladder
 * <br>- added !roulette, !roulette all
 * <p>v2.1
 * <br>- added !msg
 * <p>v2.2
 * <br>- added !give command which enables transferring points between users
 *
 * // TODO w chuj uproscic cala klase
 * @version 2.2
 * @since v0.3201
 */
public class PointSystem extends ModuleBase {
    private ArrayList<User> users;
    private ArrayList<Duel> activeDuels;

    private long now;
    private long timeoutRelease;
    private static final long TIMEOUT = 650;

    // TODO fix so it only sends one selected String.
    private static final List<String> FAILED_ROULETTE_NORMAL
            = List.of("Przejebałeś ");
//            + "Straciłeś " + "Odjąłem ci " + "");

    private static final List<String> FAILED_ROULETTE_ALL
            = List.of("️ Przejebałeś wszystkie punkty. Brawo!");

    //region regexes
    // STATS
    private static final String STATS_REGEX = Utils.TO_REGEX("stats");
    private static final String ME_REGEX = Utils.TO_REGEX("me");
    private static final String STATS_ANY_REGEX = Utils.TO_REGEX("stats (.*)");
    private static final String STATS_MENTION_ANY_REGEX = Utils.TO_REGEX("stats @(.*)");

    // PTS AND MSGS LADDER
    private static final String LADDER_REGEX = Utils.TO_REGEX("ladder");
    private static final String LADDER_MSG_REGEX = Utils.TO_REGEX("msg");

    // ROULETTE
    private static final String ROULETTE_REGEX = Utils.TO_REGEX("roulette (\\d+)");
    private static final String ROULETTE_ALL_REGEX = Utils.TO_REGEX("roulette all");
    private static final String VABANK_REGEX = Utils.TO_REGEX("vabank");
    // TODO add these

    // BET
    private static final String DUEL_REGEX = Utils.TO_REGEX("duel (.*) (\\d+)");
    private static final String DUEL_ANY_REGEX = Utils.TO_REGEX("duel (\\d+)");


    private static final String DUEL_ACCEPT_REGEX = Utils.TO_REGEX("y");
    private static final String DUEL_REFUSE_REGEX = Utils.TO_REGEX("n");
    private static final String DUEL_ACCEPT_SIMPLE = "y";
    private static final String DUEL_REFUSE_SIMPLE = "n";

    private static final String BET_MORE_THAN_REGEX = Utils.TO_REGEX("bet (.*) >(\\d+)");
    private static final String BET_LESS_THAN_REGEX = Utils.TO_REGEX("bet (.*) <(\\d+)");

    // !give <punkty> @uzytkownik przekazuje punkty uzytkownikowi
    private static final String GIVE_REGEX = Utils.TO_REGEX("give (.*) (\\d+)");
    //endregion

    public PointSystem(Chatbot chatbot) {
        super(chatbot);
        initialize();

        now = new Date().getTime();
        timeoutRelease = getTimeoutRelease();
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        refreshUsers();
        User user = null;

        if (!isNull(message) && !isNull(message.getSender())) {
            if ((user = getUser(message)) == null) {
                return false;
            }
        }

        updateMatch(message);
        // if msg isn't a command, process msg and return
        // TODO should check regexes from all modules, now only checks this module's regexes!!!
        if (!is(regexes)) {
            processMessage(user, message);
            return true;
        }

        // handle duel
        updateDuels();
        Duel duel = null;
        if ((duel = getDuel(user)) != null) {
            // duel refused
            if (isOr(DUEL_REFUSE_REGEX, DUEL_REFUSE_SIMPLE)) {
                chatbot.sendMessage(user.getName() + " odrzucił wyzwanie " + duel.getInitiator().getName());
                activeDuels.remove(duel);
                return true;
            }

            // duel accepted, process
            else if (isOr(DUEL_ACCEPT_REGEX, DUEL_ACCEPT_SIMPLE)) {
                db.refresh(duel.getInitiator(), duel.getOpponent());

                if (!duel.resolve()) {
                    chatbot.sendMessage("\u274c Pojedynek anulowany.");
                    return false;
                }

                int betPoints = duel.getBet() * 2;

                // transfer points to winner (if loser has enough bet * 2 points, else transfers all points to winner)
                if (betPoints <= duel.getLoser().getPoints() * 2 || betPoints <= duel.getWinner().getPoints() * 2) {
                    Utils.transfer(duel.getWinner(), duel.getLoser(), duel.getBet() * 2);
                } else {
                    Utils.transfer(duel.getWinner(), duel.getLoser(), duel.getLoser().getPoints());
                    betPoints = duel.getLoser().getPoints();
                }

                if (duel.getInitiator() == duel.getWinner()) {
                    chatbot.sendMessage(duel.getInitiator().getName() + " \uD83C\uDD9A " + duel.getOpponent().getName()
                            + "\n"
                            + duel.getWinner().getName() + " wygrywa pojedynek i zdobywa " + betPoints + " pkt! (" + duel.getWinner().getPoints() + ")"
                            + "\n"
                            + duel.getLoser().getName() + " traci " + betPoints + " pkt. (" + duel.getLoser().getPoints() + ")");

                    activeDuels.remove(duel);
                    return true;

                } else if (duel.getInitiator() == duel.getLoser()){
                    chatbot.sendMessage(duel.getInitiator().getName() + " \uD83C\uDD9A " + duel.getOpponent().getName()
                            + "\n"
                            + duel.getLoser().getName() + " przegrywa pojedynek i traci " + betPoints + " pkt (" + duel.getWinner().getPoints() + ")"
                            + "\n"
                            + duel.getLoser().getName() + " wygrywa " + betPoints + " pkt! (" + duel.getLoser().getPoints() + ")");

                    activeDuels.remove(duel);
                    return true;
                } else {
                    System.out.println("Unexpected error.");
                    return false;
                }
            }
            return false;
        }

        // stat check, we don't add points here
        if (is(STATS_REGEX)) {
            db.refresh(user);
            String msg = user.getName()
                    + "\nPunkty: " + user.getPoints()
                    + "\nWiadomości: " + user.getMessageCount();
            chatbot.sendMessage(msg);
            return true;

        } else if (is(ME_REGEX)) {
            db.refresh(user);
            String msg = "Twoje punkty: " + user.getPoints();
            chatbot.sendMessage(msg);
            return true;

        } else if (is(GIVE_REGEX)) {
            Matcher matcher = Pattern.compile(GIVE_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                String desiredUser = matcher.group(1);
                int points = Integer.parseInt(matcher.group(2));
                User receiver;

                if (desiredUser.charAt(0) == '@') {
                    desiredUser = desiredUser.substring(1);
                }

                if ((receiver = getUser(desiredUser)) == null) {
                    chatbot.sendMessage("Brak użytkownika w bazie danych.");
                    return false;
                } else if (receiver.getName() == user.getName())  {
                    chatbot.sendMessage("Nie da rady.");
                    return false;
                } else if (points > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów!");
                    return false;
                } else if (user.getPoints() == 0) {
                    chatbot.sendMessage("Nie masz punktów.");
                    return false;
                } else if (points < 0 || points == 0) {
                    chatbot.sendMessage("No ale jak");
                    return false;
                } else {
                    user.subPoints(points);
                    receiver.addPoints(points);
                    db.update(user);
                    db.update(receiver);
                    chatbot.sendMessage(user.getName() + " właśnie przekazał "
                            + points + " pkt. \u27a1\ufe0f użytkownikowi " + receiver.getName() + "!");
                    return true;
                }
            }
            return false;

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
                    chatbot.sendMessage("Użytkownik nie istnieje w bazie danych.");
                    return false;
                }
                String msg = wantedUser.getName()
                        + "\nPunkty: " + wantedUser.getPoints()
                        + "\nWiadomości: " + wantedUser.getMessageCount();
                chatbot.sendMessage(msg);
                return true;
            }
            return false;

        } else if (is(LADDER_REGEX)) {
            db.refresh(user);
            chatbot.sendMessage(getLadderMsg());
            return true;

        } else if (is(LADDER_MSG_REGEX)) {
            db.refresh(user);
            chatbot.sendMessage(Ladder.getMsgLadder(users).toString());
            return true;

        } else if (is(ROULETTE_REGEX)) {
            if (message.getMessage().length() > 16) {
                chatbot.sendMessage("Podałeś zbyt dużą liczbę!");
                return false;
            }
            Matcher matcher = Pattern.compile(ROULETTE_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                int desiredRoll = Integer.parseInt(matcher.group(1));

                if (desiredRoll <= 0) {
                    chatbot.sendMessage("Nieprawidłowa komenda.");
                    return false;
                } else if (user.getPoints() == 0) {
                    chatbot.sendMessage("Nie masz punktów.");
                    return false;
                } else if (desiredRoll > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów! (" + user.getPoints() + ")");
                    return false;
                }

                if (Utils.fiftyFifty()) {
                    db.refresh(user);
                    user.addPoints(desiredRoll);
                    db.update(user);
                    chatbot.sendMessage("\uD83D\uDE4F Wygrałeś " + desiredRoll + " pkt! (" + user.getPoints() + ")");
                    return true;
                } else {
                    if (user.getPoints() - desiredRoll >= 0) {
                        db.refresh(user);
                        user.subPoints(desiredRoll);
                        db.update(user);
                        chatbot.sendMessage("\u2b07\ufe0f " + Util.GET_RANDOM(FAILED_ROULETTE_NORMAL) + desiredRoll + " pkt. (" + user.getPoints() + ")");
                        return true;
                    } else {
                        db.refresh(user);
                        user.setPoints(0);
                        db.update(user);
                        chatbot.sendMessage("\uD83C\uDD7E️ Przejebałeś wszystkie punkty. Brawo!");
                        return true;
                    }
                }
            } // matcher.find()
            return false;
        } // ROULETTE_REGEX

        else if (isOr(ROULETTE_ALL_REGEX, VABANK_REGEX)) {
            int userPoints = user.getPoints();

            if (userPoints == 0) {
                chatbot.sendMessage("Nie masz punktów.");
                return true;
            }

            if (Utils.fiftyFifty()) {
                db.refresh(user);
                user.addPoints(userPoints);
                db.update(user);
                chatbot.sendMessage("\uD83D\uDCAF Wygrałeś " + userPoints + " pkt! (" + user.getPoints() + ")");
                return true;
            } else {
                db.refresh(user);
                user.setPoints(0);
                db.update(user);
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

                db.refresh(user);
                System.out.println("bet: " + bet + "\ninitiator: " + user.getName() + "\nopponent: " + desiredUser);
                if (user.getPoints() < bet) {
                    chatbot.sendMessage("\u274c Nie masz tylu punktów! (" + user.getPoints() + ")");
                    return false;
                }

                if ((opponent = getUser(desiredUser)) != null) {
                    if (opponent.getPoints() < bet) {
                        chatbot.sendMessage("\u274c Przeciwnik nie ma tylu punktów! (" + opponent.getPoints() + ")");
                        return false;
                    } else if (opponent == user) {
                        chatbot.sendMessage("Nie możesz wyzwać siebie na pojedynek!");
                        return false;
                    } else if (bet == 0) {
                        chatbot.sendMessage("Nie da rady.");
                        return false;
                    } else if (opponent == DBConnection.BOT) {
                        chatbot.sendMessage("Ja się nie bawie");
                        return false;
                    }
                } else {
                    chatbot.sendMessage("\u274c Użytkownik nie istnieje w bazie danych.");
                    return false;
                }

                activeDuels.add(new Duel(user, opponent, bet));
                String msg = user.getName() + " \u2694\ufe0f wyzywa \uD83D\uDEE1 "
                        + opponent.getName() + " na pojedynek o " + bet * 2
                        + " pkt!\nCzekam 60s. na odpowiedź przeciwnika. \n\u23f3 (y/n)";
                chatbot.sendMessage(msg);
                return true;
            }
            return false;
        }
        else if (is(DUEL_ANY_REGEX)) {
            Matcher matcher = Pattern.compile(DUEL_ANY_REGEX).matcher(message.getMessage());
        }

        else if (is(BET_MORE_THAN_REGEX)) {
            Matcher matcher = Pattern.compile(BET_MORE_THAN_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                User opponent = getUser(matcher.group(1));
                int bet = Integer.parseInt(matcher.group(2));

                if (opponent == null) {
                    chatbot.sendMessage("Brak użytkownika w bazie danych");
                    return false;
                }

                if (bet > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów!");
                    return false;
                }
            }
        }
        return false;
    }

    public void logOnly(Message message) {
        refreshUsers();
        User user;

        if (!isNull(message) && !isNull(message.getSender())) {
            user = getUser(message);
            processMessage(user, message);
        }
    }

    private void processMessage(User user, Message message) {
        now = new Date().getTime();
        if (now < timeoutRelease) {
            addMessageCount(user);
            db.update(user);
            return;
        }
        String messageBody = message.getMessage();
        int msgLength = messageBody.length();
        db.refresh(user);

        if (isRegex(message, regexes)) {
            return;
        }

        if (message.getImage() != null) {
            user.addPoints(Utils.POINT_SYSTEM_IMAGE);
            System.out.println(user.getName() + "(+" + Utils.POINT_SYSTEM_IMAGE + ")(IMG)");
        }

        // if message is a url, no points are added.
        if (messageBody.contains("http") || messageBody.contains("www.") || messageBody.contains("//")) {
            user.addPoints(POINT_SYSTEM_URL);
            addMessageCount(user);
            db.update(user);
            return;
        }

        if (msgLength <= 20 && msgLength >= 3) {
            user.addPoints(Utils.POINTS_MAX_CHAR_20);
            System.out.println(user.getName() + "(+" + Utils.POINTS_MAX_CHAR_20 + ")");

        } else if (msgLength <= 60 && msgLength > 20) {
            user.addPoints(Utils.POINTS_MAX_CHAR_60);
            System.out.println(user.getName() + "(+" + Utils.POINTS_MAX_CHAR_60 + ")");

        } else if (msgLength <= 100 && msgLength > 60) {
            user.addPoints(Utils.POINTS_MAX_CHAR_100);
            System.out.println(user.getName() + "(+" + Utils.POINTS_MAX_CHAR_100 + ")");

        } else if (msgLength > 100 && msgLength < 300) {
            user.addPoints(Utils.POINTS_MAX_CHAR_300);
            System.out.println(user.getName() + "(+" + Utils.POINTS_MAX_CHAR_300 + ")");
        }

        timeoutRelease = getTimeoutRelease();
        addMessageCount(user);
        db.update(user);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(
                STATS_REGEX,
                ME_REGEX,
                STATS_ANY_REGEX,
                STATS_MENTION_ANY_REGEX,
                LADDER_REGEX,
                LADDER_MSG_REGEX,
                ROULETTE_REGEX,
                ROULETTE_ALL_REGEX,
                VABANK_REGEX,
                DUEL_REGEX,
                DUEL_ACCEPT_REGEX,
                DUEL_ACCEPT_SIMPLE,
                DUEL_REFUSE_REGEX,
                DUEL_REFUSE_SIMPLE,
                BET_MORE_THAN_REGEX,
                BET_LESS_THAN_REGEX,
                GIVE_REGEX);
    }

    private void initialize() {
        users = new ArrayList<>(10);
        activeDuels = new ArrayList<>();
        System.out.print("\nFetching users from database... ");
        users = db.getUsers();
        System.out.println("done.\n");
    }

    private String getLadderMsg() {
        System.out.println("Generating ladder...");
        Ladder ladder = Ladder.getLadder(users);
        System.out.println("Ladder generated");
        return ladder.toString();
    }

    /**
     * Returns duel instance based on the argument, null if no duel is found with given user.
     */
    private Duel getDuel(User user) {
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
            Iterator<Duel> i = activeDuels.iterator();
            long now = new Date().getTime();
            Duel temp;

            // TODO test
            while (i.hasNext()) {
                temp = i.next();

                if (now - temp.getTimeStarted() > 60000) {
                    activeDuels.remove(temp);
                }
            }
        }
    }

    private long getTimeoutRelease() {
        return new Date().getTime() + TIMEOUT;
    }

    private User getUser(String str) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(str)) {
                return user;
            }
        }
        return null;
    }

    private User getUser(Message message) {
        String sender = message.getSender().getName();

        for (User user : users) {
            if (user.getName().equalsIgnoreCase(sender)) {
                return user;
            }
        }
        return null;
    }

    private void refreshUsers() {
        db.refresh();
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
