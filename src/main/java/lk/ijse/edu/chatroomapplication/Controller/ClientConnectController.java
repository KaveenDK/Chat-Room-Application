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

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.application.Platform;

public class ClientConnectController {
  @FXML
  private TextField serverField;
  @FXML
  private TextField nameField;
  @FXML
  private Button connectButton;
  @FXML
  private Label errorLabel;

  @FXML
  public void initialize() {
    connectButton.setOnAction(e -> connect());
  }

  private void connect() {
    String server = serverField.getText().trim();
    String name = nameField.getText().trim();
    if (server.isEmpty() || name.isEmpty()) {
      errorLabel.setText("Server and Name required!");
      errorLabel.setVisible(true);
      return;
    }
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ChatClientView.fxml"));
      Scene scene = new Scene(loader.load());
      ChatClientController controller = loader.getController();
      controller.initConnection(server, name);

      Stage stage = (Stage) connectButton.getScene().getWindow();
      Platform.runLater(() -> {
        stage.setScene(scene);
        stage.setTitle("Chat Room by KaveeN");
      });
    } catch (Exception ex) {
      errorLabel.setText("Failed to connect: " + ex.getMessage());
      errorLabel.setVisible(true);
    }
  }
}
