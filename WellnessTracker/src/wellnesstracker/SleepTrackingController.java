package wellnesstracker;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class SleepTrackingController {

    @FXML
    private TextField txtDuration;
    @FXML
    private ComboBox<String> cbQuality;  // ComboBox for Sleep Quality
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea txtComments;

    // Initialize method to populate the ComboBox with values
    @FXML
    private void initialize() {
        // Populate ComboBox with sleep quality options
        cbQuality.getItems().addAll("Good", "Average", "Poor");
    }

    // Method to handle Save Button click
    @FXML
    private void handleSaveSleep() {
        // Get the values entered by the user
        String durationText = txtDuration.getText();
        String quality = cbQuality.getValue();  // Get the selected value from ComboBox
        LocalDate date = datePicker.getValue();
        String comments = txtComments.getText();

        // Validate input
        if (durationText.isEmpty() || quality == null || date == null) {
            showAlert(Alert.AlertType.ERROR, "Form Incomplete", "Please fill in all required fields.");
            return;
        }

        try {
            // Parse the numeric field
            float duration = Float.parseFloat(durationText);

            // Save data to MySQL
            saveToDatabase(duration, quality, date, comments);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Sleep data saved successfully.");

            // Navigate back to the dashboard after saving
            loadDashboard();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for sleep duration.");
        }
    }

    // Save sleep data to MySQL database
    private void saveToDatabase(float duration, String quality, LocalDate date, String comments) {
        String query = "INSERT INTO sleep_tracking (sleep_duration, sleep_quality, sleep_date, comments) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setFloat(1, duration);
            pstmt.setString(2, quality);
            pstmt.setDate(3, java.sql.Date.valueOf(date));
            pstmt.setString(4, comments);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save sleep data: " + e.getMessage());
        }
    }

    // Navigate to the Main Dashboard after saving the data
    private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) txtDuration.getScene().getWindow();
            currentStage.close();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Main Dashboard");
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the dashboard.");
        }
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
