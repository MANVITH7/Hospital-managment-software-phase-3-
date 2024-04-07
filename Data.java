package HospitalPortal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Data {

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

