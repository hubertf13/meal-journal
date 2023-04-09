module meal.journal.mealjournal {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.xerial.sqlitejdbc;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires commons.logging;

    opens meal.journal.mealjournal to javafx.fxml;
    opens meal.journal.mealjournal.model to javafx.base;
    exports meal.journal.mealjournal;
    exports meal.journal.mealjournal.controllers;
    opens meal.journal.mealjournal.controllers to javafx.fxml;
}