module uqvirutal.edu.co.cinemauq {
    requires javafx.controls;
    requires javafx.fxml;

    opens uqvirutal.edu.co.cinemauq to javafx.fxml;
    opens uqvirutal.edu.co.cinemauq.viewController to javafx.fxml;

    exports uqvirutal.edu.co.cinemauq;
    exports uqvirutal.edu.co.cinemauq.viewController;
}