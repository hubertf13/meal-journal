package meal.journal.mealjournal;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import meal.journal.mealjournal.dao.Database;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MealsApplication extends Application {
    private final static String MAIN_API_URL_PROP = "api_url";
    private final static String API_APP_ID_PROP = "app_id";
    private final static String API_APP_KEY_PROP = "app_key";

    private final static String SCREEN_WIDTH_PROP = "screen_width";
    private final static String SCREEN_HEIGHT_PROP = "screen_height";
    public static String mainApiUrl;
    public static String apiAppId;
    public static String apiAppKey;
    private static String screenWidth;
    private static String screenHeight;

    @Override
    public void start(Stage stage) throws IOException {
        if (Database.isOK()) {
            setProperties();
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

        mainApiUrl = p.getProperty(MAIN_API_URL_PROP, "https://api.edamam.com/api/nutrition-data");
        apiAppId = p.getProperty(API_APP_ID_PROP, "b9574edf");
        apiAppKey = p.getProperty(API_APP_KEY_PROP, "a6fff2053066b3ffe0ae193e69c4a6cc");
        screenWidth = p.getProperty(SCREEN_WIDTH_PROP, "1280");
        screenHeight = p.getProperty(SCREEN_HEIGHT_PROP, "720");
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

    public static void main(String[] args) {
        launch();
    }
}