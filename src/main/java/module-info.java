module org.apiapplication {
    /**
     * JavaFX App built with Maven
     * @author: Gaous Muhammad Saklaen
     */
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires json;

    opens org.covidTracker to javafx.fxml;
    exports org.covidTracker;
}