package bot.modules.gabe.point_system;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.gabes_framework.core.database.Users;
import bot.modules.gabe.point_system.submodule.PointUtils;
import bot.modules.gabe.point_system.util.Points;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.modules.gabe.point_system.util.Duel;
import bot.modules.gabe.point_system.util.Ladder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.database.User.INVALID_USER;
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
public class PointSystem extends ModuleBase {
    private ArrayList<Duel> activeDuels;
    private long now;
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
    private static final String REGEX_ME = Utils.TO_REGEX("me");
    private static final String REGEX_STATS = Utils.TO_REGEX("stats");
    private static final String REGEX_POINTS = Utils.TO_REGEX("points");
    private static final String REGEX_PTS = Utils.TO_REGEX("pts");
    private static final String REGEX_STATS_ANY = Utils.TO_REGEX("stats (.*)");
    private static final String REGEX_POINTS_ANY = Utils.TO_REGEX("points (.*)");
    private static final String REGEX_PTS_ANY = Utils.TO_REGEX("pts (.*)");
    // PTS AND MSGS LADDER
    private static final String LADDER_REGEX = Utils.TO_REGEX("ladder");
    private static final String LADDER_MSG_REGEX = Utils.TO_REGEX("ermekynewiy4"); // msg
    // ROULETTE
    private static final String ROULETTE_REGEX = Utils.TO_REGEX("roulette (\\d+)");
    private static final String ROULETTE_ALL_REGEX = Utils.TO_REGEX("roulette all");
    private static final String VABANK_REGEX = Utils.TO_REGEX("vabank");
    // DUEL
    private static final String REGEX_DUEL = Utils.TO_REGEX("duel (.*) (\\d+)");
    private static final String DUEL_ACCEPT_REGEX = Utils.TO_REGEX("y");
    private static final String DUEL_REFUSE_REGEX = Utils.TO_REGEX("n");
    private static final String DUEL_ACCEPT_SIMPLE = "y";
    private static final String DUEL_REFUSE_SIMPLE = "n";
    private static final String DUEL_ANY_REGEX = Utils.TO_REGEX("duel (\\d+)");
    // TODO BET
    private static final String BET_MORE_THAN_REGEX = Utils.TO_REGEX("bet (.*) >(\\d+)");
    private static final String BET_LESS_THAN_REGEX = Utils.TO_REGEX("bet (.*) <(\\d+)");
    // TRANSFER
    private static final String GIVE_REGEX = Utils.TO_REGEX("give (.*) (\\d+)");
    private static final String AWARD_REGEX = Utils.TO_REGEX("award (.*) (\\d+)");
    private static final String REGEX_AWARD_ALL = Utils.TO_REGEX("awardAll (\\d+)");
    //endregion

    public PointSystem(Chatbot chatbot) {
        super(chatbot);
        initialize();
    }

    @Override
    public boolean process(Message message) {
        user = db.getUser(message);

        if (user == INVALID_USER) {
            System.out.println("invalid user!");
            return false;
        }

        updateMatch(message);
        analyzeMessage(message);

        updateMatch(message);
        // handle duel
        updateDuels();
        Duel duel;
        if ((duel = getDuel(user)) != null) {
            // duel refused
            if (isOr(DUEL_REFUSE_REGEX, DUEL_REFUSE_SIMPLE)) {
                chatbot.sendMessage(user.getName() + " odrzucił wyzwanie " + duel.getInitiator().getName());
                activeDuels.remove(duel);
                return true;
            } else (isOr(DUEL_ACCEPT_REGEX, DUEL_ACCEPT_SIMPLE)) {
                return handleDuel(duel);
            }
        }

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

        } else if (is(GIVE_REGEX)) {
            Matcher matcher = Pattern.compile(GIVE_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                String desiredUser = matcher.group(1);
                int points = Integer.parseInt(matcher.group(2));
                User receiver = db.findUser(desiredUser);

                if (receiver == INVALID_USER) {
                    chatbot.sendMessage("Brak użytkownika w bazie danych.");
                    return false;
                } else if (receiver.getName().equals(user.getName()))  {
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

                    String msg = user.getName() + format(user.getPoints()) + " " + points
                            + "pkt. " + Emoji.RIGHT_ARROW
                            + " " + receiver.getName() + format(receiver.getPoints());

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
            } else {
                matcher = Pattern.compile(REGEX_PTS_ANY).matcher(message.getMessage());
            }

            if (matcher.find()) {
                String desiredUser = matcher.group(1);
                User wantedUser = db.findUser(desiredUser);

                if (wantedUser == INVALID_USER) {
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
            String msg = Ladder.getMsgLadder(PointUtils.usersFromDatabase()).toString();
            chatbot.sendMessage(msg);
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
                    user.addPoints(desiredRoll);
                    db.update(user);
                    chatbot.sendMessage("\uD83D\uDE4F Wygrałeś " + desiredRoll + " pkt! (" + user.getPoints() + ")");
                    return true;
                } else {
                    if (user.getPoints() - desiredRoll > 0) {
                        user.subPoints(desiredRoll);
                        db.update(user);
                        chatbot.sendMessage("\u2b07\ufe0f " + Util.GET_RANDOM(FAILED_ROULETTE_NORMAL) + desiredRoll + " pkt. (" + user.getPoints() + ")");
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
                chatbot.sendMessage("Nie masz punktów.");
                return true;
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

        else if (is(REGEX_DUEL)) {
            Matcher matcher = Pattern.compile(REGEX_DUEL).matcher(message.getMessage());

            if (matcher.find()) {
            User user = db.getUser(message);
                String desiredUser = matcher.group(1);
                int bet = Integer.parseInt(matcher.group(2));
                System.out.println("bet: " + bet + "\ninitiator: " + user.getName() + "\nopponent: " + desiredUser);
                if (user.getPoints() < bet) {
                    chatbot.sendMessage("\u274c Nie masz tylu punktów! (" + user.getPoints() + ")");
                    return false;
                }

                User opponent = db.findUser(desiredUser);
                if (opponent != INVALID_USER) {
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
            System.out.println("before");
            return false;
        } else if (is(REGEX_AWARD_ALL) && user.getName().equals(Users.Gabe.name())) {
            Matcher matcher = Pattern.compile(REGEX_AWARD_ALL).matcher(message.getMessage());
            System.out.println("here");

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
                if (receiver != INVALID_USER) {
                    receiver.addPoints(points);
                    db.update(receiver);
                    return true;
                }
                return false;
            }
            return false;
        }
        else if (is(DUEL_ANY_REGEX)) {
      //      Matcher matcher = Pattern.compile(DUEL_ANY_REGEX).matcher(message.getMessage());
        //    System.out.println("duel any!!!");
          //  return false;
        }

        else if (is(BET_MORE_THAN_REGEX)) {
            Matcher matcher = Pattern.compile(BET_MORE_THAN_REGEX).matcher(message.getMessage());
            System.out.println("betmore!");

            if (matcher.find()) {
                User opponent = db.findUser(matcher.group(1));
                int bet = Integer.parseInt(matcher.group(2));

                if (opponent == INVALID_USER) {
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

    private void analyzeMessage(Message message) {
        if (new Date().getTime() < timeoutRelease) {
            System.out.println("not time yet!");
            return;
        }

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

        timeoutRelease = getTimeoutRelease();
        user.addMessageCount();
        pushMessageCount();
        db.update(user);
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

            activeDuels.remove(duel);
            return true;

        } else if (duel.getInitiator() == duel.getLoser()) {
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

    private void initialize() {
        now = new Date().getTime();
        timeoutRelease = getTimeoutRelease();
        activeDuels = new ArrayList<>();
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
                REGEX_DUEL,
                DUEL_ACCEPT_REGEX,
                DUEL_ACCEPT_SIMPLE,
                DUEL_REFUSE_REGEX,
                DUEL_REFUSE_SIMPLE,
                BET_MORE_THAN_REGEX,
                BET_LESS_THAN_REGEX,
                GIVE_REGEX,
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
        for (Duel duel : activeDuels) {
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

                System.out.println("temp.getTimeStarted() = " + temp.getTimeStarted());
                if (now > temp.getTimeStarted()) {
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
}
