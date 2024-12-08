package wellnesstracker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MoodTrackerController {

    @FXML
    private TextArea commentTextArea;

    @FXML
    private ComboBox<String> moodComboBox;

    // Initialize method is called automatically after FXML is loaded
    @FXML
    private void initialize() {
        // Populate the ComboBox with mood options
        moodComboBox.getItems().addAll("Happy", "Sad", "Excited", "Tired", "Angry");
    }

    // Method to handle Save Button click
    @FXML
    private void handleSaveMood() {
        // Get the selected mood from ComboBox
        String selectedMood = moodComboBox.getValue();
        if (selectedMood == null || selectedMood.isEmpty()) {
            System.out.println("Please select a mood.");
            return;
        }

        // Get the comment from the TextArea
        String comment = commentTextArea.getText();

        // Ensure a comment is provided (optional validation)
        if (comment == null || comment.isEmpty()) {
            System.out.println("Please provide a comment.");
            return;
        }

        // Save the mood and comment to the database
        saveMoodToDatabase(selectedMood, comment);

        // After successfully saving, navigate to the main dashboard
        loadDashboard();
    }

    // Save mood entry to the database
    private void saveMoodToDatabase(String mood, String comment) {
        String insertQuery = "INSERT INTO mood_entries (mood, comment) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, mood);
            pstmt.setString(2, comment);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Error saving mood: " + e.getMessage());
        }
    }

    // Navigate to the Main Dashboard after saving the data
    private void loadDashboard() {
        try {
            // Load the MainDashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

             // Manually add the CSS stylesheet
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/wellnesstracker/moodtracker.css").toExternalForm()); // Adjust path here

        // Close the current window (Mood Tracker form)
        Stage currentStage = (Stage) moodComboBox.getScene().getWindow();
        currentStage.close();
        
          String cssPath = getClass().getResource("/wellnesstracker/moodtracker.css").toExternalForm();
        System.out.println("CSS Path: " + cssPath); // Debugging path

        scene.getStylesheets().add(cssPath);

              

            // Open the Main Dashboard in a new window with the CSS applied
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Main Dashboard");
            dashboardStage.setScene(scene);
            dashboardStage.show();
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Navigation Error", "Failed to load the dashboard.");
        }
    }

    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
