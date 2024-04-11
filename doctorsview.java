package HospitalPortal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

public class doctorsview extends Application {
	
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
	public void start(Stage window) {
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

		Button prescribeMedicationButton = new Button("Prescribe Medication");
		/* prescribeMedicationButton.setOnAction(event -> {
			try {
        		prescription patPage = new prescription();
                patPage.start(window); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
		}); */

		Button messagePatientButton = new Button("Message Patient");

		HBox buttonBox = new HBox(10);
		buttonBox.getChildren().addAll(prescribeMedicationButton, messagePatientButton);
		buttonBox.setPadding(new Insets(10, 0, 20, 0));
		buttonBox.setAlignment(Pos.BASELINE_RIGHT);

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

		GridPane.setHalignment(titlePane, javafx.geometry.HPos.CENTER);
		GridPane.setColumnSpan(titlePane, 2);

		BorderPane root = new BorderPane();
		root.setCenter(gridPane);
		root.setBottom(buttonBox);

		BackgroundFill backgroundFill = new BackgroundFill(Color.web("#006e8c"), null, null);
		root.setBackground(new Background(backgroundFill));

		Scene scene = new Scene(root, 700, 500);
		window.setTitle("Doctor View");
		window.setScene(scene);
		
		// added here to include scene
		messagePatientButton.setOnAction(event -> {
    		MessagePortal message = new MessagePortal();
            window.setScene(message.openScene(window, scene, true, searchPatientField.getText())); 
		}); 
		
		window.show();
	}

	private Label createSubheadingLabel(String text) {
		Label label = new Label(text);
		label.setFont(Font.font("Arial", FontWeight.BOLD, Font.getDefault().getSize()));
		label.setTextFill(Color.WHITE);
		return label;
	}
	
	private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
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
	    String query = "SELECT prevhealth, prev_presc, immun, weight, height, Blood_pressure, body_temperature FROM Uchiha WHERE patient_id = ?";
	    try (Connection conn = connectToDatabase();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, Integer.parseInt(patientId));
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            healthIssuesTextArea.setText(rs.getString("prevhealth"));
	            medicationsTextArea.setText(rs.getString("prev_presc"));
	            immunizationHistoryTextArea.setText(rs.getString("immun"));
	            weightField.setText(rs.getString("weight"));
	            heightField.setText(rs.getString("height"));
	            bloodPressureField.setText(rs.getString("Blood_pressure"));
	            temperatureField.setText(rs.getString("body_temperature"));
	        } else {
	            clearFields();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert("Error retrieving data");
	    }
	}
	
	private void clearFields() {
	    healthIssuesTextArea.clear();
	    medicationsTextArea.clear();
	    immunizationHistoryTextArea.clear();
	    weightField.clear();
	    heightField.clear();
	    bloodPressureField.clear();
	    temperatureField.clear();
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

	public static void main(String[] args) {
		launch(args);
	}
}