/*
 * Author: Hanyi Gao <hanyig1@student.unimelb.edu.au>
 * Student ID: 1236476
 */

package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	
	private int port;
	private ServerSocket listeningSocket = null;
	private Dictionary dict;
	private ServerThread thread = null;
	
	public Server(int port, String path) {
		this.port = port;
		this.dict = new Dictionary(path);
	}
	
	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			String path = args[1];
			if (port < 1024 || port > 65535) {
				System.out.println("Port number should between 1024 and 65535");
				System.exit(-1);
			}
			
			Server server = new Server(port, path);
			server.run();
		}catch(Exception e) {
			System.out.println("error");
		}	
	}
	
	private void run() {
		try {
			System.out.println("server start");
			listeningSocket = new ServerSocket(this.port);
			while (true) {
				Socket clientSocket = listeningSocket.accept();
				System.out.println("new requset is coming...");
				thread = new ServerThread(clientSocket, dict);
				thread.start();
				
			}
		} 
		catch (BindException e) {
			System.out.println("Port has already being used! Try another port or kill the process.");
		}
		catch (IOException e) {
			System.out.println("Error occurs. I/O error!");
			e.printStackTrace();
		}
		finally
		{
			if(listeningSocket != null)
			{
				try
				{
					listeningSocket.close();
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

}
