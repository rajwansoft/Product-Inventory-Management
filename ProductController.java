package productinventorysystem1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class ProductController {

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;

    @FXML private DatePicker issueDatePicker;
    @FXML private DatePicker expiryDatePicker;

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idCol;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, Integer> quantityCol;
    @FXML private TableColumn<Product, Double> priceCol;
    @FXML private TableColumn<Product, String> issueCol;
    @FXML private TableColumn<Product, String> expiryCol;

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    private Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/pinventory", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
private void initialize() {
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    issueCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
    expiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

    loadProducts();

    // ✅ Auto-fill fields when a product is selected
    productTable.setRowFactory(tv -> {
        TableRow<Product> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (!row.isEmpty()) {
                Product selected = row.getItem();
                nameField.setText(selected.getName());
                quantityField.setText(String.valueOf(selected.getQuantity()));
                priceField.setText(String.valueOf(selected.getPrice()));

                try {
                    issueDatePicker.setValue(LocalDate.parse(selected.getIssueDate()));
                    expiryDatePicker.setValue(LocalDate.parse(selected.getExpiryDate()));
                } catch (Exception e) {
                    issueDatePicker.setValue(null);
                    expiryDatePicker.setValue(null);
                }
            }
        });
        return row;
    });
}


    private void loadProducts() {
        productList.clear();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                productList.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("issueDate"),
                        rs.getString("expiryDate")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        productTable.setItems(productList);
    }

    @FXML
    private void addProduct() {
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());
        String issueDate = issueDatePicker.getValue() != null ? issueDatePicker.getValue().toString() : "";
        String expiryDate = expiryDatePicker.getValue() != null ? expiryDatePicker.getValue().toString() : "";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (name, quantity, price, issueDate, expiryDate) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setString(4, issueDate);
            stmt.setString(5, expiryDate);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadProducts();
        clearFields();
    }

    @FXML
    private void updateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            String issueDate = issueDatePicker.getValue() != null ? issueDatePicker.getValue().toString() : "";
            String expiryDate = expiryDatePicker.getValue() != null ? expiryDatePicker.getValue().toString() : "";

            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE products SET name = ?, quantity = ?, price = ?, issueDate = ?, expiryDate = ? WHERE id = ?")) {
                stmt.setString(1, name);
                stmt.setInt(2, quantity);
                stmt.setDouble(3, price);
                stmt.setString(4, issueDate);
                stmt.setString(5, expiryDate);
                stmt.setInt(6, selected.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadProducts();
            clearFields();
        }
    }

    @FXML
    private void deleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE id = ?")) {
                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            loadProducts();
        }
    }

    @FXML
    private void clearFields() {
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        issueDatePicker.setValue(null);
        expiryDatePicker.setValue(null);
        productTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleLogout() {
        System.exit(0); // or redirect to login.fxml
    }

    // ✅ New method for "Go Back" button
    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
