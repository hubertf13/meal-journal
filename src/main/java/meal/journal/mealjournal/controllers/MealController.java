package meal.journal.mealjournal.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import meal.journal.mealjournal.dao.IngredientDao;
import meal.journal.mealjournal.model.Ingredient;

public class MealController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

        ObservableList<Ingredient> ingredients = IngredientDao.getIngredients();
        //IngredientDao.insertIngredient("rice", "540", "0.9", "119", "9.9", "150", 1);

        for (Ingredient ingredient : ingredients) {
            System.out.println(ingredient);
        }
        System.out.println(ingredients);

    }
}