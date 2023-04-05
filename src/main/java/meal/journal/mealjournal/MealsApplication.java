package meal.journal.mealjournal;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import meal.journal.mealjournal.dao.Database;

import java.io.IOException;

public class MealsApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        if (Database.isOK()) {
            FXMLLoader fxmlLoader = new FXMLLoader(MealsApplication.class.getResource("view/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);
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

    public static void main(String[] args) {
        launch();
    }
}