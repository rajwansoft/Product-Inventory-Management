package productinventorysystem1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class DashboardController {
    @FXML private Button productBtn;
    @FXML private Button customerBtn;
    @FXML private Button logoutBtn;

    @FXML
private void handleProductBtn(ActionEvent event) {
    try {
        System.out.println("Logged in role: " + LoginController.loggedInUserRole);

        // Allow only employee
        if ("employee".equalsIgnoreCase(LoginController.loggedInUserRole.trim())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("productView.fxml"));
            Stage stage = (Stage) productBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Access Denied");
            alert.setHeaderText(null);
            alert.setContentText("Only employees can manage products.");
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @FXML
    private void handleCustomerBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customerView.fxml"));
            Stage stage = (Stage) customerBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
