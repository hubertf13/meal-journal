package meal.journal.mealjournal.dao;

import meal.journal.mealjournal.MealsApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;

public class Database {
    private static Log log = LogFactory.getLog(Database.class);
    private static String DATABASE_LOCATION;
    private static final String requiredTable = "meal";

    public static boolean isOK() {
        DATABASE_LOCATION = MealsApplication.getDatabaseLocation();
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
            } else {
                throw new SQLException("Connection is null");
            }
        } catch (SQLException e) {
            log.error("Could not find tables in database: " + e.getMessage());
            return false;
        }

        String createTableMealStmt = "CREATE TABLE IF NOT EXISTS meal\n" +
                "(\n" +
                "    id   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name VARCHAR(255) NOT NULL,\n" +
                "    date NUMERIC NOT NULL\n" +
                ");";
        String createTableIngredientStmt = "CREATE TABLE IF NOT EXISTS ingredient\n" +
                "(\n" +
                "    id           INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name         VARCHAR(255) NOT NULL,\n" +
                "    calories     NUMERIC      NOT NULL,\n" +
                "    fat          NUMERIC      NOT NULL,\n" +
                "    carbohydrate NUMERIC      NOT NULL,\n" +
                "    protein      NUMERIC      NOT NULL,\n" +
                "    amount       VARCHAR(255) NOT NULL,\n" +
                "    meal_id      INTEGER      NOT NULL,\n" +
                "    FOREIGN KEY (meal_id) REFERENCES meal (id)\n" +
                ");";

        try (Connection connection = Database.connect()) {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                // create a new table
                stmt.execute(createTableMealStmt);
                stmt.execute(createTableIngredientStmt);

                return true;
            } else {
                throw new SQLException("Connection is null");
            }
        } catch (SQLException e) {
            log.error("Could not create tables in database: " + e.getMessage());
            return false;
        }
    }

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            log.error("Could not connect to database: " + e.getMessage());
            return false;
        }
    }

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Could not start SQLite Drivers: " + e.getMessage());
            return false;
        }
    }

    protected static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;

        try {
            connection = DriverManager.getConnection(dbPrefix + DATABASE_LOCATION);
        } catch (SQLException exception) {
            log.error("Could not connect to SQLite DB at " + DATABASE_LOCATION);
            return null;
        }
        return connection;
    }
}
