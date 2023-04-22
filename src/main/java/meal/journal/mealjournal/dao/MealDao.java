package meal.journal.mealjournal.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MealDao {
    private static final Log log = LogFactory.getLog(MealDao.class);
    private static final String tableName = "meal";
    private static final String idColumn = "id";
    private static final String nameColumn = "name";
    private static final String dateColumn = "date";

    private static final ObservableList<Meal> meals;

    static {
        meals = FXCollections.observableArrayList();
        updateMealsFromDB();
    }

    public static ObservableList<Meal> getMeals() {
        return FXCollections.unmodifiableObservableList(meals);
    }

    private static void updateMealsFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery();
                meals.clear();

                while (rs.next()) {
                    int mealId = rs.getInt(idColumn);
                    List<Ingredient> mealIngredients = getMealIngredients(mealId);

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    LocalDate date = df.parse(rs.getString("date"))
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    meals.add(new Meal(mealId, rs.getString(nameColumn), date, mealIngredients));
                }
                log.info("Update meals from database completed");
            } else {
                throw new SQLException("Connection in is null");
            }
        } catch (SQLException | ParseException e) {
            log.error("Could not load Meal from database: " + e.getMessage());
            meals.clear();
        }
    }

    private static List<Ingredient> getMealIngredients(int mealId) {
        ObservableList<Ingredient> allIngredients = IngredientDao.getIngredients();
        return allIngredients.stream()
                .filter(ingredient -> ingredient.getMealId() == mealId)
                .toList();
    }

    public static Meal insertMeal(String name, LocalDate date) {
        int id = (int) CRUDHelper.create(
                tableName,
                new String[]{nameColumn, dateColumn},
                new Object[]{name, date},
                new int[]{Types.VARCHAR, Types.NUMERIC});

        Meal meal = new Meal(id, name, date, new ArrayList<>());
        meals.add(meal);

        return meal;
    }

    public static Optional<Meal> getMeal(String mealName, LocalDate date) {
        List<Meal> allMeals = MealDao.getMeals().stream().toList();
        return allMeals.stream()
                .filter(meal -> (meal.getDate().isEqual(date) && meal.getName().equals(mealName)))
                .findFirst();
    }
}
