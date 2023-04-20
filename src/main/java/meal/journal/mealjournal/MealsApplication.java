package meal.journal.mealjournal;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import meal.journal.mealjournal.dao.Database;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class MealsApplication extends Application {
    private static final Log log = LogFactory.getLog(MealsApplication.class);
    private final static String DATABASE_LOCATION_PROP = "database_location";
    private final static String MAIN_API_URL_PROP = "edamam_api_url";
    private final static String FOOD_API_URL_PROP = "edamam_food_api_url";
    private final static String API_APP_ID_PROP = "edamam_app_id";
    private final static String API_APP_KEY_PROP = "edamam_app_key";

    private final static String SCREEN_WIDTH_PROP = "screen_width";
    private final static String SCREEN_HEIGHT_PROP = "screen_height";
    private static String databaseLocation;
    private static String mainApiUrl;
    private static String foodApiUrl;
    private static String apiAppId;
    private static String apiAppKey;
    private static String screenWidth;
    private static String screenHeight;

    @Override
    public void start(Stage stage) throws IOException {
        setProperties();
        if (Database.isOK()) {
            FXMLLoader fxmlLoader = new FXMLLoader(MealsApplication.class.getResource("view/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), Double.parseDouble(screenWidth), Double.parseDouble(screenHeight));
            stage.setTitle("Meal Journal");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Database error");
            errorAlert.setContentText("Could not load database");
            errorAlert.showAndWait();
            Platform.exit();
        }
    }

    private void setProperties() {
        Properties p = loadProperties();

        databaseLocation = p.getProperty(DATABASE_LOCATION_PROP, Objects.requireNonNull(MealsApplication.class.getResource("database/database")).toString());
        mainApiUrl = p.getProperty(MAIN_API_URL_PROP, "https://api.edamam.com/api/nutrition-data");
        apiAppId = p.getProperty(API_APP_ID_PROP, "b9574edf");
        apiAppKey = p.getProperty(API_APP_KEY_PROP, "a6fff2053066b3ffe0ae193e69c4a6cc");
        screenWidth = p.getProperty(SCREEN_WIDTH_PROP, "1280");
        screenHeight = p.getProperty(SCREEN_HEIGHT_PROP, "720");
        foodApiUrl = p.getProperty(FOOD_API_URL_PROP, "https://api.edamam.com/api/food-database/v2/parser");
    }

    private Properties loadProperties() {
        final Properties p = new Properties();

        try (
                FileReader fileReader = new FileReader("config/mealJournal.properties")
        ) {
            p.load(fileReader);
        } catch (IOException e) {
            log.error("Default properties will be read");
        }
        return p;
    }

    public static String getDatabaseLocation() {
        return databaseLocation;
    }

    public static String getMainApiUrl() {
        return mainApiUrl;
    }

    public static String getApiAppId() {
        return apiAppId;
    }

    public static String getApiAppKey() {
        return apiAppKey;
    }

    public static String getFoodApiUrl() {
        return foodApiUrl;
    }

    public static void main(String[] args) {
        launch();
    }
}