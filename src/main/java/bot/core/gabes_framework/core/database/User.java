package bot.core.gabes_framework.core.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.naming.OperationNotSupportedException;

/**
 * @since v0.3201
 */
@DatabaseTable(tableName = "User")
public class User {
    @DatabaseField(id = true, unique = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField(defaultValue = "0")
    private int points;
    @DatabaseField
    private int messageCount;

    // TODO add these to database:
//    "\uD83D\uDE0D"
    // totalLoveEmoji

//    "\uD83D\uDE06"
    // totalLaughEmoji

//    "\uD83D\uDE2E "
    // totalSurpriseEmote

//    "\uD83D\uDE22"
    // totalCryEmote

//    "\uD83D\uDE20"
    // totalAngryEmote

//    "\uD83D\uDC4D"
    // totalThumbsUpEmoji

//    "\uD83D\uDC4E"
    // totalThumbsDownEmote

    public static final User INVALID_USER = new User("INVALID_USER");
    public static final String INVALID_NAME = "INVALID_USER";

    public User() { }

    public User(String name) {
        this.name = name;
    }

    public User(int id, String name, int points, int messageCount) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.messageCount = messageCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int value) {
        this.points += value;
    }

    /**
     * If {@code points} value is less or equal than zero, sets points to 0.
     */
    public void subPoints(int value) {
        if (points - value >= 0) {
            this.points -= value;
        } else {
            this.points = 0;
        }
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void addMessagecount(int value) {
        messageCount += value;
    }

    public void addMessageCount() {
        messageCount++;
    }
}
