package meal.journal.mealjournal.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import meal.journal.mealjournal.model.Ingredient;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IngredientDao {
    private static final String tableName = "ingredient";
    private static final String idColumn = "id";
    private static final String nameColumn = "name";
    private static final String caloriesColumn = "calories";
    private static final String fatColumn = "fat";
    private static final String carbohydrateColumn = "carbohydrate";
    private static final String proteinColumn = "protein";
    private static final String amountGColumn = "amount_g";
    private static final String mealId = "meal_id";

    private static final ObservableList<Ingredient> ingredients;

    static {
        ingredients = FXCollections.observableArrayList();
        updateIngredientsFromDB();
    }

    public static ObservableList<Ingredient> getIngredients() {
        return FXCollections.unmodifiableObservableList(ingredients);
    }

    private static void updateIngredientsFromDB() {

        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            ingredients.clear();
            while (rs.next()) {
                ingredients.add(new Ingredient(
                        rs.getInt(idColumn),
                        rs.getString(nameColumn),
                        rs.getString(caloriesColumn),
                        rs.getString(fatColumn),
                        rs.getString(carbohydrateColumn),
                        rs.getString(proteinColumn),
                        rs.getString(amountGColumn),
                        rs.getInt(mealId)));
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(
                    Level.SEVERE,
                    LocalDateTime.now() + ": Could not load Ingredient from database ");
            ingredients.clear();
        }
    }

    public static void update(Ingredient newIngredient) {
        //udpate database
        long rows = CRUDHelper.update(
                tableName,
                new String[]{nameColumn, caloriesColumn, fatColumn, carbohydrateColumn, proteinColumn, amountGColumn, mealId},
                new Object[]{newIngredient.getName(), newIngredient.getCalories(), newIngredient.getFat(), newIngredient.getCarbohydrate(), newIngredient.getProtein(), newIngredient.getAmountG(), newIngredient.getMealId()},
                new int[]{Types.VARCHAR, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.INTEGER},
                idColumn,
                Types.INTEGER,
                newIngredient.getId()
        );

        if (rows == 0)
            throw new IllegalStateException("Ingredient to be updated with id " + newIngredient.getId() + " didn't exist in database");

        //update cache
        Optional<Ingredient> optionalIngredient = getIngredient(newIngredient.getId());
        optionalIngredient.ifPresentOrElse((oldIngredient) -> {
            ingredients.remove(oldIngredient);
            ingredients.add(newIngredient);
        }, () -> {
            throw new IllegalStateException("Ingredient to be updated with id " + newIngredient.getId() + " didn't exist in database");
        });
    }

    public static void insertIngredient(String name, String calories, String fat, String carbohydrate, String protein, String amountG, int inMealId) {
        //update database
        int id = (int) CRUDHelper.create(
                tableName,
                new String[]{nameColumn, caloriesColumn, fatColumn, carbohydrateColumn, proteinColumn, amountGColumn, mealId},
                new Object[]{name, calories, fat, carbohydrate, protein, amountG, inMealId},
                new int[]{Types.VARCHAR, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.INTEGER});

        //update cache
        ingredients.add(new Ingredient(id, name, calories, fat, carbohydrate, protein, amountG, inMealId));
    }

    public static void delete(int id) {
        //update database
        CRUDHelper.delete(tableName, id);

        //update cache
        Optional<Ingredient> ingredient = getIngredient(id);
        ingredient.ifPresent(ingredients::remove);

    }

    public static Optional<Ingredient> getIngredient(int id) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() == id) return Optional.of(ingredient);
        }
        return Optional.empty();
    }
}
