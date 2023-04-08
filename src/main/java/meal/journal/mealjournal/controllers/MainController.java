package meal.journal.mealjournal.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import meal.journal.mealjournal.dao.IngredientDao;
import meal.journal.mealjournal.dao.MealDao;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;
import meal.journal.mealjournal.model.MealName;
import meal.journal.mealjournal.service.MealService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        addColumnsToTables();
    }

    private void addColumnsToTables() {
        List<Ingredient> ingredients = IngredientDao.getIngredients().stream().toList();
        List<Meal> meals = MealDao.getMeals().stream().toList();

        for (Meal meal : meals) {
            if (meal.getDate().isEqual(datePicker.getValue())) {
                ObservableList<Ingredient> validIngr = FXCollections.observableList(ingredients.stream()
                        .filter(ingredient -> ingredient.getMealId() == meal.getId()).toList());

                switch (meal.getName()) {
                    case "Breakfast" -> addColumns(validIngr, breakfastTable);
                    case "II Breakfast" -> addColumns(validIngr, iibreakfastTable);
                    case "Lunch" -> addColumns(validIngr, lunchTable);
                    case "Snack" -> addColumns(validIngr, snackTable);
                    case "Dinner" -> addColumns(validIngr, dinnerTable);
                }
            }
        }
    }

    private void addColumns(ObservableList<Ingredient> validIngr, TableView<Ingredient> table) {
        TableColumn<Ingredient, String> name = new TableColumn<>("Ingredient");
        TableColumn<Ingredient, BigDecimal> calories = new TableColumn<>("Calories");
        TableColumn<Ingredient, BigDecimal> fat = new TableColumn<>("Fats");
        TableColumn<Ingredient, BigDecimal> carbohydrate = new TableColumn<>("Carbohydrate");
        TableColumn<Ingredient, BigDecimal> protein = new TableColumn<>("Protein");
        TableColumn<Ingredient, BigDecimal> amount = new TableColumn<>("Amount");

        setColumnsWidth(table, name, calories, fat, carbohydrate, protein, amount);

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        calories.setCellValueFactory(new PropertyValueFactory<>("calories"));
        fat.setCellValueFactory(new PropertyValueFactory<>("fat"));
        carbohydrate.setCellValueFactory(new PropertyValueFactory<>("carbohydrate"));
        protein.setCellValueFactory(new PropertyValueFactory<>("protein"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.getColumns().add(name);
        table.getColumns().add(calories);
        table.getColumns().add(fat);
        table.getColumns().add(carbohydrate);
        table.getColumns().add(protein);
        table.getColumns().add(amount);

        table.setItems(validIngr);
    }

    private static void setColumnsWidth(TableView<Ingredient> table, TableColumn<Ingredient, String> name, TableColumn<Ingredient, BigDecimal> calories, TableColumn<Ingredient, BigDecimal> fat, TableColumn<Ingredient, BigDecimal> carbohydrate, TableColumn<Ingredient, BigDecimal> protein, TableColumn<Ingredient, BigDecimal> amount) {
        name.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
        calories.prefWidthProperty().bind(table.widthProperty().multiply(0.17));
        fat.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
        carbohydrate.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
        protein.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
        amount.prefWidthProperty().bind(table.widthProperty().multiply(0.17));
        name.setResizable(false);
        calories.setResizable(false);
        fat.setResizable(false);
        carbohydrate.setResizable(false);
        protein.setResizable(false);
        amount.setResizable(false);
    }

    @FXML
    void addMeal(ActionEvent event) {
        String ingredientName = ingredientField.getText();
        MealName mealName = choiceBox.getValue();
        LocalDate date = datePicker.getValue();

        Ingredient ingredient = mealService.getIngredient(ingredientName, mealName.getMealName(), date);

        Optional<Meal> meal = MealDao.getMeal(ingredient.getMealId());
        if (meal.isPresent()) {

        }
    }

    @FXML
    void dateChange(ActionEvent event) {
        today = datePicker.getValue();

        clearTables();
        addColumnsToTables();
    }

    @FXML
    void nextDay(ActionEvent event) {
        today = today.plusDays(1);
        datePicker.setValue(today);

        clearTables();
        addColumnsToTables();
    }

    @FXML
    void prevDay(ActionEvent event) {
        today = today.minusDays(1);
        datePicker.setValue(today);

        clearTables();
        addColumnsToTables();
    }

    private void clearTables() {
        breakfastTable.getColumns().clear();
        iibreakfastTable.getColumns().clear();
        lunchTable.getColumns().clear();
        snackTable.getColumns().clear();
        dinnerTable.getColumns().clear();
    }
}
