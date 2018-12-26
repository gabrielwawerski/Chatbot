package bot.core.gabes_framework.core.database;

import bot.core.hollandjake_api.helper.misc.Message;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private ConnectionSource connectionSource;
    Dao<User, String> userDao;
    Dao<MessageLog, String> messageDao;

    private static final String URL = "jdbc:hsqldb:file:E:/IntelliJProjekty/libGDX/Chatbot/PcionDB/PcionDatabase";

    public Database() {
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

    public ArrayList<User> initialize() {
        ArrayList<User> returnValue = new ArrayList<>();
        for (User user : userDao) {
            returnValue.add(new User(user.getId(), user.getName(), user.getPoints(), user.getMessageCount()));
        }
        return returnValue;
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
