module org.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens org.main to javafx.fxml;
    exports org.main;
}
