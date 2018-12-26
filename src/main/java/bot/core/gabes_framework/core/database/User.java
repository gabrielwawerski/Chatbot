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
//    @DatabaseField
//    private int randomUsed;
//    @DatabaseField
//    private int kwejkUsed;
//    @DatabaseField
//    private int lezeHate;

    public User() {
    }

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

    public void subPoints(int value) {
        if (value < points) {
            points = 0;
        } else {
            this.points -= value;
        }
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void addMessagecount(int value) {
        messageCount += value;
    }
}
