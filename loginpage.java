import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginpage extends Application {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HOSPITALS";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mysql@123";

    public static void main(String[] args) {
        launch(args);
    }
    
    private boolean findLogin(String patientId, String password) {
        boolean loginSuccess = false;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM Patients WHERE patient_id = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(patientId));
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    loginSuccess = resultSet.next();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return loginSuccess;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        Text header = new Text("Login to your account");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        header.setFill(Color.WHITE);

        Text errorMessage = new Text();
        errorMessage.setFill(Color.WHITE);

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Patient ID");
        patientIdField.setMinSize(150, 25);
        patientIdField.setMaxSize(150, 25);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMinSize(150, 25);
        passwordField.setMaxSize(150, 25);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4C7A34;");
        loginButton.setOnAction(event -> {
            if (findLogin(patientIdField.getText(), passwordField.getText())) {
                errorMessage.setText("Correct username and password");
            } else {
                errorMessage.setText("Username/Password is entered incorrectly");
            }
        });

        Hyperlink signupLink = new Hyperlink("Don't have an account? Signup here.");
        signupLink.setOnAction(event -> {
            errorMessage.setText("Navigating to signup page");
            // Here you would navigate to the signup page
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(header, errorMessage, patientIdField, passwordField, loginButton, signupLink);
        layout.setStyle("-fx-background-color: #006E8C;");

        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.show();
    }
}
