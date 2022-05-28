package client;

import java.rmi.RemoteException;

public class JoinWhiteBoard {

	public static void main(String[] args) {
		
		try {
			String userName;
			ClientController clientController;
			if (args.length == 1) {
				userName = args[0]; 
				clientController = new ClientController(userName);
			}else {
				String host = args[0];
				int port = Integer.parseInt(args[1]);
				userName = args[2];
				
				clientController = new ClientController(userName, host, port);
			}
			
			clientController.setManager(false);
			
			if (!clientController.join()) {
				System.exit(0);
			} 
			
		} catch(NumberFormatException e) {
			System.out.println("You sholud enter an integer for the port. Please run the server again.");
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("The number of arguments should be one or three.");
		} catch(RemoteException e) {
			System.out.println("Remote error occurs. The server might have some problems.");
		}
	}

}
