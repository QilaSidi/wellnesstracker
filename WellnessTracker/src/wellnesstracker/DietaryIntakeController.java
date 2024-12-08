package wellnesstracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DietaryIntakeController {

    @FXML
    private TextField txtFoodName;
    @FXML
    private TextField txtCalories;
    @FXML
    private TextField txtProtein;
    @FXML
    private TextField txtCarbs;
    @FXML
    private TextField txtFats;

    @FXML
    private void handleAddFood(ActionEvent event) {
        // Get the values entered by the user
        String foodName = txtFoodName.getText();
        String caloriesText = txtCalories.getText();
        String proteinText = txtProtein.getText();
        String carbsText = txtCarbs.getText();
        String fatsText = txtFats.getText();

        // Validate input
        if (foodName.isEmpty() || caloriesText.isEmpty() || proteinText.isEmpty() ||
            carbsText.isEmpty() || fatsText.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Incomplete", "Please fill all fields.");
            return;
        }

        try {
            int calories = Integer.parseInt(caloriesText);
            double protein = Double.parseDouble(proteinText);
            double carbs = Double.parseDouble(carbsText);
            double fats = Double.parseDouble(fatsText);

            // Save data to the database
            saveToDatabase(foodName, calories, protein, carbs, fats);

            // Show success message
            showAlert(AlertType.INFORMATION, "Success", "Food entry added successfully.");

            // Navigate back to the dashboard
            loadDashboard();

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter valid numbers for calories, protein, carbs, and fats.");
        }
    }

    private void saveToDatabase(String foodName, int calories, double protein, double carbs, double fats) {
        String insertQuery = "INSERT INTO dietary_intake (food_name, calories, protein, carbs, fats) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, foodName);
            pstmt.setInt(2, calories);
            pstmt.setDouble(3, protein);
            pstmt.setDouble(4, carbs);
            pstmt.setDouble(5, fats);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to save data: " + e.getMessage());
        }
    }

    private void loadDashboard() {
        try {
            // Load the dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Close the current window
            Stage currentStage = (Stage) txtFoodName.getScene().getWindow();
            currentStage.close();

            // Open the dashboard in a new window
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Main Dashboard");
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Failed to load the dashboard.");
        }
    }
    @FXML
private void saveAddFood(ActionEvent event) {
    handleAddFood(event); // Call the actual logic
}


    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
