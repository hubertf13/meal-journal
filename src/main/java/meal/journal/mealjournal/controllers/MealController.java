package meal.journal.mealjournal.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import meal.journal.mealjournal.model.Meal;
import meal.journal.mealjournal.model.MealName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MealController {

    private ObservableList<MealName> mealNames;
    private LocalDate today;

    @FXML
    private Button addButton;

    @FXML
    private TextField carbsBox;

    @FXML
    private ChoiceBox<MealName> choiceBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField fatsBox;

    @FXML
    private TextField kcalBox;

    @FXML
    private TableView<Meal> mealsTable;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    @FXML
    private TextField proteinsBox;

    public void initialize() {
        mealNames = FXCollections.observableArrayList(Arrays.stream(MealName.values()).toList());

        today = LocalDate.now();
        datePicker.setValue(today);
        choiceBox.setItems(mealNames);
        choiceBox.setValue(mealNames.stream().findFirst().orElse(MealName.BREAKFAST));
    }

    @FXML
    void addMeal(ActionEvent event) {
        System.out.println(choiceBox.getValue());
    }

    @FXML
    void nextDay(ActionEvent event) {
        today = today.plusDays(1);
        datePicker.setValue(today);
    }

    @FXML
    void prevDay(ActionEvent event) {
        today = today.minusDays(1);
        datePicker.setValue(today);
    }

    @FXML
    void dateChange(ActionEvent event) {
        today = datePicker.getValue();
    }
}


//    ObservableList<Ingredient> ingredients = IngredientDao.getIngredients();
//    ObservableList<Meal> meals = MealDao.getMeals();
//        System.out.println("============");
//                for (Meal meal : meals) {
//                System.out.println(meal);
//                }
//                System.out.println("============");
//                System.out.println("============");
//                for (Meal meal : meals) {
//                System.out.println(meal);
//                }
//                System.out.println("============");
//                //IngredientDao.insertIngredient("rice", "540", "0.9", "119", "9.9", "150", 1);
//
//                for (Ingredient ingredient : ingredients) {
//                System.out.println(ingredient);
//                }
//                System.out.println(ingredients);