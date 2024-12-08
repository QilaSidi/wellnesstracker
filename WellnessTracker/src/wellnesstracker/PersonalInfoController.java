package wellnesstracker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PersonalInfoController {

    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtAge;
    @FXML private ComboBox<String> cbGender;

    @FXML
    public void initialize() {
        // Populate ComboBox with gender options
        cbGender.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    private void handleSaveUser() {
        String fullName = txtFullName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String ageText = txtAge.getText();
        String gender = cbGender.getValue();

        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Age: " + ageText);
        System.out.println("Gender: " + gender);

        // Validate inputs
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || ageText.isEmpty() || gender == null) {
            System.out.println("Please fill in all fields.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText); // Convert age to an integer
            boolean success = saveUserToDatabase(fullName, email, password, age, gender);

            if (success) {
                System.out.println("User saved successfully!");
                loadDashboard(); // Navigate to the Main Dashboard
            } else {
                System.out.println("Failed to save user.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid age entered.");
        }
    }

    private boolean saveUserToDatabase(String fullName, String email, String password, int age, String gender) {
        String query = "INSERT INTO users (full_name, email, password, age, gender) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, password); // Hash password before saving in real apps
            stmt.setInt(4, age);
            stmt.setString(5, gender);

            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Navigate to the Main Dashboard
    private void loadDashboard() {
        try {
            // Load MainDashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Close the current window (Personal Info form)
            Stage currentStage = (Stage) txtFullName.getScene().getWindow();
            currentStage.close();

            // Open the Main Dashboard in a new window
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Main Dashboard");
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
