module mitienda {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires AnimateFX;
    requires java.desktop;
    requires java.logging;
    
    opens Model to javafx.base;
    opens controller to javafx.fxml;
    opens mitienda to javafx.fxml;
    exports mitienda;
}
