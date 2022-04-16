/*
 * Author: Hanyi Gao <hanyig1@student.unimelb.edu.au>
 * Student ID: 1236476
 */

package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import client.ClientCommand;

public class ServerThread extends Thread{

	// some messages that will be sent back to the client
	private final static String WORD_NOT_FOUND_MSG = "Word not found in the dictionary. Try to add the word.";
	private final static String WORD_COLLISON_MSG = "Word is already in the dictionary. Try to add a new word.";
	private final static String SERVER_ERROR_MSG = "An error occurs in the server side.";
	
	private Socket clientSocket;
	private Dictionary dict;
	private String msg = "success";
	private String ret = "";
	private boolean success = true;
	
	public ServerThread(Socket clientSocket, Dictionary dict) {
		this.clientSocket = clientSocket;
		this.dict = dict;
	}
	
	private void dictOperate(JSONObject request) {
		String command = (String) request.get("command");
		
		switch (command) {
		case ClientCommand.QUERY:
			handleQuery(request);
			break;
		case ClientCommand.ADD:
			handleAdd(request);
			break;
		case ClientCommand.REMOVE:
			handleRemove(request);
			break;
		case ClientCommand.UPDATE:
			handleUpdate(request);
			break;
		default:
			break;
			
		}
	}
	
	private void handleQuery(JSONObject request) {
		String word = (String) request.get("word");
		String meaning = dict.query(word);
		if (meaning.equals("")) {
			msg = WORD_NOT_FOUND_MSG;
			success = false;
		}else {
			ret = meaning;
		}
	}
	
	private void handleAdd(JSONObject request) {
		String word = (String) request.get("word");
		String meaning =  (String) request.get("meaning");
		try {
			if (!dict.add(word, meaning)) {
				msg = WORD_COLLISON_MSG;
				success = false;
			} 
		} catch (IOException e) {
			msg = SERVER_ERROR_MSG;
			success = false;
			e.printStackTrace();
		}
	}
	
	private void handleRemove(JSONObject request) {
		String word = (String) request.get("word");
		try {
			if (!dict.remove(word)) {
				msg = WORD_NOT_FOUND_MSG;
				success = false;
			}
		} catch (IOException e) {
			msg = SERVER_ERROR_MSG;
			success = false;
			e.printStackTrace();
		}
	}
	
	private void handleUpdate(JSONObject request) {
		String word = (String) request.get("word");
		String meaning =  (String) request.get("meaning");
		try {
			if (!dict.update(word, meaning)) {
				msg =  WORD_NOT_FOUND_MSG;
				success = false;
			} 
		} catch (IOException e) {
			msg = SERVER_ERROR_MSG;
			success = false;
			e.printStackTrace();
		}
	}
	
	private JSONObject createResponse() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("msg", msg);
		map.put("ret", ret);
		map.put("success", success);
		return new JSONObject(map);
	}
	
	
	@Override
	public void run() {
		try {
			JSONParser parser = new JSONParser();
			
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
		    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
			
			System.out.println("Local Port: " + clientSocket.getLocalPort());
			JSONObject request = (JSONObject) parser.parse(in.readUTF());
			
			dictOperate(request);
			
			out.writeUTF(createResponse().toJSONString());
			out.flush();
			
			in.close();
			out.close();
			clientSocket.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
