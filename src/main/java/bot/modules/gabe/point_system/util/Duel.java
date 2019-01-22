package bot.modules.gabe.point_system.util;

import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;

import java.util.Date;

public class Duel {
    private final User initiator;
    private final User opponent;
    private final int bet;

    private User winner;
    private User loser;

    private long timeStarted;

    private final DBConnection db = DBConnection.getInstance();

    public Duel(User initiator, User opponent, int bet) {
        this.initiator = initiator;
        this.opponent = opponent;
        this.bet = bet;
        timeStarted = new Date().getTime();
    }

    /**
     * Refreshes initiator and opponent, checks if they still have required points for duel,
     */
    public boolean resolve() {
        db.refresh(initiator, opponent);

        if (initiator.getPoints() < bet || opponent.getPoints() < bet) {
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

    public User getWinner() {
        return winner;
    }

    public User getLoser() {
        return loser;
    }

    public User getInitiator() {
        return initiator;
    }

    public User getOpponent() {
        return opponent;
    }

    public int getBet() {
        return bet;
    }

    public long getTimeStarted() {
        return timeStarted;
    }
}
