package lk.ijse.edu.chatroomapplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * --------------------------------------------
 * @Author Dimantha Kaveen
 * @GitHub: https://github.com/KaveenDK
 * --------------------------------------------
 * @Created 5/22/2025
 * @Project Chat Room Application
 * --------------------------------------------
 **/

public class ChatClient {
    BufferedReader in;
    PrintWriter out;

    JFrame frame = new JFrame("Chat Room by KaveeN");
    JTextField textField = new JTextField(40);
    JTextArea textArea = new JTextArea(8, 40);

    public ChatClient() {
        textField.setEditable(false);
        textArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(textArea), "Center");
        frame.pack();

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(frame, "Enter IP Address of the Server:", "Welcome to Chat Room by KaveeN", JOptionPane.QUESTION_MESSAGE);
    }

    private String getName() {
        return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen Name Selection", JOptionPane.PLAIN_MESSAGE);
    }

    private void run() throws Exception {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 8080);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String serverMessage = in.readLine();
            if (serverMessage == null) break;
            if (serverMessage.startsWith("SUBMIT NAME")) {
                out.println(getName());
            } else if (serverMessage.startsWith("NAME ACCEPTED")) {
                textField.setEditable(true);
            } else {
                textArea.append(serverMessage + "\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
