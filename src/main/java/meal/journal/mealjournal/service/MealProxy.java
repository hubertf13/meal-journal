package meal.journal.mealjournal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import meal.journal.mealjournal.MealsApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MealProxy {
    private static final Log log = LogFactory.getLog(MealProxy.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MealService mealService;
    private final Map<String, JsonNode> nutrientsNodes = new HashMap<>();
    private final Map<String, JsonNode> foodNodes = new HashMap<>();

    public MealProxy(MealService mealService) {
        this.mealService = mealService;
    }

    public void getIngredient(String rawIngredient, String mealName, LocalDate date) throws IOException {
        JsonNode nutrientsJsonNode = nutrientsNodes.get(rawIngredient);
        String ingredientName;
        JsonNode foodJsonNode;

        if (nutrientsJsonNode == null) {
            nutrientsJsonNode = getJsonNode(rawIngredient, MealsApplication.getMainApiUrl());
            nutrientsNodes.put(rawIngredient, nutrientsJsonNode);

            ingredientName = nutrientsJsonNode.get("ingredients").get(0).get("parsed").get(0).get("foodMatch").asText();
            foodJsonNode = foodNodes.get(ingredientName);

            if (foodJsonNode == null) {
                foodJsonNode = getJsonNode(ingredientName, MealsApplication.getFoodApiUrl());
                foodNodes.put(ingredientName, foodJsonNode);
            }
        } else {
            ingredientName = nutrientsJsonNode.get("ingredients").get(0).get("parsed").get(0).get("foodMatch").asText();
            foodJsonNode = foodNodes.get(ingredientName);
        }

        mealService.getIngredient(mealName, date, nutrientsJsonNode, foodJsonNode);
    }

    private JsonNode getJsonNode(String rawIngredient, String apiUrl) throws IOException {
        URL nutrientsUrl = buildUrl(rawIngredient, apiUrl);
        return objectMapper.readTree(nutrientsUrl);
    }

    private URL buildUrl(String rawIngredient, String apiUrl) throws MalformedURLException {
        String convertedIngredient = rawIngredient.replaceAll(" ", "%20");

        String requestUrl = apiUrl + "?" +
                "app_id=" + MealsApplication.getApiAppId() +
                "&" +
                "app_key=" + MealsApplication.getApiAppKey() +
                "&" +
                "ingr=" + convertedIngredient;

        log.info("Request URL to API: " + requestUrl);

        return new URL(requestUrl);
    }
}
