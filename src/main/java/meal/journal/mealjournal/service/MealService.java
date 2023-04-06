package meal.journal.mealjournal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import meal.journal.mealjournal.dao.IngredientDao;
import meal.journal.mealjournal.dao.MealDao;
import meal.journal.mealjournal.model.Ingredient;
import meal.journal.mealjournal.model.Meal;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

public class MealService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final static String MAIN_API_URL_PROP = "api_url";
    private final static String API_APP_ID_PROP = "app_id";
    private final static String API_APP_KEY_PROP = "app_key";
    private static String mainApiUrl;
    private static String apiAppId;
    private static String apiAppKey;

    public Ingredient getIngredient(String rawIngredient, String mealName, LocalDate date) {
        try {
            setProperties();

            URL url = buildUrl(rawIngredient);
            JsonNode jsonNode = objectMapper.readTree(url);

            Optional<Meal> optionalMeal = MealDao.getMeal(mealName, date);
            Meal meal = optionalMeal.isEmpty() ? MealDao.insertMeal(mealName, date) : optionalMeal.get();

            return createIngredient(jsonNode, meal);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Ingredient createIngredient(JsonNode jsonNode, Meal meal) {

        String ingrName = jsonNode.get("ingredients").get(0).get("parsed").get(0).get("food").asText();
        String ingrCalories = jsonNode.get("calories").asText();
        String ingrFat = jsonNode.get("totalNutrients").get("FAT").get("quantity").asText();
        String ingrCarbohydrate = jsonNode.get("totalNutrients").get("CHOCDF").get("quantity").asText();
        String ingrProtein = jsonNode.get("totalNutrients").get("PROCNT").get("quantity").asText();
        String ingrAmount = jsonNode.get("ingredients").get(0).get("parsed").get(0).get("quantity").asText() +
                jsonNode.get("ingredients").get(0).get("parsed").get(0).get("measure").asText();

        return IngredientDao.insertIngredient(ingrName, ingrCalories, ingrFat,
                ingrCarbohydrate, ingrProtein, ingrAmount, meal.getId());
    }

    private URL buildUrl(String rawIngredient) throws MalformedURLException {
        String convertedIngredient = rawIngredient.replaceAll(" ", "%20");

        String requestUrl = mainApiUrl + "?" +
                "app_id=" + apiAppId +
                "&" +
                "app_key=" + apiAppKey +
                "&" +
                "ingr=" + convertedIngredient;

        return new URL(requestUrl);
    }

    private void setProperties() {
        Properties p = loadProperties();

        mainApiUrl = p.getProperty(MAIN_API_URL_PROP, "https://api.edamam.com/api/nutrition-data");
        apiAppId = p.getProperty(API_APP_ID_PROP, "b9574edf");
        apiAppKey = p.getProperty(API_APP_KEY_PROP, "a6fff2053066b3ffe0ae193e69c4a6cc");
    }

    private Properties loadProperties() {
        final Properties p = new Properties();

        try (
                FileReader fileReader = new FileReader("properties/mealJournal.properties")
        ) {
            p.load(fileReader);
        } catch (IOException e) {
            System.out.println("ERROR - default properties will be read");
        }
        return p;
    }
}
