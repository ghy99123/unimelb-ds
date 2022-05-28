package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import util.JoinMessage;

public interface IRemoteWhiteBoard extends Remote{
	
	// Join the white board and return whether the join is successful.
	public JoinMessage join(String userName, boolean isManager, IClient remoteClient) throws RemoteException;
	
	// synchronize one client's white board image to all the other clients.
	public void synchronizeImage(byte[] b, String userName) throws RemoteException;
	
	// Remove a user from the user list.
	public void clearUser(String userName) throws RemoteException;
	
	// Broadcast the message to all the clients.
	public void broadcast(String msg, String speaker) throws RemoteException;
	
	// Terminate the application when the manager leaves.
	public void terminate() throws RemoteException;
	
	// Kick a user out of the white board application.
	public void kickUser(String userName) throws RemoteException;
	
	// Check if a user is currently editing the white board.
	public boolean isUserExist(String userName) throws RemoteException;
	
	
}
