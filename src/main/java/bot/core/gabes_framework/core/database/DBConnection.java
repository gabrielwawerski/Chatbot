package bot.core.gabes_framework.core.database;

import bot.core.PcionBot;
import bot.core.hollandjake_api.helper.misc.Message;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;

public class DBConnection {
    private static ConnectionSource connectionSource;
    private static Dao<User, String> userDao;
    private static Dao<MessageLog, String> messageDao;

    private static DBConnection instance;

    public static User BOT;

    private static final String URL = "jdbc:hsqldb:file:E:/IntelliJProjekty/libGDX/Chatbot/PcionDB/PcionDatabase";

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
            BOT = getBot();
        }
        return instance;
    }

    private DBConnection() {
        try {
            connectionSource = new JdbcConnectionSource(URL);
            ((JdbcConnectionSource) connectionSource).setUsername(PcionBot.DATABASE_USERNAME);
            ((JdbcConnectionSource) connectionSource).setPassword(PcionBot.DATABASE_PASSWORD);

            userDao = DaoManager.createDao(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        connectionSource.closeQuietly();
    }

    public void refresh(User user) {
        try {
            userDao.refresh(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refresh(User... users) {
        for (User user : users) {
            try {
                userDao.refresh(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(User user) {
        try {
            userDao.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User... users) {
        for (User user : users) {
            try {
                userDao.update(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static User getBot() {
        for (User currentUser : userDao) {
            if (currentUser == DBConnection.BOT) {
                return currentUser;
            }
        }
        return null;
    }

    public User getUser(Message message) {
        for (User user : userDao) {
            if (message.getSender().getName().equals(user.getName())) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        for (User user : userDao) {
            users.add(user);
        }
        return users;
    }

    public void createUser(User user) {
        try {
            userDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
