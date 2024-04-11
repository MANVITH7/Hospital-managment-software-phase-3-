package HospitalPortal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyCode;

public class MessagePortal extends Application{
	public static void main(String[] args) {
		launch (args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setScene(openScene(primaryStage, new Scene(new VBox(), 200, 200), true, "25"));
		primaryStage.show();
	}
	
	String allMessages = "";
	public Scene openScene(Stage primaryStage, Scene previous, boolean isDoctor, String id) {
		String sender;
		String[] oldMessages = readFile(id + "Messages.txt");
		
		for (int i = 0; i < oldMessages.length; i++) {
			allMessages += oldMessages[i] + "\n";
		}
		
		if (isDoctor) {
			sender = "Doctor: ";
		} else {
			sender = "Patient: ";
		}
		
		TextArea messages = new TextArea(allMessages); 
		messages.setEditable(false);
		messages.setMaxWidth(600);
		
		TextArea sendMessage = new TextArea();
		sendMessage.setMaxWidth(400);
		sendMessage.setMaxHeight(25);
		sendMessage.setMinHeight(25);
		
		// When send is clicked, add new message to allMessages and set text
		// send will also write allMessages to txt file named from the patientID
		Button send = new Button("Send");
		
		// back button, go to previous scene
		Button back = new Button("Back");
		
		// Add event handler to handle Enter key press in sendMessage
        sendMessage.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Handle send action when Enter key is pressed
                allMessages += sender + sendMessage.getText();
                messages.setText(allMessages);
                sendMessage.clear();
                String[] fullString = allMessages.split("\n");
                writeFile(id + "Messages.txt", fullString);
            }
        });
		
		// send action
		send.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				// sendMessage text will be added to allMessage then cleared
				allMessages += sender + sendMessage.getText() + "\n";
				messages.setText(allMessages);
				sendMessage.clear(); 
				String[] fullString = allMessages.split("\n");
                writeFile(id + "Messages.txt", fullString);
			}
		});
		
		back.setOnAction(new EventHandler<>() {
			public void handle(ActionEvent event) {
				primaryStage.setScene(previous);
			}
		});
		
		HBox backBox = new HBox();
		backBox.setAlignment(Pos.TOP_LEFT);
		Insets padding = new Insets(20, 0, 0, 20);
		backBox.setPadding(padding);
		backBox.getChildren().add(back);
		
		HBox sendMessageBox = new HBox(10);
		sendMessageBox.setAlignment(Pos.TOP_CENTER);
		sendMessageBox.getChildren().addAll(sendMessage, send);
		sendMessageBox.maxWidthProperty().bind(messages.widthProperty());
		
		HBox messageBox = new HBox();
		messageBox.setAlignment(Pos.CENTER);
		messageBox.getChildren().add(messages);
		
		VBox total = new VBox(20);
		total.getChildren().addAll(backBox, messageBox, sendMessageBox);
		total.setAlignment(Pos.TOP_CENTER);
		total.setStyle("-fx-background-color: #006E8C;");
		
		return new Scene(total, 650, 400);
	
	}
	
	// reads from a file
    public String[] readFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        	int n = br.read();
        	String[] contents = new String[n];
        	br.readLine();
            for (int i = 0; i < contents.length; i++) {
            	contents[i] = br.readLine();
            }
            return contents;
        } 
        catch (IOException e) {
            return new String[0];
        }
    }
    
    // writes to a file
    public void writeFile(String fileName, String[] contents) {
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
    		bw.write(contents.length);
    		bw.newLine();
    		for (int i = 0; i < contents.length; i++) {
    			bw.write(contents[i]);
    			bw.newLine();
    		}
        } 
    	catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // gets a new id# for a new patient
    public String newID() {
    	String ID = "";
    	try (BufferedReader br = new BufferedReader(new FileReader("LastID.txt"))) {
    		int n = br.read();
    		n++;
    		ID += String.format("%05d", n);
    		try(BufferedWriter bw = new BufferedWriter(new FileWriter("LastID.txt"))) {
    			bw.write(n);
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	catch (IOException e) {
    		try(BufferedWriter bw = new BufferedWriter(new FileWriter("LastID.txt"))) {
    			bw.write(1);
    			ID = "00001";
    		}
    		catch (IOException r) {
    			r.printStackTrace();
    		}
    	}
    	return ID;
    }
}
