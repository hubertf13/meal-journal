package meal.journal.mealjournal.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import meal.journal.mealjournal.dao.IngredientDao;
import meal.journal.mealjournal.dao.MealDao;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;
import meal.journal.mealjournal.model.MealName;
import meal.journal.mealjournal.service.MealProxy;
import meal.journal.mealjournal.service.MealService;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainController {

    private final ObservableList<MealName> mealNames = FXCollections.observableArrayList(Arrays.stream(MealName.values()).toList());
    private LocalDate today;
    private MealProxy mealProxy;

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
    private TextField porteinsField;

    @FXML
    private TableView<Ingredient> snackTable;

    @FXML
    private TextField ingredientField;

    @FXML
    private Label timeLabel;

    @FXML
    private ProgressBar progressBar;

    public void initialize() {
        MealService mealService = new MealService();
        mealProxy = new MealProxy(mealService);

        progressBar.setVisible(false);
        today = LocalDate.now();
        datePicker.setValue(today);
        choiceBox.setItems(mealNames);
        choiceBox.setValue(mealNames.stream().findFirst().orElse(MealName.BREAKFAST));
        timenow();

        ArrayList<Ingredient> tablesIngredients = addColumnsToTables();
        calculateNutrients(tablesIngredients);
    }

    private ArrayList<Ingredient> addColumnsToTables() {
        List<Ingredient> ingredients = IngredientDao.getIngredients().stream().toList();
        List<Meal> meals = MealDao.getMeals().stream().toList();
        ArrayList<Ingredient> addedIngredients = new ArrayList<>();

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
                addedIngredients.addAll(validIngr);
            }
        }
        return addedIngredients;
    }

    private void addColumns(ObservableList<Ingredient> validIngr, TableView<Ingredient> table) {
        TableColumn<Ingredient, ImageView> image = new TableColumn<>("Image");
        TableColumn<Ingredient, String> name = new TableColumn<>("Ingredient");
        TableColumn<Ingredient, BigDecimal> calories = new TableColumn<>("Calories");
        TableColumn<Ingredient, BigDecimal> fat = new TableColumn<>("Fats");
        TableColumn<Ingredient, BigDecimal> carbohydrate = new TableColumn<>("Carbohydrate");
        TableColumn<Ingredient, BigDecimal> protein = new TableColumn<>("Protein");
        TableColumn<Ingredient, BigDecimal> amount = new TableColumn<>("Amount");

        setColumnsWidth(table, image, name, calories, fat, carbohydrate, protein, amount);

        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        calories.setCellValueFactory(new PropertyValueFactory<>("calories"));
        fat.setCellValueFactory(new PropertyValueFactory<>("fat"));
        carbohydrate.setCellValueFactory(new PropertyValueFactory<>("carbohydrate"));
        protein.setCellValueFactory(new PropertyValueFactory<>("protein"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.getColumns().add(image);
        table.getColumns().add(name);
        table.getColumns().add(calories);
        table.getColumns().add(fat);
        table.getColumns().add(carbohydrate);
        table.getColumns().add(protein);
        table.getColumns().add(amount);

        table.setItems(validIngr);
    }

    private void setColumnsWidth(TableView<Ingredient> table, TableColumn<Ingredient, ImageView> image, TableColumn<Ingredient, String> name, TableColumn<Ingredient, BigDecimal> calories, TableColumn<Ingredient, BigDecimal> fat, TableColumn<Ingredient, BigDecimal> carbohydrate, TableColumn<Ingredient, BigDecimal> protein, TableColumn<Ingredient, BigDecimal> amount) {
        image.prefWidthProperty().bind(table.widthProperty().multiply(0.04));
        name.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
        calories.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
        fat.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        carbohydrate.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        protein.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        amount.prefWidthProperty().bind(table.widthProperty().multiply(0.17));

        image.setResizable(false);
        name.setResizable(false);
        calories.setResizable(false);
        fat.setResizable(false);
        carbohydrate.setResizable(false);
        protein.setResizable(false);
        amount.setResizable(false);
    }

    private void calculateNutrients(ArrayList<Ingredient> tablesIngredients) {
        double totalCalories = Math.round(calculateTotalCalories(tablesIngredients) * 100.0) / 100.0;
        double totalProteins = Math.round(calculateTotalProteins(tablesIngredients) * 100.0) / 100.0;
        double totalFat = Math.round(calculateTotalFat(tablesIngredients) * 100.0) / 100.0;
        double totalCarbohydrates = Math.round(calculateTotalCarbohydrates(tablesIngredients) * 100.0) / 100.0;

        kcalField.setText(String.valueOf(totalCalories));
        porteinsField.setText(String.valueOf(totalProteins));
        fatsField.setText(String.valueOf(totalFat));
        carbsField.setText(String.valueOf(totalCarbohydrates));
    }

    private double calculateTotalCalories(ArrayList<Ingredient> tablesIngredients) {
        double calories = 0.0;
        for (Ingredient tablesIngredient : tablesIngredients) {
            calories += Double.parseDouble(tablesIngredient.getCalories());
        }

        return calories;
    }

    private double calculateTotalProteins(ArrayList<Ingredient> tablesIngredients) {
        double proteins = 0.0;
        for (Ingredient tablesIngredient : tablesIngredients) {
            proteins += Double.parseDouble(tablesIngredient.getProtein());
        }

        return proteins;
    }

    private double calculateTotalCarbohydrates(ArrayList<Ingredient> tablesIngredients) {
        double carbohydrate = 0.0;
        for (Ingredient tablesIngredient : tablesIngredients) {
            carbohydrate += Double.parseDouble(tablesIngredient.getCarbohydrate());
        }

        return carbohydrate;
    }

    private double calculateTotalFat(ArrayList<Ingredient> tablesIngredients) {
        double fat = 0.0;
        for (Ingredient tablesIngredient : tablesIngredients) {
            fat += Double.parseDouble(tablesIngredient.getFat());
        }

        return fat;
    }

    @FXML
    public void onEnter() {
        addMeal();
    }

    @FXML
    void addMeal() {
        String ingredientName = ingredientField.getText();
        MealName mealName = choiceBox.getValue();
        LocalDate date = datePicker.getValue();

        startAddThread(ingredientName, mealName, date);
    }

    private void startAddThread(String ingredientName, MealName mealName, LocalDate date) {
        Task<Ingredient> task = new Task<>() {
            @Override
            protected Ingredient call() throws IOException, InterruptedException {
                progressBar.setVisible(true);

                Ingredient ingredient = mealProxy.getIngredient(ingredientName, mealName.getMealName(), date, progressBar);

                progressBar.setProgress(0.99);
                Thread.sleep(300);
                progressBar.setVisible(false);
                return ingredient;
            }
        };

        task.setOnSucceeded(t -> updateWindow());
        task.setOnFailed(t -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ingredient has not been added");
            alert.showAndWait();
            progressBar.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    void dateChange() {
        today = datePicker.getValue();

        updateWindow();
    }

    @FXML
    void nextDay() {
        today = today.plusDays(1);
        datePicker.setValue(today);

        updateWindow();
    }

    private void updateWindow() {
        Platform.runLater(() -> {
            clearTables();
            ArrayList<Ingredient> tablesIngredients = addColumnsToTables();

            calculateNutrients(tablesIngredients);
        });
    }

    @FXML
    void prevDay() {
        today = today.minusDays(1);
        datePicker.setValue(today);

        updateWindow();
    }

    private void clearTables() {
        breakfastTable.getColumns().clear();
        iibreakfastTable.getColumns().clear();
        lunchTable.getColumns().clear();
        snackTable.getColumns().clear();
        dinnerTable.getColumns().clear();
    }

    @FXML
    void onBreakfastDelete() {
        onDelete(breakfastTable);
    }

    @FXML
    void onIIBreakfastDelete() {
        onDelete(iibreakfastTable);
    }

    @FXML
    void onLunchDelete() {
        onDelete(lunchTable);
    }

    @FXML
    void onSnackDelete() {
        onDelete(snackTable);
    }

    @FXML
    void onDinnerDelete() {
        onDelete(dinnerTable);
    }

    private void onDelete(TableView<Ingredient> tableView) {
        Ingredient selectedItem = tableView.getSelectionModel().getSelectedItem();
        IngredientDao.delete(selectedItem.getId());

        updateWindow();
    }

    private void timenow() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
                final String timenow = sdf.format(new Date());
                Platform.runLater(() -> timeLabel.setText(timenow));
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
