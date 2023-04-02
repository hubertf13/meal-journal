package meal.journal.mealjournal.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MealDao {
//    private static final String tableName = "meal";
//    private static final String idColumn = "id";
//    private static final String nameColumn = "name";
//
//    private static final ObservableList<Meal> meals;
//
//    static {
//        meals = FXCollections.observableArrayList();
//        updateMealsFromDB();
//    }
//
//    public static ObservableList<Ingredient> getIngredients() {
//        return FXCollections.unmodifiableObservableList(ingredients);
//    }
//
//    private static void updateMealsFromDB() {
//
//        String query = "SELECT * FROM " + tableName;
//
//        try (Connection connection = Database.connect()) {
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet rs = statement.executeQuery();
//            ingredients.clear();
//            while (rs.next()) {
//                ingredients.add(new Ingredient(
//                        rs.getInt(idColumn),
//                        rs.getString(nameColumn),
//                        rs.getString(caloriesColumn),
//                        rs.getString(fatColumn),
//                        rs.getString(carbohydrateColumn),
//                        rs.getString(proteinColumn),
//                        rs.getString(amountGColumn),
//                        rs.getInt(mealId)));
//            }
//        } catch (SQLException e) {
//            Logger.getAnonymousLogger().log(
//                    Level.SEVERE,
//                    LocalDateTime.now() + ": Could not load Persons from database ");
//            ingredients.clear();
//        }
//    }
//
//    public static void update(Ingredient newIngredient) {
//        //udpate database
//        long rows = CRUDHelper.update(
//                tableName,
//                new String[]{nameColumn, caloriesColumn, fatColumn, carbohydrateColumn, proteinColumn, amountGColumn, mealId},
//                new Object[]{newIngredient.getName(), newIngredient.getCalories(), newIngredient.getFat(), newIngredient.getCarbohydrate(), newIngredient.getProtein(), newIngredient.getAmountG(), newIngredient.getMealId()},
//                new int[]{Types.VARCHAR, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.INTEGER},
//                idColumn,
//                Types.INTEGER,
//                newIngredient.getId()
//        );
//
//        if (rows == 0)
//            throw new IllegalStateException("Person to be updated with id " + newIngredient.getId() + " didn't exist in database");
//
//        //update cache
//        Optional<Ingredient> optionalIngredient = getPerson(newIngredient.getId());
//        optionalIngredient.ifPresentOrElse((oldIngredient) -> {
//            ingredients.remove(oldIngredient);
//            ingredients.add(newIngredient);
//        }, () -> {
//            throw new IllegalStateException("Person to be updated with id " + newIngredient.getId() + " didn't exist in database");
//        });
//    }
//
//    public static void insertIngredient(String name, String calories, String fat, String carbohydrate, String protein, String amountG, int inMealId) {
//        //update database
//        int id = (int) CRUDHelper.create(
//                tableName,
//                new String[]{nameColumn, caloriesColumn, fatColumn, carbohydrateColumn, proteinColumn, amountGColumn, mealId},
//                new Object[]{name, calories, fat, carbohydrate, protein, amountG, inMealId},
//                new int[]{Types.VARCHAR, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.NUMERIC, Types.INTEGER});
//
//        //update cache
//        ingredients.add(new Ingredient(id, name, calories, fat, carbohydrate, protein, amountG, inMealId));
//    }
//
//    public static void delete(int id) {
//        //update database
//        CRUDHelper.delete(tableName, id);
//
//        //update cache
//        Optional<Ingredient> person = getPerson(id);
//        person.ifPresent(ingredients::remove);
//
//    }
//
//    public static Optional<Ingredient> getPerson(int id) {
//        for (Ingredient ingredient : ingredients) {
//            if (ingredient.getId() == id) return Optional.of(ingredient);
//        }
//        return Optional.empty();
//    }
}
