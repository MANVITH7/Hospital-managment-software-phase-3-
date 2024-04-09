import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class SignUpPage extends Application {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HOSPITALS";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mysql@123";

    @Override
    public void start(Stage window) {
        VBox mainContainer = new VBox(15);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(15));
        mainContainer.setStyle("-fx-background-color: #006e8c;");

        Text heading = new Text("Sign-Up Page");
        heading.setFont(Font.font("Verdana", 22));
        heading.setFill(Color.WHITE);

        Image image = new Image("https://res.cloudinary.com/dcqpetbru/image/upload/v1712444259/Screenshot_2024-04-06_at_3.56.57_PM_hslkhj.png");
        ImageView logo = new ImageView(image);
        logo.setFitWidth(100);
        logo.setFitHeight(100);

        GridPane formLayout = new GridPane();
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setVgap(30);
        formLayout.setHgap(35);
        formLayout.setPadding(new Insets(20));

        Label lblFirstName = new Label("First Name:");
        TextField txtFirstName = new TextField();
        txtFirstName.setPrefWidth(300);
        formLayout.add(lblFirstName, 0, 0);
        formLayout.add(txtFirstName, 1, 0);

        Label lblLastName = new Label("Last Name:");
        TextField txtLastName = new TextField();
        formLayout.add(lblLastName, 0, 1);
        formLayout.add(txtLastName, 1, 1);

        Label lblBirthDate = new Label("Birth Date:");
        TextField dateBirthDate = new TextField();
        formLayout.add(lblBirthDate, 0, 2);
        formLayout.add(dateBirthDate, 1, 2);

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        formLayout.add(lblEmail, 0, 3);
        formLayout.add(txtEmail, 1, 3);

        Label lblPassword = new Label("Password:");
        PasswordField pwdPassword = new PasswordField();
        formLayout.add(lblPassword, 0, 4);
        formLayout.add(pwdPassword, 1, 4);

        Color labelsColor = Color.WHITE;
        lblFirstName.setTextFill(labelsColor);
        lblLastName.setTextFill(labelsColor);
        lblBirthDate.setTextFill(labelsColor);
        lblEmail.setTextFill(labelsColor);
        lblPassword.setTextFill(labelsColor);

        Button btnRegister = new Button("Register");
        btnRegister.setFont(Font.font("Verdana", 14));
        GridPane.setConstraints(btnRegister, 1, 5);
        GridPane.setHalignment(btnRegister, HPos.RIGHT);
        formLayout.add(btnRegister, 1, 5);

        btnRegister.setOnAction(e -> {
            if (validateForm(txtEmail.getText(), txtFirstName.getText(), txtLastName.getText(), dateBirthDate.getText())) {
                int uniqueId = generateUniqueId();
                try {
                    insertDataIntoDatabase(uniqueId, txtFirstName.getText(), txtLastName.getText(), dateBirthDate.getText(), txtEmail.getText(), pwdPassword.getText());
                    showPopup("Signup successful with ID: " + uniqueId, window);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showPopup("Failed to store signup data.", window);
                }
            } else {
                showPopup("Invalid input detected.", window);
            }
        });

        mainContainer.getChildren().addAll(heading, logo, formLayout);

        Scene scene = new Scene(mainContainer, 420, 520);

        window.setTitle("Signup Interface");
        window.setScene(scene);
        window.show();
    }

    private boolean validateForm(String email, String firstName, String lastName, String dob) {
        return email.contains("@") && !firstName.trim().isEmpty() && !lastName.trim().isEmpty() && dob != null;
    }

    private int generateUniqueId() {
        return (ThreadLocalRandom.current().nextInt(100000, 999999 + 1));
    }

    private void insertDataIntoDatabase(int uniqueId, String firstName, String lastName, String dob, String email, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO Patients(patient_id, first_name, last_name, email, dob, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, uniqueId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, dob);
            pstmt.setString(6, password);
            pstmt.executeUpdate();
        }
    }


    private void showPopup(String message, Stage owner) {
        Stage popupWindow = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));

        Text messageText = new Text(message);
        Button btnClose = new Button("Close");
        btnClose.setOnAction(event -> popupWindow.close());

        layout.getChildren().addAll(messageText, btnClose);

        Scene scene = new Scene(layout, 350, 200);
        popupWindow.initOwner(owner);
        popupWindow.setTitle("Signup Status");
        popupWindow.setScene(scene);
        popupWindow.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
