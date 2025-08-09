package productinventorysystem1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class LoginController {
    public static String loggedInUserRole;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
               LoginController.loggedInUserRole = rs.getString("role");

                FXMLLoader loader;
                if ("employee".equals("role")) {
                    loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                }
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
            } else {
                messageLabel.setText("Invalid username or password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error connecting to database.");
        }
    }

    @FXML
    private void handleGoToSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}