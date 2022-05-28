package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRemoteWhiteBoard;

public class WhiteBoardServer{

	public static void main(String[] args) {
		try {
			
			IRemoteWhiteBoard remoteWb = new RemoteWhiteBoard();
			
			int port = Integer.parseInt(args[0]);
			 
			Registry registry = LocateRegistry.createRegistry(port);
			
			registry.bind("whiteboard", remoteWb);
			
			System.out.println("Server ready on port " + port);
			 
			
		} 
		catch(RemoteException e) {
			System.out.println("Error occurs. The registry could not be exported.");
			e.printStackTrace();
		} 
		catch (AlreadyBoundException e) {
			System.out.println("Error occurs. Alreadt bounded");
		} 
		catch(NumberFormatException e) {
			System.out.println("You sholud enter an integer for the port. Please run the server again.");
			
		}
	}

}
