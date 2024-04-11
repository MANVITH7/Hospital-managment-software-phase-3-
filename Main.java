package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

	public void start(Stage primaryStage) {
		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);

		Label prescribeLabel = new Label("Prescribe:");
		prescribeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		vbox.getChildren().add(prescribeLabel);

		TextArea prescribeTextArea = new TextArea();
		prescribeTextArea.setPromptText("Prescribe");
		prescribeTextArea.setPrefRowCount(6);
		prescribeTextArea.setMaxWidth(300);
		vbox.getChildren().add(prescribeTextArea);

		Label diagnosisLabel = new Label("Diagnosis:");
		diagnosisLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		vbox.getChildren().add(diagnosisLabel);

		TextArea diagnosisTextArea = new TextArea();
		diagnosisTextArea.setPromptText("Diagnosis");
		diagnosisTextArea.setPrefRowCount(6);
		diagnosisTextArea.setMaxWidth(300);
		vbox.getChildren().add(diagnosisTextArea);

		Button sendButton = new Button("Send");
		sendButton.setOnAction(event -> {
			System.out.println("Send button clicked!");
		});

		StackPane bottomRightPane = new StackPane();
		bottomRightPane.getChildren().add(sendButton);
		StackPane.setAlignment(sendButton, Pos.BOTTOM_RIGHT);

		vbox.setPadding(new Insets(20));
		vbox.setBackground(new Background(new BackgroundFill(Color.web("#006e8c"), CornerRadii.EMPTY, Insets.EMPTY)));

		StackPane root = new StackPane();
		root.getChildren().addAll(vbox, bottomRightPane);

		Scene scene = new Scene(root, 500, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Medical Application");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
