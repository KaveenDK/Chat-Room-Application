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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ChatServerController {

    private static final int PORT = 8080;
    private final HashSet<String> names = new HashSet<>();
    private final HashSet<PrintWriter> writers = new HashSet<>();
    private final ObservableList<String> clientNames = FXCollections.observableArrayList();

    @FXML
    private ListView<String> clientListView;
    @FXML
    private TextArea logArea;

    @FXML
    public void initialize() {
        clientListView.setItems(clientNames);
        log("Chat Server is running...");
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try (ServerSocket listener = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = listener.accept();
                new Thread(new Handler(socket)).start();
            }
        } catch (IOException e) {
            log("Server error: " + e.getMessage());
        }
    }

    private void log(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }

    private class Handler implements Runnable {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMIT NAME");
                    name = in.readLine();
                    if (name == null) return;
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            Platform.runLater(() -> clientNames.add(name));
                            break;
                        } else {
                            out.println("NAME ALREADY EXISTS");
                        }
                    }
                }
                out.println("NAME ACCEPTED");
                writers.add(out);
                log(name + " connected.");

                while (true) {
                    String input = in.readLine();
                    if (input == null) return;
                    for (PrintWriter writer : writers) {
                        writer.println(name + ": " + input);
                    }
                }
            } catch (IOException e) {
                log("Error: " + e.getMessage());
            } finally {
                if (name != null) {
                    synchronized (names) {
                        names.remove(name);
                        Platform.runLater(() -> clientNames.remove(name));
                    }
                    log(name + " has left the chat.");
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException ignored) {}
                for (PrintWriter writer : writers) {
                    writer.println(name + " has left the chat.");
                }
            }
        }
    }
}