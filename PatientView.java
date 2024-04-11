package HospitalPortal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientView extends Application {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HOSPITALS";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mysql@123";

    @Override
    public void start(Stage window) {
        Label titleLabel = new Label("Patient Information");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.BLACK);

        StackPane titlePane = new StackPane();
        titlePane.getChildren().add(titleLabel);

        Label historyLabel = new Label("Patient History:");
        historyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        historyLabel.setTextFill(Color.BLACK);

        TextArea healthIssuesTextArea = createEmptyTextArea();
        TextArea medicationsTextArea = createEmptyTextArea();
        TextArea immunizationHistoryTextArea = createEmptyTextArea();
        TextArea diagnosisTextArea = createEmptyTextArea();

        TextField weightField = new TextField();
        TextField heightField = new TextField();
        TextField temperatureField = new TextField();
        TextField bloodPressureField = new TextField();

        Button enterButton = new Button("Enter");
        TextField searchPatientField = new TextField();

        enterButton.setOnAction(event -> {
            String patientId = searchPatientField.getText();
            if (patientId.isEmpty()) {
                showPopup("Please enter a Patient ID.", window);
                return;
            }

            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                if (!patientExists(connection, patientId)) {
                    showPopup("Patient ID not found.", window);
                    return;
                }

                fillPatientHistory(connection, patientId, healthIssuesTextArea, immunizationHistoryTextArea);
                fillPatientPrescriptions(connection, patientId, medicationsTextArea, diagnosisTextArea);
                fillPatientVitals(connection, patientId, weightField, heightField, temperatureField, bloodPressureField);
            } catch (SQLException e) {
                e.printStackTrace();
                showPopup("Failed to retrieve patient data.", window);
            }
        });
        
        Button messageDoctorButton = new Button("Message Doctor");

        HBox searchBox = new HBox(10, searchPatientField, enterButton);
        searchBox.setAlignment(Pos.CENTER);

        HBox bottomRightBox = new HBox(messageDoctorButton);
        bottomRightBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomRightBox.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #006e8c;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.add(titlePane, 0, 0, 2, 1);
        gridPane.add(searchBox, 0, 1, 2, 1);
        gridPane.add(historyLabel, 0, 2, 2, 1);
        gridPane.add(new Label("Previous Health Issues:"), 0, 3);
        gridPane.add(new Label("Previously Prescribed Medications:"), 1, 3);
        gridPane.add(healthIssuesTextArea, 0, 4);
        gridPane.add(medicationsTextArea, 1, 4);
        gridPane.add(new Label("History of Immunization:"), 0, 5);
        gridPane.add(new Label("Diagnosis:"), 1, 5);
        gridPane.add(immunizationHistoryTextArea, 0, 6);
        gridPane.add(diagnosisTextArea, 1, 6);
        gridPane.add(new Label("Patient Vitals:"), 0, 7, 2, 1);
        gridPane.add(createLabeledBox("Weight:", weightField), 0, 8);
        gridPane.add(createLabeledBox("Height:", heightField), 1, 8);
        gridPane.add(createLabeledBox("Body Temperature:", temperatureField), 0, 9);
        gridPane.add(createLabeledBox("Blood Pressure:", bloodPressureField), 1, 9);
        gridPane.add(bottomRightBox, 1, 10); // Add the messageDoctorButton to the layout

        BorderPane root = new BorderPane();
        root.setCenter(gridPane);

        Scene scene = new Scene(root, 700, 600);
        window.setTitle("Patient View");
        window.setScene(scene);
        
        // added at the end to include scene
        messageDoctorButton.setOnAction(event -> {
            MessagePortal message = new MessagePortal();
            if (searchPatientField.getText().isEmpty()) {
                showPopup("Please enter a Patient ID.", window);
                return;
            }
            window.setScene(message.openScene(window, scene, false, searchPatientField.getText()));
        });
        
        window.show();
    }

    private boolean patientExists(Connection connection, String patientId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Patients WHERE patient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(patientId));
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void fillPatientHistory(Connection connection, String patientId, TextArea healthIssuesTextArea, TextArea immunizationTextArea) throws SQLException {
        String sql = "SELECT prevhealth, immun FROM Uchiha WHERE patient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(patientId));
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    healthIssuesTextArea.setText(resultSet.getString("prevhealth"));
                    immunizationTextArea.setText(resultSet.getString("immun"));
                }
            }
        }
    }

    private void fillPatientPrescriptions(Connection connection, String patientId, TextArea medicationsTextArea, TextArea diagnosisTextArea) throws SQLException {
        String sql = "SELECT diagnosis, prescription FROM Prescriptions WHERE patient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(patientId));
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    diagnosisTextArea.setText(resultSet.getString("diagnosis"));
                    medicationsTextArea.setText(resultSet.getString("prescription"));
                }
            }
        }
    }

    private void fillPatientVitals(Connection connection, String patientId, TextField weightField, TextField heightField, TextField temperatureField, TextField bloodPressureField) throws SQLException {
        String sql = "SELECT weight, height, body_temperature, Blood_pressure FROM Uchiha WHERE patient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(patientId));
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    weightField.setText(resultSet.getString("weight"));
                    heightField.setText(resultSet.getString("height"));
                    temperatureField.setText(resultSet.getString("body_temperature"));
                    bloodPressureField.setText(resultSet.getString("Blood_pressure"));
                }
            }
        }
    }

    private TextArea createEmptyTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(4);
        return textArea;
    }

    private HBox createLabeledBox(String labelText, TextField field) {
        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE);
        HBox box = new HBox(10);
        box.getChildren().addAll(label, field);
        return box;
    }

    private void showPopup(String message, Stage owner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(owner);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
