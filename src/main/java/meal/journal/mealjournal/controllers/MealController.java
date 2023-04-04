package meal.journal.mealjournal.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import meal.journal.mealjournal.dao.IngredientDao;
import meal.journal.mealjournal.dao.MealDao;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;

public class MealController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

        ObservableList<Ingredient> ingredients = IngredientDao.getIngredients();
        ObservableList<Meal> meals = MealDao.getMeals();
        System.out.println("============");
        for (Meal meal : meals) {
            System.out.println(meal);
        }
        System.out.println("============");
        System.out.println("============");
        for (Meal meal : meals) {
            System.out.println(meal);
        }
        System.out.println("============");
        //IngredientDao.insertIngredient("rice", "540", "0.9", "119", "9.9", "150", 1);

        for (Ingredient ingredient : ingredients) {
            System.out.println(ingredient);
        }
        System.out.println(ingredients);

    }
}