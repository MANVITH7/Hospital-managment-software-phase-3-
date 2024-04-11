import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class prescription extends Application {


    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HOSPITALS";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mysql@123";

    @Override
    public void start(Stage primaryStage) {
       
        TextField idTextField = new TextField();
        idTextField.setPromptText("Patient id");
        TextArea prescriptionTextArea = new TextArea();
        prescriptionTextArea.setPromptText("Diagnosis");
        TextArea diagnosisTextArea = new TextArea();
        diagnosisTextArea.setPromptText("Perscription");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            String patientId = idTextField.getText();
            String prescription = prescriptionTextArea.getText();
            String diagnosis = diagnosisTextArea.getText();

            
            if (isPatientExists(patientId)) {
             
                insertPrescription(patientId, prescription, diagnosis);
              
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Prescription and diagnosis added successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Patient ID not found.");
                alert.showAndWait();
            }
        });

        
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(idTextField, prescriptionTextArea, diagnosisTextArea, sendButton);

      
        vBox.setStyle("-fx-background-color: #006e8c;");

        
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);

        
        Scene scene = new Scene(borderPane, 400, 300);

       
        primaryStage.setScene(scene);
        primaryStage.setTitle("Medical Record");
        primaryStage.show();
    }

    private boolean isPatientExists(String patientId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Patients WHERE patient_id = ?")) {
            stmt.setInt(1, Integer.parseInt(patientId)); 
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertPrescription(String patientId, String prescription, String diagnosis) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Prescriptions (patient_id, diagnosis, prescription) VALUES (?, ?, ?)")) {
            stmt.setInt(1, Integer.parseInt(patientId)); 
            stmt.setString(2, diagnosis);
            stmt.setString(3, prescription);
            stmt.executeUpdate();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
