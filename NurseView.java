import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;

public class NurseView extends Application {

    private TextArea healthIssuesTextArea;
    private TextArea medicationsTextArea;
    private TextArea immunizationHistoryTextArea;
    private TextField weightField;
    private TextField heightField;
    private TextField temperatureField;
    private TextField bloodPressureField;
    private Button saveButton;
    private TextField searchPatientField;

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Patient Information");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.BLACK);

        StackPane titlePane = new StackPane();
        titlePane.getChildren().add(titleLabel);

        Label historyLabel = new Label("Patient History:");
        historyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        historyLabel.setTextFill(Color.BLACK);

        Label healthIssuesLabel = createSubheadingLabel("Previous Health Issues:");
        Label medicationsLabel = createSubheadingLabel("Previously Prescribed Medications:");
        Label immunizationLabel = createSubheadingLabel("History of Immunization:");
        Label vitalsLabel = createSubheadingLabel("Patient Vitals:");
        vitalsLabel.setTextFill(Color.BLACK);

        healthIssuesTextArea = createEmptyTextArea();
        medicationsTextArea = createEmptyTextArea();
        immunizationHistoryTextArea = createEmptyTextArea();

        weightField = new TextField();
        weightField.setPromptText("Weight");

        heightField = new TextField();
        heightField.setPromptText("Height");

        temperatureField = new TextField();
        temperatureField.setPromptText("Body Temperature");

        bloodPressureField = new TextField();
        bloodPressureField.setPromptText("Blood Pressure");

        saveButton = new Button("Save");
        saveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        saveButton.setDefaultButton(true); 

        searchPatientField = new TextField();
        searchPatientField.setPromptText("Search Patient");

        Button enterButton = new Button("Enter");
        enterButton.setStyle("-fx-font-size: 12px; -fx-padding: 5px 10px;");
        enterButton.setOnAction(event -> {
            String patientId = searchPatientField.getText();
            if (patientIdExists(patientId)) {
                fetchDataAndPopulateFields(patientId);
                enableFields(true);
            } else {
                showAlert("Patient doesn't exist");
                enableFields(false);
            }
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton);
        buttonBox.setPadding(new Insets(10, 0, 20, 0));
        buttonBox.setAlignment(Pos.BASELINE_RIGHT);

        saveButton.setOnAction(event -> saveNurseData(
        	Integer.parseInt(searchPatientField.getText()),
            healthIssuesTextArea.getText(),
            medicationsTextArea.getText(),
            immunizationHistoryTextArea.getText(),
            weightField.getText(),
            heightField.getText(),
            bloodPressureField.getText(),
            temperatureField.getText()
        ));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.addRow(0, titlePane);
        gridPane.addRow(1, searchPatientField, enterButton);
        gridPane.addRow(2, historyLabel);
        gridPane.addRow(3, healthIssuesLabel, medicationsLabel);
        gridPane.addRow(4, healthIssuesTextArea, medicationsTextArea);
        gridPane.addRow(5, immunizationLabel, new Label());
        gridPane.addRow(6, immunizationHistoryTextArea, new Label());
        gridPane.addRow(7, vitalsLabel, new Label());
        gridPane.addRow(8, createLabeledBox("Weight:", weightField), createLabeledBox("Height:", heightField));
        gridPane.addRow(9, createLabeledBox("Body Temperature:", temperatureField),
                createLabeledBox("Blood Pressure:", bloodPressureField));

        gridPane.setStyle("-fx-background-color: #006e8c;");

       
        GridPane.setColumnSpan(titlePane, 2);

        BorderPane root = new BorderPane();
        root.setCenter(gridPane);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Nurse View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Label createSubheadingLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, Font.getDefault().getSize()));
        label.setTextFill(Color.WHITE);
        return label;
    }

    private TextArea createEmptyTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(4);
        return textArea;
    }

    private HBox createLabeledBox(String label, TextField field) {
        Label nameLabel = new Label(label);
        nameLabel.setTextFill(Color.WHITE);
        HBox box = new HBox(10);
        box.getChildren().addAll(nameLabel, field);
        return box;
    }

    private Connection connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/HOSPITALS";
        String user = "root";
        String password = "Mysql@123";
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fetchDataAndPopulateFields(String patientId) {
        String query = "SELECT prevhealth, prev_presc, immun FROM Uchiha WHERE patient_id = ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(patientId));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                healthIssuesTextArea.setText(rs.getString("prevhealth"));
                medicationsTextArea.setText(rs.getString("prev_presc"));
                immunizationHistoryTextArea.setText(rs.getString("immun"));
            } else {
                healthIssuesTextArea.clear();
                medicationsTextArea.clear();
                immunizationHistoryTextArea.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error retrieving data");
        }
    }
    
    private boolean patientIdExists(String patientId) {
        String query = "SELECT COUNT(*) FROM Patients WHERE patient_id = ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(patientId));
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void enableFields(boolean enable) {
        healthIssuesTextArea.setDisable(!enable);
        medicationsTextArea.setDisable(!enable);
        immunizationHistoryTextArea.setDisable(!enable);
        weightField.setDisable(!enable);
        heightField.setDisable(!enable);
        temperatureField.setDisable(!enable);
        bloodPressureField.setDisable(!enable);
        saveButton.setDisable(!enable);
    }

    private void saveNurseData(int patientId, String prevHealth, String prevPresc,
            String immun, String weight, String height,
            String bloodPressure, String bodyTemperature) {
String query = "INSERT INTO Uchiha(patient_id, prevhealth, prev_presc, immun, weight, height, Blood_pressure, body_temperature) " +
    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
    "ON DUPLICATE KEY UPDATE prevhealth=?, prev_presc=?, immun=?, weight=?, height=?, Blood_pressure=?, body_temperature=?";
try (Connection conn = connectToDatabase();
PreparedStatement stmt = conn.prepareStatement(query)) {
stmt.setInt(1, patientId);
stmt.setString(2, prevHealth);
stmt.setString(3, prevPresc);
stmt.setString(4, immun);
stmt.setString(5, weight);
stmt.setString(6, height);
stmt.setString(7, bloodPressure);
stmt.setString(8, bodyTemperature);

stmt.setString(9, prevHealth);
stmt.setString(10, prevPresc);
stmt.setString(11, immun);
stmt.setString(12, weight);
stmt.setString(13, height);
stmt.setString(14, bloodPressure);
stmt.setString(15, bodyTemperature);

stmt.executeUpdate();
showAlert("Data saved successfully");
} catch (SQLException e) {
e.printStackTrace();
showAlert("Error saving data");
}
}


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
