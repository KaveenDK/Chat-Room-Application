package lk.ijse.edu.chatroomapplication;

/**
 * --------------------------------------------
 * @Author Dimantha Kaveen
 * @GitHub: https://github.com/KaveenDK
 * --------------------------------------------
 * @Created 5/22/2025
 * @Project Chat Room Application
 * --------------------------------------------
 **/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatClientApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chat Room by KaveeN");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/ChatClientView.fxml"))));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}