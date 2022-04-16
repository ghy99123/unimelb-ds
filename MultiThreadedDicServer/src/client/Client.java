/*
 * Author: Hanyi Gao <hanyig1@student.unimelb.edu.au>
 * Student ID: 1236476
 */

package client;

public class Client {
	
	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[1]);
			String address = args[0];

			ClientController clientController = new ClientController(port, address);
			GUI window = new GUI(clientController);
			clientController.setGUI(window);
			window.frame.setVisible(true);
			
		}catch(Exception e) {
			System.out.println("Wrong format of the input from the console! Please run the program again!");
		}
	}

}
