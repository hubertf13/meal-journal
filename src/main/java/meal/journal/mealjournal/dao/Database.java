package meal.journal.mealjournal.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private final static String DATABASE_LOCATION = "C:\\Users\\huber\\IdeaProjects\\EDP-PROJECT\\src\\main\\resources\\pl\\filipczuk\\edpproject\\database\\database";
    private static final String requiredTable = "meal";

    public static boolean isOK() {
        if (!checkDrivers())
            return false; //driver errors

        if (!checkConnection())
            return false; //can't connect to db

        return checkTables(); //tables didn't exist
    }

    private static boolean checkTables() {
        String checkTables =
                "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + requiredTable + "'";

        try (Connection connection = Database.connect()) {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(checkTables);
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    if (rs.getString("tbl_name").equals(requiredTable))
                        return true;
                }
            }
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not find tables in database");
            return false;
        }
        return false;
    }

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not connect to database");
            return false;
        }
    }

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }

    protected static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;

        try {
            connection = DriverManager.getConnection(dbPrefix + DATABASE_LOCATION);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE, LocalDateTime.now() + ": Could not connect to SQLite DB at " + DATABASE_LOCATION);
            return null;
        }
        return connection;
    }
}
