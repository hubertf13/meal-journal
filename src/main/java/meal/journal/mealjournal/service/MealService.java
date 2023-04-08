package meal.journal.mealjournal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import meal.journal.mealjournal.MealsApplication;
import meal.journal.mealjournal.dao.IngredientDao;
import meal.journal.mealjournal.dao.MealDao;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

public class MealService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Ingredient getIngredient(String rawIngredient, String mealName, LocalDate date) throws IOException {
        URL url = buildUrl(rawIngredient);
        JsonNode jsonNode = objectMapper.readTree(url);

        JsonNode totalNutrientsNode = jsonNode.get("totalNutrients");
        if (totalNutrientsNode.size() != 0) {
            Optional<Meal> optionalMeal = MealDao.getMeal(mealName, date);
            Meal meal = optionalMeal.isEmpty() ? MealDao.insertMeal(mealName, date) : optionalMeal.get();

            return createIngredient(jsonNode, meal);
        } else {
            throw new IOException();
        }
    }

    private Ingredient createIngredient(JsonNode jsonNode, Meal meal) {

        String ingrName = jsonNode.get("ingredients").get(0).get("parsed").get(0).get("food").asText();
        String ingrCalories = String.valueOf(Math.round(jsonNode.get("calories").asDouble() * 100.0) / 100.0);
        String ingrFat = String.valueOf(Math.round(
                jsonNode.get("totalNutrients").get("FAT").get("quantity").asDouble() * 100.0) / 100.0);
        String ingrCarbohydrate = String.valueOf(Math.round(
                jsonNode.get("totalNutrients").get("CHOCDF").get("quantity").asDouble() * 100.0) / 100.0);
        String ingrProtein = String.valueOf(Math.round(
                jsonNode.get("totalNutrients").get("PROCNT").get("quantity").asDouble() * 100.0) / 100.0);
        String ingrAmount = jsonNode.get("ingredients").get(0).get("parsed").get(0).get("quantity").asText() +
                jsonNode.get("ingredients").get(0).get("parsed").get(0).get("measure").asText();

        return IngredientDao.insertIngredient(ingrName, ingrCalories, ingrFat,
                ingrCarbohydrate, ingrProtein, ingrAmount, meal.getId());
    }

    private URL buildUrl(String rawIngredient) throws MalformedURLException {
        String convertedIngredient = rawIngredient.replaceAll(" ", "%20");

        String requestUrl = MealsApplication.mainApiUrl + "?" +
                "app_id=" + MealsApplication.apiAppId +
                "&" +
                "app_key=" + MealsApplication.apiAppKey +
                "&" +
                "ingr=" + convertedIngredient;

        return new URL(requestUrl);
    }
}
