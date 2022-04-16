/*
 * Author: Hanyi Gao <hanyig1@student.unimelb.edu.au>
 * Student ID: 1236476
 */

package client;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JLabel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class ClientController {
	
	private class ClientThread extends Thread {
		
		private JSONObject json;
		
		public ClientThread(JSONObject json) {
			this.json = json;
		}
		
		@Override
		public void run() {
			sendOperationToServer(json);
		}
	}
	
	private int port;
	private String serverAddress;
	private GUI gui;
	
	public ClientController(int port, String serverAddress) {
		this.port = port;
		this.serverAddress = serverAddress;
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
	
	private boolean isEmpty(String str) {
		return str.trim().equals("");
	}
	
	private void handleInputError(String msg) {
		JLabel lblMessage = gui.getLblMessage();
		lblMessage.setForeground(Color.RED);
		lblMessage.setText(msg);
		System.out.println(msg);
	}

	private boolean isWordValid(String word) {
		if (isEmpty(word)) {
			handleInputError("the word is empty!");
			return false;
		}
		return true;
	}
	
	private boolean isMeaningValid(String meaning) {
		if (isEmpty(meaning)) {
			handleInputError("the meaning is empty!");
			return false;
		}
		return true;
	}
	
	public void add(String word, String meaning) {
		if (isWordValid(word) && isMeaningValid(meaning))
			execute(word, meaning, ClientCommand.ADD);
	}
	
	public void query(String word) {
		if (isWordValid(word))
			execute(word, "", ClientCommand.QUERY);
	}
	
	public void remove(String word) {
		if (isWordValid(word))
			execute(word, "", ClientCommand.REMOVE);
	}
	
	public void update(String word, String meaning) {
		if (isWordValid(word) && isMeaningValid(meaning))
			execute(word, meaning, ClientCommand.UPDATE);
	}
	
	private void execute(String word, String meaning, String command) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("word", word);
		map.put("meaning", meaning);
		map.put("command", command);
		JSONObject json = new JSONObject(map);
		
		new ClientThread(json).start();
	}
	
	// Handles response from the server and displays relevant information on GUI window.
	private void handleResponse(String response) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response);
			
			String meaning = (String) json.get("ret");
			String msg = (String) json.get("msg");
			boolean isSuccess = (Boolean) json.get("success");
			
			gui.getTextArea().setText(meaning);
			
			JLabel lblMessage = gui.getLblMessage();
			
			lblMessage.setText(msg);
			if (isSuccess)
				lblMessage.setForeground(new Color(0, 100, 0)); // Green
			else
				lblMessage.setForeground(Color.RED);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void handleException(String msg) {
		System.out.println(msg);
		JLabel lblMessage = gui.getLblMessage();
		lblMessage.setForeground(Color.RED);
		lblMessage.setText(msg);
	}
	
	private void sendOperationToServer(JSONObject json) {
		Socket socket = null;
		try {
			socket = new Socket(serverAddress, port);
			
			DataInputStream in = new DataInputStream(socket.getInputStream());
		    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
		    out.writeUTF(json.toJSONString());
			out.flush();
			
			String response = in.readUTF();
		
			System.out.println("return from server: " + response);
			handleResponse(response);
			
			in.close();
			out.close();
		} catch (UnknownHostException e) {
			handleException("Error occurs. Could not find the server IP address!");
		} catch (IllegalArgumentException e) {
			handleException("Error occurs. The port value is invalid!");
		} catch (IOException e) {
			handleException("Error occurs. I/O error!");
		} catch (Exception e) {
			handleException("Error occurs. Unknown error!");
		}
		finally
		{
			// Close the socket
			if (socket != null)
			{
				try
				{
					socket.close();
				}
				catch (IOException e) 
				{
					handleException("Error occurs. I/O error!");
				}
			}
		}
	}
}
