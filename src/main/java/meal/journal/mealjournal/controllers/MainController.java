package meal.journal.mealjournal.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import meal.journal.mealjournal.MealsApplication;
import meal.journal.mealjournal.dao.IngredientDao;
import meal.journal.mealjournal.dao.MealDao;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;
import meal.journal.mealjournal.model.MealName;
import meal.journal.mealjournal.service.MealService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MainController {

    private final ObservableList<MealName> mealNames = FXCollections.observableArrayList(Arrays.stream(MealName.values()).toList());
    private LocalDate today;
    private MealService mealService;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Ingredient> breakfastTable;

    @FXML
    private TextField carbsField;

    @FXML
    private ChoiceBox<MealName> choiceBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Ingredient> dinnerTable;

    @FXML
    private TextField fatsField;

    @FXML
    private TableView<Ingredient> iibreakfastTable;

    @FXML
    private TextField kcalField;

    @FXML
    private TableView<Ingredient> lunchTable;

    @FXML
    private Button nextButton;

    @FXML
    private TextField porteinsField;

    @FXML
    private Button prevButton;

    @FXML
    private TableView<Ingredient> snackTable;

    @FXML
    private TextField ingredientField;

    public void initialize() {
        mealService = new MealService();
        today = LocalDate.now();
        datePicker.setValue(today);
        choiceBox.setItems(mealNames);
        choiceBox.setValue(mealNames.stream().findFirst().orElse(MealName.BREAKFAST));

        TableColumn<Ingredient, String> name = new TableColumn<>("Ingredient");
        TableColumn<Ingredient, BigDecimal> calories = new TableColumn<>("Calories");
        TableColumn<Ingredient, BigDecimal> fat = new TableColumn<>("Fats");
        TableColumn<Ingredient, BigDecimal> carbohydrate = new TableColumn<>("Carbohydrate");
        TableColumn<Ingredient, BigDecimal> protein = new TableColumn<>("Protein");
        TableColumn<Ingredient, BigDecimal> amount = new TableColumn<>("Amount");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        calories.setCellValueFactory(new PropertyValueFactory<>("calories"));
        fat.setCellValueFactory(new PropertyValueFactory<>("fat"));
        carbohydrate.setCellValueFactory(new PropertyValueFactory<>("carbohydrate"));
        protein.setCellValueFactory(new PropertyValueFactory<>("protein"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        ObservableList<Ingredient> list = IngredientDao.getIngredients();

        breakfastTable.getColumns().add(name);
        breakfastTable.getColumns().add(calories);
        breakfastTable.getColumns().add(fat);
        breakfastTable.getColumns().add(carbohydrate);
        breakfastTable.getColumns().add(protein);
        breakfastTable.getColumns().add(amount);

        breakfastTable.setItems(list);
    }

    @FXML
    void addMeal(ActionEvent event) {
        String ingredientName = ingredientField.getText();
        MealName mealName = choiceBox.getValue();
        LocalDate date = datePicker.getValue();

        Ingredient ingredient = mealService.getIngredient(ingredientName, mealName.getMealName(), date);

        Optional<Meal> meal = MealDao.getMeal(ingredient.getMealId());
        if (meal.isPresent()) {
            for (Node centerGridPaneNode : MealsApplication.centerGridPaneNodes) {
                if (centerGridPaneNode instanceof TableView<?> tableView) {
//                    ObservableList<Ingredient> obsList = (ObservableList<Ingredient>) tableView.getItems();
//                    obsList.removeAll(obsList);
//                    obsList.addAll(In)
                }
            }
        }
    }

    @FXML
    void dateChange(ActionEvent event) {
        today = datePicker.getValue();
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
}
