package meal.journal.mealjournal.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Ingredient {
    private final int id;
    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty calories;
    private final ReadOnlyStringProperty fat;
    private final ReadOnlyStringProperty carbohydrate;
    private final ReadOnlyStringProperty protein;
    private final ReadOnlyStringProperty amount;
    private final int mealId;
    private final ImageView image;

    public Ingredient(int id, String name, String calories, String fat, String carbohydrate, String protein, String amount, int mealId, String image) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.calories = new SimpleStringProperty(calories);
        this.fat = new SimpleStringProperty(fat);
        this.carbohydrate = new SimpleStringProperty(carbohydrate);
        this.protein = new SimpleStringProperty(protein);
        this.amount = new SimpleStringProperty(amount);
        this.mealId = mealId;
        File file = new File("src/main/resources/meal/journal/mealjournal/images/" + image);
        if (file.exists()) {
            String absolutePath = file.getAbsolutePath();
            this.image = new ImageView(new Image(absolutePath, 50.0, 50.0, true, true));
        } else {
            this.image = new ImageView();
        }
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

    public String getAmount() {
        return amount.get();
    }

    public ReadOnlyStringProperty amountProperty() {
        return amount;
    }

    public int getMealId() {
        return mealId;
    }

    public ImageView getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name=" + name.get() +
                ", calories=" + calories.get() +
                ", fat=" + fat.get() +
                ", carbohydrate=" + carbohydrate.get() +
                ", protein=" + protein.get() +
                ", amount=" + amount.get() +
                "}";
    }
}
