package lk.ijse.edu.chatroomapplication;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * --------------------------------------------
 * @Author Dimantha Kaveen
 * @GitHub: https://github.com/KaveenDK
 * --------------------------------------------
 * @Created 5/22/2025
 * @Project Chat Room Application
 * --------------------------------------------
 **/

public class ChatServer {

    private static final int PORT = 8080;
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    // Swing UI components
    private static DefaultListModel<String> clientListModel = new DefaultListModel<>();
    private static JTextArea logArea = new JTextArea(10, 30);

    public static void main(String[] args) throws Exception {
        // Setup UI
        JFrame frame = new JFrame("Chat Server - Connected Clients");
        JList<String> clientList = new JList<>(clientListModel);
        logArea.setEditable(false);
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(clientList), BorderLayout.CENTER);
        frame.add(new JScrollPane(logArea), BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        log("Chat Server is running...");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                Socket socket = listener.accept();
                Thread handlerThread = new Thread(new Handler(socket));
                handlerThread.start();
            }
        } finally {
            listener.close();
        }
    }

    private static void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMIT NAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            SwingUtilities.invokeLater(() -> clientListModel.addElement(name));
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
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println(name + ": " + input);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (name != null) {
                    synchronized (names) {
                        names.remove(name);
                        SwingUtilities.invokeLater(() -> clientListModel.removeElement(name));
                    }
                    log(name + " has left the chat.");
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (PrintWriter writer : writers) {
                    writer.println(name + " has left the chat.");
                }
            }
        }
    }
}