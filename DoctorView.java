package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class DoctorView extends Application {

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

		TextArea healthIssuesTextArea = createEmptyTextArea();
		TextArea medicationsTextArea = createEmptyTextArea();
		TextArea immunizationHistoryTextArea = createEmptyTextArea();

		TextField weightField = new TextField();
		weightField.setPromptText("Weight");

		TextField heightField = new TextField();
		heightField.setPromptText("Height");

		TextField temperatureField = new TextField();
		temperatureField.setPromptText("Body Temperature");

		TextField bloodPressureField = new TextField();
		bloodPressureField.setPromptText("Blood Pressure");

		Button messageDoctorButton = new Button("Message Doctor");
		messageDoctorButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");

		TextField searchPatientField = new TextField();
		searchPatientField.setPromptText("Search Patient");

		Button enterButton = new Button("Enter");
		enterButton.setStyle("-fx-font-size: 12px; -fx-padding: 5px 10px;");

		Button prescribeMedicationButton = new Button("Prescribe Medication");
		prescribeMedicationButton.setOnAction(event -> {
			Main main = new Main();
			Stage stage = new Stage();
			main.start(stage);
			primaryStage.close(); // Close the DoctorView stage
		});

		Button messagePatientButton = new Button("Message Patient");

		HBox buttonBox = new HBox(10);
		buttonBox.getChildren().addAll(prescribeMedicationButton, messagePatientButton); // Removed newExaminationButton
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
		primaryStage.setTitle("Doctor View");
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

	public static void main(String[] args) {
		launch(args);
	}
}
