package meal.journal.mealjournal.model;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import lombok.Getter;

import java.util.List;

@Getter
public class Meal {
    private final int id;
    private final ReadOnlyStringProperty name;
    private final ReadOnlyListProperty<Ingredient> ingredients;

    public Meal(int id, String name, List<Ingredient> ingredients) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.ingredients = new SimpleListProperty<>(FXCollections.observableArrayList(ingredients));
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    @Override
    public String toString() {
        return name + ": " + ingredients;
    }
}
