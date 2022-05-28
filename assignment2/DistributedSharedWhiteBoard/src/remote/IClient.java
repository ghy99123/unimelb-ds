package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import util.CloseType;


public interface IClient extends Remote{
	
	// Initialize the GUI window
	public void initGUI() throws RemoteException;
	
	// Update the user list on client's GUI.
	public void updateUserList(ArrayList<String> userList) throws RemoteException;
	
	// Notify the manager that someone wants to join and let the manager approve or disapprove the join request.
	public boolean ApproveJoin(String userName) throws RemoteException;
	
	// Display the message on chat window
	public void showChatMessage(String msg, String userName) throws RemoteException;
	
	// Close the GUI window.
	public void close(CloseType closeType) throws RemoteException;
	
	// Load the image from the server.
	public void loadImage(byte[] b) throws RemoteException;
	
}
