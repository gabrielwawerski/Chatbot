package bot.modules.gabe.point_system;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.database.Users;
import bot.modules.gabe.point_system.submodule.PointUtils;
import bot.modules.gabe.point_system.util.Giveaway;
import bot.modules.gabe.point_system.util.Points;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.modules.gabe.point_system.util.Duel;
import bot.modules.gabe.point_system.util.Ladder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.database.User.EMPTY_USER;
import static bot.core.gabes_framework.core.util.Utils.TO_REGEX;
import static bot.modules.gabe.point_system.util.Giveaway.PUBLIC_GIVEAWAY;
import static bot.modules.gabe.point_system.util.Points.SYSTEM_URL;
import static bot.modules.gabe.point_system.submodule.PointUtils.*;

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
 * @version 2.2
 * @since v0.3201
 */
// TODO split into several classes - this class should only be a utility class for other modules that need point system functionality
// singleton!!
// make this class like ModuleBase - framework for point related modules!!!!!!!!!!!!!!!
// FIXME DO IT UP!!! /\
    // TODO komenda !daily - jedno dzienne zgarniecie x punktow
public class PointSystem extends ModuleBase {
    private ArrayList<Duel> activeDuels;
    private long timeoutRelease;
    private static final long TIMEOUT = 350;

    // TODO fix so it only sends one selected String.
    private static final List<String> FAILED_ROULETTE_NORMAL
            = List.of("Przejebałeś ");
//            + "Straciłeś " + "Odjąłem ci " + "");

    private static final List<String> FAILED_ROULETTE_ALL
            = List.of("️ Przejebałeś wszystkie punkty. Brawo!");

    //region regexes
    // STATS
    private static final String REGEX_ME = TO_REGEX("me");
    private static final String REGEX_STATS = TO_REGEX("stats");
    private static final String REGEX_POINTS = TO_REGEX("points");
    private static final String REGEX_PTS = TO_REGEX("pts");
    private static final String REGEX_STATS_ANY = TO_REGEX("stats (.*)");
    private static final String REGEX_POINTS_ANY = TO_REGEX("points (.*)");
    private static final String REGEX_PTS_ANY = TO_REGEX("pts (.*)");
    // PTS AND MSGS LADDER
    private static final String LADDER_REGEX = TO_REGEX("ladder");
    private static final String LADDER_MSG_REGEX = TO_REGEX("msg"); // msg
    // ROULETTE
    private static final String ROULETTE_REGEX = TO_REGEX("roulette (\\d+)");
    private static final String ROULETTE_ALL_REGEX = TO_REGEX("roulette all");
    private static final String VABANK_REGEX = TO_REGEX("vabank");
    // DUEL
    private static final String DUEL_REGEX = TO_REGEX("duel (.*) (\\d+)");
    private static final String DUEL_ACCEPT_REGEX = TO_REGEX("y");
    private static final String DUEL_REFUSE_REGEX = TO_REGEX("n");
    private static final String DUEL_ACCEPT_SIMPLE = "y";
    private static final String DUEL_REFUSE_SIMPLE = "n";

    private static final String DUEL_ANY_REGEX = TO_REGEX("duel (\\d+)");
    private static final String DUEL_ANY_ACCEPT_REGEX = TO_REGEX("go");

    // TODO BET
    private static final String BET_MORE_THAN_REGEX = TO_REGEX("bet (.*) >(\\d+)");
    private static final String BET_LESS_THAN_REGEX = TO_REGEX("bet (.*) <(\\d+)");
    // TRANSFER
    private static final String GIVE_REGEX = TO_REGEX("give (.*) (\\d+)");

    private static final String GIVEAWAY_REGEX = TO_REGEX("giveaway (\\d+)");
    private static final String REGEX_GIVEAWAY_CLAIM = TO_REGEX("claim");

    private static final String AWARD_REGEX = TO_REGEX("award (.*) (\\d+)");
    private static final String REGEX_AWARD_ALL = TO_REGEX("awardAll (\\d+)");
    //endregion

    public PointSystem(Chatbot chatbot) {
        super(chatbot);
        initialize();
    }

    @Override
    public boolean process(Message message) {
        user = db.getUser(message);

        if (user == EMPTY_USER) {
            System.out.println("invalid user!");
            return false;
        }
        updateMatch(message);
        analyzeMessage(message);
        updateDuels();

        //region Duel handle
        Duel duel;
        if ((duel = getDuel(user)) != null) {
            if (isOr(DUEL_REFUSE_REGEX, DUEL_REFUSE_SIMPLE)) {
                chatbot.sendMessage(user.getName() + " odrzucił wyzwanie " + duel.getInitiator().getName());
                getActiveDuels().remove(duel);
                return true;
            } else if (isOr(DUEL_ACCEPT_REGEX, DUEL_ACCEPT_SIMPLE)) {
                System.out.println("handling duel...");
                return handleDuel(duel);
            }
        }

        if (is(DUEL_ANY_ACCEPT_REGEX)) {
            if (Duel.PUBLIC_DUEL.getInitiator() != EMPTY_USER && !Duel.PUBLIC_DUEL.getInitiator().getName().equals(user.getName())) {
                if (user.getPoints() < Duel.PUBLIC_DUEL.getBet()) {
                    String msg = "Nie masz tylu punktów! " + format(user.getPoints()) + "\nCzekam jeszcze "
                            + TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - Duel.PUBLIC_DUEL.getTimeStarted())
                            + "s!\nWpisz !go aby zawalczyć o " + Duel.PUBLIC_DUEL.getBet() * 2 + "pkt!";

                    chatbot.sendMessage(msg);
                    return false;
                }
                System.out.println("handling any duel...");
                Duel.PUBLIC_DUEL.setOpponent(user);
                return handleDuel(Duel.PUBLIC_DUEL);
            }
        }
        //endregion

        //region Giveaway handle

        if (is(REGEX_GIVEAWAY_CLAIM)) {
            System.out.println("claimer: " + user);
            System.out.println("giver  : " + PUBLIC_GIVEAWAY.getGiver());
            if (PUBLIC_GIVEAWAY == Giveaway.NO_GIVEAWAY) {
                System.out.println("no giveaway");
                return false;
            } else if (user.getName().equals(PUBLIC_GIVEAWAY.getGiver().getName())) {
                return false;
            }

            System.out.println("claimed!");
            pushPoints(PUBLIC_GIVEAWAY.getPoints());
            // TODO zwracanie punktow jesli nikt nie odbierze po (60?)s
            chatbot.sendMessage("Otrzymałeś " + PUBLIC_GIVEAWAY.getPoints() + "pkt. za friko! " + PointUtils.format(user.getPoints()));
            Giveaway.reset();
            return true;
        }

        //endregion


        // stat check, we don't add points here
        if (is(REGEX_STATS, REGEX_POINTS, REGEX_PTS)) {
            String msg = user.getName()
                    + "\nPunkty: " + user.getPoints()
                    + "\nWiadomości: " + user.getMessageCount();
            chatbot.sendMessage(msg);
            return true;

        } else if (is(REGEX_ME)) {
            String msg = "Twoje punkty: " + user.getPoints();
            chatbot.sendMessage(msg);
            return true;

        } else if (is(GIVEAWAY_REGEX)) {
            Matcher matcher = Pattern.compile(GIVEAWAY_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                int points = Integer.parseInt(matcher.group(1));

                if (points > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów!");
                    return false;
                } else if (user.getPoints() == 0) {
                    chatbot.sendMessage("Nie masz punktów.");
                    return false;
                } else if (points <= 0) {
                    return false;
                }

                String msg = user.getName() + " rozdaje " + Emoji.RIGHT_ARROW + " " + points + "pkt!\nWpisz !claim aby je zgarnąć!";
                System.out.println("giveaway starter before: " + user);
                pullPoints(points);
                Giveaway.newGiveaway(user, points);
                System.out.println("giveaway starter after : " + Giveaway.PUBLIC_GIVEAWAY.getGiver());
                chatbot.sendMessage(msg);
                return true;
            }
            return false;

        } else if (is(GIVE_REGEX)) {
            Matcher matcher = Pattern.compile(GIVE_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                String desiredUser = matcher.group(1);
                int points = Integer.parseInt(matcher.group(2));
                User receiver = db.findUser(desiredUser);

                if (receiver == EMPTY_USER) {
                    chatbot.sendMessage("Brak użytkownika w bazie danych.");
                    return false;
                } else if (receiver.getName().equals(user.getName())) {
                    return false;
                } else if (points > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów!");
                    return false;
                } else if (user.getPoints() == 0) {
                    chatbot.sendMessage("Nie masz punktów.");
                    return false;
                } else if (points <= 0) {
                    return false;
                } else {
                    user.subPoints(points);
                    receiver.addPoints(points);
                    db.update(user);
                    db.update(receiver);

                    String msg = user.getName() + " " + format(user.getPoints()) + " " + Emoji.RIGHT_ARROW + " " + points
                            + " " + Emoji.RIGHT_ARROW
                            + " " + receiver.getName() + " " + format(receiver.getPoints());

                    chatbot.sendMessage(msg);
                    return true;
                }
            }
            return false;
        } else if (is(REGEX_STATS_ANY, REGEX_POINTS_ANY, REGEX_PTS_ANY)) {
            Matcher matcher;

            if (is(REGEX_STATS_ANY)) {
                matcher = Pattern.compile(REGEX_STATS_ANY).matcher(message.getMessage());
            } else if (is(REGEX_POINTS_ANY)) {
                matcher = Pattern.compile(REGEX_POINTS_ANY).matcher(message.getMessage());
            } else if (is(REGEX_POINTS_ANY)) {
                matcher = Pattern.compile(REGEX_PTS_ANY).matcher(message.getMessage());
            } else {
                return false;
            }

            if (matcher.find()) {
                String desiredUser = matcher.group(1);
                User wantedUser = db.findUser(desiredUser);

                if (wantedUser == EMPTY_USER) {
                    chatbot.sendMessage("Użytkownik nie odnaleziony.");
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
            chatbot.sendMessage(getLadder());
            return true;

        } else if (is(LADDER_MSG_REGEX)) {
            String msg = Ladder.getMsgLadder(db.getUsers());
            chatbot.sendMessage(msg);
            return true;

        } else if (is(ROULETTE_REGEX)) {
            if (message.getMessage().length() > 23) { // todo check
                chatbot.sendMessage("Podałeś zbyt dużą liczbę!");
                return false;
            }
            Matcher matcher = Pattern.compile(ROULETTE_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                int desiredRoll = Integer.parseInt(matcher.group(1));

                if (desiredRoll <= 0) {
                    return false;
                } else if (user.getPoints() == 0) {
                    chatbot.sendMessage("Nie masz punktów.");
                    return false;
                } else if (desiredRoll > user.getPoints()) {
                    chatbot.sendMessage("Nie masz tylu punktów! (" + user.getPoints() + ")");
                    return false;
                }

                if (Utils.fiftyFifty()) {
                    user.addPoints(desiredRoll);
                    db.update(user);
                    chatbot.sendMessage("\uD83D\uDE4F Wygrałeś " + desiredRoll + "pkt! (" + user.getPoints() + ")");
                    return true;
                } else {
                    if (user.getPoints() - desiredRoll > 0) {
                        user.subPoints(desiredRoll);
                        db.update(user);
                        chatbot.sendMessage("\u2b07\ufe0f " + Util.GET_RANDOM(FAILED_ROULETTE_NORMAL) + desiredRoll + "pkt. (" + user.getPoints() + ")");
                        return true;
                    } else {
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
                return false;
            }

            if (Utils.fiftyFifty()) {
                user.addPoints(userPoints);
                db.update(user);
                chatbot.sendMessage("\uD83D\uDCAF Wygrałeś " + userPoints + " pkt! (" + user.getPoints() + ")");
                return true;
            } else {
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
                System.out.println("bet: " + bet + "\ninitiator: " + user.getName() + "\nopponent: " + desiredUser);

                if (user.getPoints() < bet) {
                    chatbot.sendMessage("\u274c Nie masz tylu punktów! (" + user.getPoints() + ")");
                    return false;
                }

                User opponent = db.findUser(desiredUser);
                if (opponent != EMPTY_USER) {
                    if (opponent.getPoints() < bet) {
                        chatbot.sendMessage("\u274c Przeciwnik nie ma tylu punktów. (" + opponent.getPoints() + ")");
                        return false;
                    } else if (opponent == user) {
                        chatbot.sendMessage("Nie możesz wyzwać siebie na pojedynek.");
                        return false;
                    } else if (bet <= 0) {
                        return false;
                    } else if (opponent.getName().equalsIgnoreCase(DBConnection.BOT.getName())) {
                        chatbot.sendMessage("Ja się nie bawie");
                        return false;
                    }
                } else {
                    chatbot.sendMessage("\u274c Użytkownik nie istnieje w bazie danych.");
                    return false;
                }
                getActiveDuels().add(new Duel(user, opponent, bet));

                String msg = user.getName() + " \u2694\ufe0f wyzywa \uD83D\uDEE1 "
                        + opponent.getName() + " na pojedynek o " + bet * 2
                        + " pkt!\nCzekam 60s. na odpowiedź przeciwnika. \n\u23f3 (y/n)";

                chatbot.sendMessage(msg);
                return true;
            }
            return false;

        } else if (is(REGEX_AWARD_ALL) && user.getName().equals(Users.Gabe.name())) {
            Matcher matcher = Pattern.compile(REGEX_AWARD_ALL).matcher(message.getMessage());

            if (matcher.find()) {
                System.out.println("matcher = " + matcher.group(1));
                int points = Integer.parseInt(matcher.group(1));
                pushPoints(db.getUsers(), points);
                chatbot.sendImageUrlWaitToLoad("https://i.imgur.com/7drHiqr.gif");
                return true;
            }
            return false;
        } else if (is(AWARD_REGEX) && user.getName().equals(Users.Gabe.name())) {
            Matcher matcher = Pattern.compile(AWARD_REGEX).matcher(message.getMessage());

            if (matcher.find()) {
                String desiredUser = matcher.group(1);
                int points = Integer.parseInt(matcher.group(2));

                User receiver = db.findUser(desiredUser);
                if (receiver != EMPTY_USER) {
                    receiver.addPoints(points);
                    db.update(receiver);
                    return true;
                }
                return false;
            }
            return false;
        } else if (is(DUEL_ANY_REGEX)) {
            Matcher matcher = Pattern.compile(DUEL_ANY_REGEX).matcher(message.getMessage());
            int bet;

            if (matcher.find()) {
                // todo length check
                bet = Integer.parseInt(matcher.group(1));

                if (bet <= 0) {
                    return false;
                } else if (user.getPoints() < bet) {
                    chatbot.sendMessage("\u274c Nie masz tylu punktów! " + format(user.getPoints()));
                    return false;
                }

                Duel.PUBLIC_DUEL.set(user, bet);
                chatbot.sendMessage(user.getName() + " \u2694\ufe0f chce się pojedynkować o " + bet * 2 + "pkt!\nWpisz !go aby przyjąć wyzwanie.");
                return true;
            }
            return false;
        }
        return false;
    }

    private void analyzeMessage(Message message) {
//        if (new Date().getTime() < getTimeoutRelease()) {
//            System.out.println("not time yet!");
//            return;
//        }

        if (isRegex()) {
            System.out.println("returning...");
            return;
        }

        String messageBody = message.getMessage();
        int msgLength = messageBody.length();
        db.refresh(user);

        if (message.getImage() != null) {
            System.out.println("msg with image:\n" + user.getName() + "\n +" + Points.POINT_SYSTEM_IMAGE);
            user.addPoints(Points.POINT_SYSTEM_IMAGE); // FIXME sometimes throws exception
            System.out.println(user.getName() + "(+" + Points.POINT_SYSTEM_IMAGE + ")(IMG)");
        }

        if (messageBody.contains("http") || messageBody.contains("www.") || messageBody.contains("//")) {
            user.addPoints(SYSTEM_URL);
        }

        if (msgLength <= 20 && msgLength >= 3) {
            user.addPoints(Points.MSG_LENGTH_20);
            System.out.println(user.getName() + "(+" + Points.MSG_LENGTH_20 + ")");

        } else if (msgLength <= 60 && msgLength > 20) {
            user.addPoints(Points.MSG_LENGTH_60);
            System.out.println(user.getName() + "(+" + Points.MSG_LENGTH_60 + ")");

        } else if (msgLength <= 100 && msgLength > 60) {
            user.addPoints(Points.MSG_LENGTH_100);
            System.out.println(user.getName() + "(+" + Points.MSG_LENGTH_100 + ")");

        } else if (msgLength > 100 && msgLength < 300) {
            user.addPoints(Points.MSG_LENGTH_300);
            System.out.println(user.getName() + "(+" + Points.MSG_LENGTH_300 + ")");
        }

        setTimeoutRelease(getTimeoutRelease());
        pushMessageCount();
    }

    private boolean handleDuel(Duel duel) {
        db.refresh(duel.getInitiator(), duel.getOpponent());

        if (!duel.resolve()) {
            chatbot.sendMessage("\u274c Pojedynek anulowany.");
            return false;
        }

        int betPoints = duel.getBet() * 2;

        // transfer points to winner (if loser has enough bet * 2 points, else transfers all points to winner)
        if (betPoints <= duel.getLoser().getPoints() * 2) {
            Utils.transfer(duel.getLoser(), duel.getWinner(), betPoints);
        } else {
            betPoints = duel.getLoser().getPoints();
            Utils.transfer(duel.getLoser(), duel.getWinner(), betPoints);
        }
        db.update(duel.getWinner(), duel.getLoser());

        if (duel.getInitiator() == duel.getWinner()) {
            chatbot.sendMessage(duel.getInitiator().getName() + " \uD83C\uDD9A " + duel.getOpponent().getName()
                    + "\n"
                    + duel.getWinner().getName() + " wygrywa pojedynek i zdobywa " + betPoints + " pkt! (" + duel.getWinner().getPoints() + ")"
                    + "\n"
                    + duel.getLoser().getName() + " traci " + betPoints + " pkt. (" + duel.getLoser().getPoints() + ")");

            if (duel != Duel.PUBLIC_DUEL) {
                getActiveDuels().remove(duel);
                return true;
            } else {
                Duel.reset();
                return true;
            }
        } else if (duel.getInitiator() == duel.getLoser()) {
            chatbot.sendMessage(duel.getInitiator().getName() + " \uD83C\uDD9A " + duel.getOpponent().getName()
                    + "\n"
                    + duel.getLoser().getName() + " przegrywa pojedynek i traci " + betPoints + " pkt (" + duel.getWinner().getPoints() + ")"
                    + "\n"
                    + duel.getLoser().getName() + " wygrywa " + betPoints + " pkt! (" + duel.getLoser().getPoints() + ")");

            if (duel != Duel.PUBLIC_DUEL) {
                getActiveDuels().remove(duel);
                return true;
            } else {
                Duel.reset();
                return true;
            }
        } else {
            System.out.println("Unexpected error.");
            return false;
        }
    }

    private void initialize() {
        setTimeoutRelease(getTimeoutRelease());
        setActiveDuels(new ArrayList<>());
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(
                REGEX_ME,
                REGEX_STATS,
                REGEX_POINTS,
                REGEX_PTS,
                REGEX_STATS_ANY,
                REGEX_POINTS_ANY,
                REGEX_PTS_ANY,
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
                DUEL_ANY_REGEX,
                DUEL_ANY_ACCEPT_REGEX,
                BET_MORE_THAN_REGEX,
                BET_LESS_THAN_REGEX,
                GIVE_REGEX,
                GIVEAWAY_REGEX,
                REGEX_GIVEAWAY_CLAIM,
                AWARD_REGEX,
                REGEX_AWARD_ALL);
    }

    private String getLadder() {
        System.out.println("Generating ladder...");
        Ladder ladder = Ladder.getLadder(PointUtils.usersFromDatabase());
        System.out.println("Ladder generated");
        return ladder.toString();
    }

    /**
     * Returns duel instance based on the argument, null if no duel is found with given user.
     */
    private Duel getDuel(User user) {
        for (Duel duel : getActiveDuels()) {
            if (duel.getOpponent() == user) {
                return duel;
            }
        }
        return null;
    }

    private void updateDuels() {
        long now = new Date().getTime();

        if (Duel.PUBLIC_DUEL.getInitiator() != EMPTY_USER) {
            if (now - Duel.PUBLIC_DUEL.getTimeStarted() > 60000) {
                Duel.reset();
            }
        }

        if (!getActiveDuels().isEmpty()) {
            Iterator<Duel> i = getActiveDuels().iterator();
            Duel temp;

            // TODO test
            while (i.hasNext()) {
                temp = i.next();

                System.out.println("temp.getTimeStarted() = " + temp.getTimeStarted());
                if (now - temp.getTimeStarted() > 60000) {
                    System.out.println("removed expired duel!");
                    i.remove();
                }
            }
        }
    }

    private long getTimeoutRelease() {
        return new Date().getTime() + TIMEOUT;
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

    public ArrayList<Duel> getActiveDuels() {
        return activeDuels;
    }

    public void setActiveDuels(ArrayList<Duel> activeDuels) {
        this.activeDuels = activeDuels;
    }

    public void setTimeoutRelease(long timeoutRelease) {
        this.timeoutRelease = timeoutRelease;
    }
}
