module lk.ijse.edu.chatroomapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens lk.ijse.edu.chatroomapplication to javafx.fxml;
    exports lk.ijse.edu.chatroomapplication;
}