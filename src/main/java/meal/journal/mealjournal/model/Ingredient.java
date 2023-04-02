package meal.journal.mealjournal.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Ingredient {
    private final int id;
    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty calories;
    private final ReadOnlyStringProperty fat;
    private final ReadOnlyStringProperty carbohydrate;
    private final ReadOnlyStringProperty protein;
    private final ReadOnlyStringProperty amountG;
    private final int mealId;

    public Ingredient(int id, String name, String calories, String fat, String carbohydrate, String protein, String amountG, int mealId) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.calories = new SimpleStringProperty(calories);
        this.fat = new SimpleStringProperty(fat);
        this.carbohydrate = new SimpleStringProperty(carbohydrate);
        this.protein = new SimpleStringProperty(protein);
        this.amountG = new SimpleStringProperty(amountG);
        this.mealId = mealId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    public String getCalories() {
        return calories.get();
    }

    public ReadOnlyStringProperty caloriesProperty() {
        return calories;
    }

    public String getFat() {
        return fat.get();
    }

    public ReadOnlyStringProperty fatProperty() {
        return fat;
    }

    public String getCarbohydrate() {
        return carbohydrate.get();
    }

    public ReadOnlyStringProperty carbohydrateProperty() {
        return carbohydrate;
    }

    public String getProtein() {
        return protein.get();
    }

    public ReadOnlyStringProperty proteinProperty() {
        return protein;
    }

    public String getAmountG() {
        return amountG.get();
    }

    public ReadOnlyStringProperty amountGProperty() {
        return amountG;
    }

    public int getMealId() {
        return mealId;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name=" + name.get() +
                ", calories=" + calories.get() +
                ", fat=" + fat.get() +
                ", carbohydrate=" + carbohydrate.get() +
                ", protein=" + protein.get() +
                ", amountG=" + amountG.get() +
                "g}";
    }
}
