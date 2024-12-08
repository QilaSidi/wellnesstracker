package wellnesstracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainDashboardController {

    // Handle Mood Tracker button click
    @FXML
    private void handleMoodTracker(ActionEvent event) {
        loadNewWindow("MoodTracker.fxml", "Mood Tracker", event);
    }
    
    // Handle Dietary Intake button click
    @FXML
    private void handleDietaryIntake(ActionEvent event) {
        loadNewWindow("DietaryIntake.fxml", "Dietary Intake", event);
    }

    // Handle Exercise Log button click
    @FXML
    private void handleExerciseLog(ActionEvent event) {
        loadNewWindow("ExerciseLog.fxml", "Exercise Log", event);
    }

    // Handle Sleep Tracking button click
    @FXML
    private void handleSleepTracking(ActionEvent event) {
        loadNewWindow("SleepTracking.fxml", "Sleep Tracking", event);
    }

    // Utility method to load a new FXML file as a new window or switch scene
    private void loadNewWindow(String fxmlFile, String title, ActionEvent event) {
        try {
            // Print the URL to debug where it's looking for the FXML file
            System.out.println("FXML URL: " + getClass().getResource(fxmlFile));
            
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage (main window) from the event source
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            
            // Set the new scene and show the window
            currentStage.setScene(scene);
            currentStage.setTitle(title);
            currentStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
