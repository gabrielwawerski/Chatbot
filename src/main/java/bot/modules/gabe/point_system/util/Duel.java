package bot.modules.gabe.point_system.util;

import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;

import java.util.Date;

public class Duel {
    private final DBConnection db = DBConnection.getInstance();

    private User initiator;
    private User opponent;
    private int bet;

    private User winner;
    private User loser;

    private long timeStarted;

    public static Duel PUBLIC_DUEL = new Duel();
    public static long DUEL_TIMEOUT = 0;
    private static long TIME_STARTED = 0;

    public Duel(User initiator, User opponent, int bet) {
        this.initiator = initiator;
        this.opponent = opponent;
        this.bet = bet;
        timeStarted = new Date().getTime();
    }

    public Duel(User initiator, int bet) {
        this.initiator = initiator;
        this.bet = bet;
    }

    private Duel() {
        initiator = User.EMPTY_USER;
        opponent = null;
        bet = 0;
        winner = null;
        loser = null;
        timeStarted = new Date().getTime();
    }

    public static void setTimeout(long timeout) {
        DUEL_TIMEOUT = timeout;
    }

    public static void reset() {
        DUEL_TIMEOUT = 0;
        TIME_STARTED = 0;
        PUBLIC_DUEL =  new Duel();
    }

    /**
     * Refreshes initiator and opponent, checks if they still have required points for duel,
     */
    public boolean resolve() {
        if (!(initiator.getPoints() > bet) && !(opponent.getPoints() > bet)) {
            return false;
        }

        if (Utils.fiftyFifty()) {
            winner = initiator;
            loser = opponent;
            return true;
        } else {
            winner = opponent;
            loser = initiator;
            return true;
        }
    }

    public void set(User initiator, int bet) {
        TIME_STARTED = new Date().getTime();
        DUEL_TIMEOUT = TIME_STARTED + 60000;
        this.initiator = initiator;
        this.bet = bet;

    }

    public User getInitiator() {
        return initiator;
    }

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public User getWinner() {
        return winner;
    }

    public User getLoser() {
        return loser;
    }

    public int getBet() {
        return bet;
    }

    public long getTimeStarted() {
        return timeStarted;
    }
}
