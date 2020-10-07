module org.apiapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires json.simple;

    opens org.covidTracker to javafx.fxml;
    exports org.covidTracker;
}