package meal.journal.mealjournal.model;

public enum MealName {
    BREAKFAST("Breakfast"),
    II_BREAKFAST("II Breakfast"),
    LUNCH("Lunch"),
    SNACK("Snack"),
    DINNER("Dinner");

    private final String mealName;

    MealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealName() {
        return mealName;
    }

    @Override
    public String toString() {
        return mealName;
    }
}
