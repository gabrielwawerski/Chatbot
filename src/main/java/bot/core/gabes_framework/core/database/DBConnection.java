package bot.core.gabes_framework.core.database;

import bot.core.hollandjake_api.helper.misc.Message;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private static ConnectionSource connectionSource;
    private static Dao<User, String> userDao;
    private static Dao<MessageLog, String> messageDao;

    private static DBConnection instance;

    private static final String URL = "jdbc:hsqldb:file:E:/IntelliJProjekty/libGDX/Chatbot/PcionDB/PcionDatabase";

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    private DBConnection() {
        try {
            connectionSource = new JdbcConnectionSource(URL);
            ((JdbcConnectionSource) connectionSource).setUsername("gabe");
            ((JdbcConnectionSource) connectionSource).setPassword("lezetykurwo");

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

    public void update(User user) {
        try {
            userDao.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        for (User user : userDao) {
            users.add(user);
        }
        return users;
    }

    public void refreshAll(ArrayList<User> users) {
        for (User user : users) {
            try {
                userDao.refresh(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAll(List<User> users) {
        for (User user : users) {
            try {
                userDao.update(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createUser(User user) {
        try {
            userDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(User user, Message message) {
    }
}
