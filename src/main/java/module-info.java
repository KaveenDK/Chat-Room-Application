module lk.ijse.edu.chatroomapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.edu.chatroomapplication to javafx.fxml;
    exports lk.ijse.edu.chatroomapplication;
}