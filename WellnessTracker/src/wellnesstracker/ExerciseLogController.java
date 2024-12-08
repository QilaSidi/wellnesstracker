package wellnesstracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExerciseLogController {

    @FXML
    private TextField txtExerciseName;
    @FXML
    private TextField txtDuration;
    @FXML
    private TextField txtCaloriesBurned;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea txtComments;

    @FXML
    private void handleAddExercise(ActionEvent event) {
        // Get the values entered by the user
        String exerciseName = txtExerciseName.getText();
        String durationText = txtDuration.getText();
        String caloriesText = txtCaloriesBurned.getText();
        LocalDate date = datePicker.getValue();
        String comments = txtComments.getText();

        // Validate input
        if (exerciseName.isEmpty() || durationText.isEmpty() || caloriesText.isEmpty() || date == null) {
            showAlert(Alert.AlertType.ERROR, "Form Incomplete", "Please fill in all required fields.");
            return;
        }

        try {
            // Parse the numeric fields
            int duration = Integer.parseInt(durationText);
            int caloriesBurned = Integer.parseInt(caloriesText);

            // Save data to MySQL
            saveToDatabase(exerciseName, duration, caloriesBurned, date, comments);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Exercise log saved successfully.");

            // Navigate back to the dashboard after saving
            loadDashboard();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for duration and calories.");
        }
    }

    // Save exercise data to MySQL database
    private void saveToDatabase(String exerciseName, int duration, int caloriesBurned, LocalDate date, String comments) {
        String query = "INSERT INTO exercise_log (exercise_name, duration_minutes, calories_burned, exercise_date, comments) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, exerciseName);
            pstmt.setInt(2, duration);
            pstmt.setInt(3, caloriesBurned);
            pstmt.setDate(4, java.sql.Date.valueOf(date));  // Set the date for exercise
            pstmt.setString(5, comments);  // Optional comments

            // Execute the update query
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Show an error alert if saving to DB fails
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save exercise log: " + e.getMessage());
        }
    }

    // Load the Main Dashboard after saving the data
    private void loadDashboard() {
        try {
            // Load MainDashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Close the current window (Exercise Log form)
            Stage currentStage = (Stage) txtExerciseName.getScene().getWindow();
            currentStage.close();

            // Open Main Dashboard in a new window
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Main Dashboard");
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();

        } catch (Exception e) {
            // If an error occurs during navigation, show an alert
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
