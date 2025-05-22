package lk.ijse.edu.chatroomapplication.Controller;

/**
 * --------------------------------------------
 * @Author Dimantha Kaveen
 * @GitHub: https://github.com/KaveenDK
 * --------------------------------------------
 * @Created 5/22/2025
 * @Project Chat Room Application
 * --------------------------------------------
 **/

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.Socket;

public class ChatClientController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;

    private BufferedReader in;
    private PrintWriter out;

    @FXML
    public void initialize() {
        chatArea.setEditable(false);
        inputField.setDisable(true);
        sendButton.setDisable(true);

        sendButton.setOnAction(e -> sendMessage());
        inputField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) sendMessage();
        });

        new Thread(this::runClient).start();
    }

    private void sendMessage() {
        String msg = inputField.getText();
        if (!msg.isEmpty()) {
            out.println(msg);
            inputField.clear();
        }
    }

    private void runClient() {
        try {
            String serverAddress = prompt("Enter IP Address of the Server:");
            Socket socket = new Socket(serverAddress, 8080);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String serverMessage = in.readLine();
                if (serverMessage == null) break;
                if (serverMessage.startsWith("SUBMIT NAME")) {
                    out.println(prompt("Choose a screen name:"));
                } else if (serverMessage.startsWith("NAME ACCEPTED")) {
                    Platform.runLater(() -> {
                        inputField.setDisable(false);
                        sendButton.setDisable(false);
                        inputField.requestFocus();
                    });
                } else {
                    Platform.runLater(() -> chatArea.appendText(serverMessage + "\n"));
                }
            }
        } catch (Exception e) {
            Platform.runLater(() -> chatArea.appendText("Connection error: " + e.getMessage() + "\n"));
        }
    }

    private String prompt(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Chat Room by KaveeN");
        dialog.setHeaderText(message);
        dialog.setContentText(null);
        return dialog.showAndWait().orElse("");
    }
}