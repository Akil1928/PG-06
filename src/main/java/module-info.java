module ucr.lab.pg06 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ucr.lab.pg06 to javafx.fxml;
    exports ucr.lab.pg06;
    exports controller;
    opens controller to javafx.fxml;
}