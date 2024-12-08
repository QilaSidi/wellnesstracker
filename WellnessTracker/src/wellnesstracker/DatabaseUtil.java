/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wellnesstracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/wellness_tracker?useSSL=false&serverTimezone=UTC"; // Change to your DB URL
    private static final String DB_USER = "root"; // Your MySQL username (default is 'root')
    private static final String DB_PASSWORD = "Aqil2612#"; // Your MySQL password (leave blank if no password)

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        try {
            // Register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish and return the connection
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC driver not found.");
        }
    }
}
