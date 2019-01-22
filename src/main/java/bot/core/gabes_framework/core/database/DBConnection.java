package bot.core.gabes_framework.core.database;

import bot.core.PcionBot;
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
    public static User BOT;

    private static DBConnection instance;

    private static final String URL = "jdbc:hsqldb:file:E:/IntelliJProjekty/libGDX/Chatbot/PcionDB/PcionDatabase";

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
            BOT = getBot();
        }
        return instance;
    }

    private DBConnection() {
        connect();
    }

    private void connect() {
        try {
            connectionSource = new JdbcConnectionSource(URL);
            ((JdbcConnectionSource) connectionSource).setUsername(PcionBot.DATABASE_USERNAME);
            ((JdbcConnectionSource) connectionSource).setPassword(PcionBot.DATABASE_PASSWORD);
            userDao = DaoManager.createDao(connectionSource, User.class);
            refreshAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        connectionSource.closeQuietly();
    }

    // TODO check if it's working properly
    public void refreshAll() {
        for (User user : userDao) {
            try {
                userDao.refresh(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

    public void refresh(List<User> users) {
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

    public void update(List<User> users) {
        for (User user : users) {
            try {
                userDao.update(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            if (currentUser.getName().equalsIgnoreCase(Users.Bot.name())) {
                return currentUser;
            }
        }
        return User.INVALID_USER;
    }

    public User getUser(Message message) {
        if (message.getSender() != null) {
            for (User user : userDao) {
                if (message.getSender().getName().equalsIgnoreCase(user.getName())) {
                    return user;
                }
            }
        }
        return User.INVALID_USER;
    }


    public User findUser(String name) {
        String name_ = name;
        if (name.charAt(0) == '@') {
            name_ = name.substring(1);
        }

        for (User user : userDao) {
            if (user.getName().equals(Users.Gabe.name()) && name_.equalsIgnoreCase("gabe")) { // TODO Users -> nowa klasa (Presenter costam?) majaca tylko name i realWorldNickname
                return user;
            } else if (user.getName().equals(Users.Leze.name()) && (name_.equalsIgnoreCase("leze")
                    || name_.equalsIgnoreCase("łeze") || name_.equalsIgnoreCase("śmieć")
                    || name_.equalsIgnoreCase("smiec"))) {
                return user;
            } else if (user.getName().equals(Users.Hube.name()) && name_.equalsIgnoreCase("hube")) {
                return user;
            } else if (user.getName().equals(Users.Mege.name()) && name_.equalsIgnoreCase("mege")) {
                return user;
            } else if (user.getName().equals(Users.Leze.name()) && name_.equalsIgnoreCase("leze")) {
                return user;
            } else if (user.getName().equals(Users.Wiesio.name()) && name_.equalsIgnoreCase("wiech")) {
                return user;
            } else if (user.getName().equals(Users.Kaspe.name()) && name_.equalsIgnoreCase("kaspe")) {
                return user;
            } else if ((name_.length() <= 30) && (name_.length() >= 3)
                    && user.getName().toLowerCase().contains(name_.toLowerCase())) {
                System.out.println("found: " + user.getName());
                return user;
            }
        }
        return User.INVALID_USER;
    }

    public ArrayList<User> getUsers() {
        refreshAll();
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
