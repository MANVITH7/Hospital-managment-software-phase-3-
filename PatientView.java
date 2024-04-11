package application;

import javafx.application.Application;
import javafx.geometry.Insets;
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

public class PatientView extends Application {

	public void start(Stage primaryStage) {
		Label historyLabel = new Label("Patient History:");
		historyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		historyLabel.setTextFill(Color.BLACK);

		Label healthIssuesLabel = createSubheadingLabel("Previous Health Issues:");
		Label medicationsLabel = createSubheadingLabel("Previously Prescribed Medications:");
		Label immunizationLabel = createSubheadingLabel("History of Immunization:");
		Label diagnosisLabel = createSubheadingLabel("Diagnosis:");
		Label vitalsLabel = createSubheadingLabel("Patient Vitals:");
		vitalsLabel.setTextFill(Color.BLACK);

		TextArea healthIssuesTextArea = createEmptyTextArea();
		TextArea medicationsTextArea = createEmptyTextArea();
		TextArea immunizationHistoryTextArea = createEmptyTextArea();
		TextArea diagnosisTextArea = createEmptyTextArea();

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

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));
		gridPane.addRow(0, historyLabel);
		gridPane.addRow(1, healthIssuesLabel, medicationsLabel);
		gridPane.addRow(2, healthIssuesTextArea, medicationsTextArea);
		gridPane.addRow(3, immunizationLabel, diagnosisLabel);
		gridPane.addRow(4, immunizationHistoryTextArea, diagnosisTextArea);

		gridPane.addRow(5, vitalsLabel, new Label());
		gridPane.addRow(6, createLabeledBox("Weight:", weightField), createLabeledBox("Height:", heightField));
		gridPane.addRow(7, createLabeledBox("Body Temperature:", temperatureField),
				createLabeledBox("Blood Pressure:", bloodPressureField));

		gridPane.setStyle("-fx-background-color: #006e8c;");

		Label titleLabel = new Label("Patient Information");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

		StackPane titlePane = new StackPane();
		titlePane.getChildren().add(titleLabel);

		BorderPane root = new BorderPane();
		root.setCenter(gridPane);
		root.setTop(titlePane);
		root.setBottom(messageDoctorButton);

		BorderPane.setMargin(messageDoctorButton, new Insets(10));
		BorderPane.setAlignment(messageDoctorButton, javafx.geometry.Pos.BOTTOM_RIGHT);

		BackgroundFill backgroundFill = new BackgroundFill(Color.web("#006e8c"), null, null);
		root.setBackground(new Background(backgroundFill));

		Scene scene = new Scene(root, 600, 500);
		primaryStage.setTitle("Patient View");
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
		HBox box = new HBox(10);
		box.getChildren().addAll(nameLabel, field);
		return box;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
