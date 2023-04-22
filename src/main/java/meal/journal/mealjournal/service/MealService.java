package meal.journal.mealjournal.service;

import com.fasterxml.jackson.databind.JsonNode;
import meal.journal.mealjournal.dao.IngredientDao;
import meal.journal.mealjournal.dao.MealDao;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

public class MealService {
    private static final Log log = LogFactory.getLog(MealService.class);

    public Ingredient getIngredient(String mealName, LocalDate date, JsonNode nutrientsJsonNode,
                                    JsonNode foodJsonNode) throws IOException {

        JsonNode totalNutrientsNode = nutrientsJsonNode.get("totalNutrients");
        if (totalNutrientsNode.size() != 0) {
            Optional<Meal> optionalMeal = MealDao.getMeal(mealName, date);
            Meal meal = optionalMeal.isEmpty() ? MealDao.insertMeal(mealName, date) : optionalMeal.get();

            return createIngredient(nutrientsJsonNode, foodJsonNode, meal);
        } else {
            log.error("Something went wrong with getting json");
            throw new IOException();
        }
    }

    private void addImage(String imageUrl, String imageName) throws IOException {
        File file = new File("src/main/resources/meal/journal/mealjournal/images/" + imageName);
        String absolutePath = file.getAbsolutePath();

        FileUtils.copyURLToFile(
                new URL(imageUrl),
                new File(absolutePath),
                5000,
                10000);
    }

    private Ingredient createIngredient(JsonNode nutrientsJsonNode, JsonNode foodJsonNode, Meal meal) throws IOException {

        String ingrName = nutrientsJsonNode.get("ingredients").get(0).get("parsed").get(0).get("food").asText().toLowerCase();
        String ingrCalories = String.valueOf(Math.round(nutrientsJsonNode.get("calories").asDouble() * 100.0) / 100.0);
        String ingrFat = String.valueOf(Math.round(
                nutrientsJsonNode.get("totalNutrients").get("FAT").get("quantity").asDouble() * 100.0) / 100.0);
        String ingrCarbohydrate = String.valueOf(Math.round(
                nutrientsJsonNode.get("totalNutrients").get("CHOCDF").get("quantity").asDouble() * 100.0) / 100.0);
        String ingrProtein = String.valueOf(Math.round(
                nutrientsJsonNode.get("totalNutrients").get("PROCNT").get("quantity").asDouble() * 100.0) / 100.0);
        String ingrAmount = nutrientsJsonNode.get("ingredients").get(0).get("parsed").get(0).get("quantity").asText() +
                nutrientsJsonNode.get("ingredients").get(0).get("parsed").get(0).get("measure").asText();
        String imageUrl = foodJsonNode.get("parsed").get(0).get("food").get("image").asText();
        String[] splitedImage = imageUrl.split("/");
        String image = splitedImage[splitedImage.length - 1];

        addImage(imageUrl, image);

        log.info("Inserting ingredient to database...");

        return IngredientDao.insertIngredient(ingrName, ingrCalories, ingrFat,
                ingrCarbohydrate, ingrProtein, ingrAmount, meal.getId(), image);
    }
}
