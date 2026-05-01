module com.example.pdiproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires java.sql;
    requires javafx.graphics;


    opens com.example.pdiproject to javafx.fxml;
    exports com.example.pdiproject;
    exports com.example.pdiproject.Controllers;
    opens com.example.pdiproject.Controllers to javafx.fxml;
}