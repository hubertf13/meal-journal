package meal.journal.mealjournal;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import meal.journal.mealjournal.dao.Database;

import java.io.IOException;
import java.util.List;

public class MealsApplication extends Application {
    public static List<Node> centerGridPaneNodes;

    @Override
    public void start(Stage stage) throws IOException {
        if (Database.isOK()) {
            FXMLLoader fxmlLoader = new FXMLLoader(MealsApplication.class.getResource("view/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            stage.setTitle("Meal Journal");
            stage.setScene(scene);
            setListOfCenterNodes(scene);
            stage.show();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Database error");
            errorAlert.setContentText("Could not load database");
            errorAlert.showAndWait();
            Platform.exit();
        }
    }

    private static void setListOfCenterNodes(Scene scene) {
        ObservableList<Node> childrenUnmodifiable = scene.getRoot().getChildrenUnmodifiable();
        List<Node> nodes = childrenUnmodifiable.stream().toList();
        BorderPane borderPane = (BorderPane) nodes.get(0);
        GridPane centerGridPane = (GridPane) borderPane.centerProperty().getValue();
        centerGridPaneNodes = centerGridPane.getChildrenUnmodifiable().stream().toList();
    }

    public static void main(String[] args) {
        launch();
    }
}