package meal.journal.mealjournal.model;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meal {
    private final int id;
    private final ReadOnlyStringProperty name;
    private final LocalDate date;
    private final ReadOnlyListProperty<Ingredient> ingredients;

    public Meal(int id, String name, LocalDate date, List<Ingredient> ingredients) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.date = date;
        if (ingredients == null)
            ingredients = new ArrayList<>();
        this.ingredients = new SimpleListProperty<>(FXCollections.observableArrayList(ingredients));
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
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

    public ObservableList<Ingredient> getIngredients() {
        return ingredients.get();
    }

    public ReadOnlyListProperty<Ingredient> ingredientsProperty() {
        return ingredients;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        StringBuilder allIngredients = new StringBuilder();

        if (ingredients.size() != 0) {
            for (Ingredient ingredient : ingredients) {
                allIngredients.append(ingredient).append(", ");
            }

            return "Meal{" +
                    name.get() +
                    " " +
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                    ": "
                    + allIngredients.substring(0, allIngredients.length() - 2) +
                    "}";
        }
        return "Meal{" +
                name.get() +
                ": no ingredients}";
    }
}
