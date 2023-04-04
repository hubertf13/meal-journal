package meal.journal.mealjournal.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MealDao {
    private static final String tableName = "meal";
    private static final String idColumn = "id";
    private static final String nameColumn = "name";

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

                    meals.add(new Meal(mealId, rs.getString(nameColumn), mealIngredients));
                }
                System.out.println(meals);
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Meal from database ");
            meals.clear();
        }
    }

    private static List<Ingredient> getMealIngredients(int mealId) {
        ObservableList<Ingredient> allIngredients = IngredientDao.getIngredients();
        return allIngredients.stream()
                .filter(ingredient -> ingredient.getMealId() == mealId)
                .toList();
    }

    public static void update(Meal newMeal) {
        //udpate database
        long rows = CRUDHelper.update(
                tableName,
                new String[]{nameColumn},
                new Object[]{newMeal.getName()},
                new int[]{Types.VARCHAR},
                idColumn,
                Types.INTEGER,
                newMeal.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Meal to be updated with id " + newMeal.getId() + " didn't exist in database");

        //update cache
        Optional<Meal> optionalMeal = getMeal(newMeal.getId());
        optionalMeal.ifPresentOrElse((oldMeal) -> {
            meals.remove(oldMeal);
            meals.add(newMeal);
        }, () -> {
            throw new IllegalStateException("Meal to be updated with id " + newMeal.getId() + " didn't exist in database");
        });
    }

    public static void insertMeal(String name) {
        //update database
        int id = (int) CRUDHelper.create(
                tableName,
                new String[]{nameColumn},
                new Object[]{name},
                new int[]{Types.VARCHAR});

        //update cache
        meals.add(new Meal(id, name, new ArrayList<>()));
    }

    public static void delete(int id) {
        //update database
        CRUDHelper.delete(tableName, id);

        //update cache
        Optional<Meal> meal = getMeal(id);
        meal.ifPresent(meals::remove);

    }

    public static Optional<Meal> getMeal(int id) {
        for (Meal meal : meals) {
            if (meal.getId() == id) return Optional.of(meal);
        }
        return Optional.empty();
    }
}
