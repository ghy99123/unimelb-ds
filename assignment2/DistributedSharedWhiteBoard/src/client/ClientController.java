package client;

import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import client.GUI.ClientGUI;
import remote.IClient; 
import remote.IRemoteWhiteBoard;
import util.CloseType;
import util.JoinMessage;

public class ClientController extends UnicastRemoteObject implements IClient{

	private ClientGUI gui;
	private String userName;
	private String host = "localhost";
	private int port = 1099;
	private boolean isManager = false;
	
	
	private IRemoteWhiteBoard remoteWb;
	
	public ClientController(String userName, String host, int port) throws RemoteException{
		this.userName = userName;
		this.host = host;
		this.port = port;
	}
	
	public ClientController(String userName) throws RemoteException{
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	
	private void printMsg(String msg) {
		if (gui == null) {
			JOptionPane.showMessageDialog(null, msg);
		} else {
			JOptionPane.showMessageDialog(gui.getFrame(), msg);
		}
	}
	
	public boolean join() {
		String msg;
		try {
			Registry registry = LocateRegistry.getRegistry(host, port);
			IRemoteWhiteBoard remoteWb = (IRemoteWhiteBoard) registry.lookup("whiteboard");
			this.remoteWb = remoteWb;
			JoinMessage joinMsg = this.remoteWb.join(userName, isManager, this);
			
			if (joinMsg == JoinMessage.SUCCESS) {
				msg = "You have joined the whiteboard!";
				printMsg(msg);
				return true;
			} 
			
			switch (joinMsg) {
			case REFUSED:
				msg = "Sorry, the manager rejected your join request.";
				printMsg(msg);
				break;
			case NO_MANAGER:
				msg = "Sorry, there is no manager right now. You can run CreateWhiteBoard command to become the manager!";
				printMsg(msg);
				break;
			case MANAGER_EXIST:
				msg = "Sorry, Only one manager is supported. You can only be the normal user right now!";
				printMsg(msg);
				break;
			case INVALID_NAME:
				msg = "The user name already exists. You should enter a new name to join the whiteboard!";
				printMsg(msg);
			default:
				msg = "You can not join the whiteboard!";
				printMsg(msg);
				break;
			}
			
		} catch (RemoteException e) {
			msg = "The RMI could not be found in the registry. Maybe the host or port is not correct.";
			printMsg(msg);
		} catch (NotBoundException e) {
			msg = "The RMI could not be found in the registry. Maybe you bind an incorrect name.";
			printMsg(msg);
		}
		return false;
	}
	
	
	// Synchronize the local white board drawing image to other users.
	public void synchronizeImage() {
		BufferedImage canvasImage = gui.getDa().getCanvasImage();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(canvasImage, "png", out);
		
			byte[] b = out.toByteArray();
	
			remoteWb.synchronizeImage(b, userName);
		} catch (RemoteException e) {
			printMsg("Remote error occurs. The server might have some problems");
		} catch (IOException e) {
			printMsg("I/O operation fails");
			e.printStackTrace();
		}
		
	}
	
	// Close the white board window.
	public void onCloseClick(WindowEvent windowEvent) {
		try {
			if (isManager) {
				remoteWb.terminate();
			} else {
				remoteWb.clearUser(userName);
			}
		} catch (RemoteException e) {
			printMsg("Remote error occurs. The server might have some problems");
			e.printStackTrace();
		}
		windowEvent.getWindow().dispose();
        System.exit(0);
	}
	
	// Send the chat message to the server and broadcast the message to other white board user.
	public void onSendClick(String content) {
		try {
			if (content == null || content.trim().equals(""))
				return;
			remoteWb.broadcast(content, userName);
		} catch (RemoteException e) {
			printMsg("Remote error occurs. The server might have some problems");
			e.printStackTrace();
		}
	}
	
	public void onClearClick() {
		gui.getDa().clearAll();
		synchronizeImage();
	}
	
	public void onKickOutClick() {
		String userName = JOptionPane.showInputDialog(gui.getFrame(), "Please enter the user name");
		if (userName == null) {
			return;
		}
		if (userName.equals(this.userName)) {
			printMsg("You can not kick yourself!");
			return;
		}
		try {
			boolean flag = remoteWb.isUserExist(userName);
			System.out.println(userName);
			System.out.println(flag);
			if (!flag) {
				printMsg("The user does not exist!");
				return;
			} else {
				remoteWb.kickUser(userName);
			}
		
		} catch (RemoteException e) {
			printMsg("Remote error occurs. The server might have some problems");
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void initGUI() {
		this.gui = new ClientGUI(isManager, this);
		gui.initialize(); 
	}
	
	
	@Override
	public void updateUserList(ArrayList<String> userList) throws RemoteException {
		this.gui.getUserListArea().setText(null);
		for (String user: userList) {
			this.gui.getUserListArea().append(user + "\n");
		}		
	}

	@Override
	public boolean ApproveJoin(String userName) throws RemoteException {
		String dialogTitle = "User connection request";
		String dialogContent = userName + " wants to share your whiteboard\n" + "Do you want to approve?";
		int flag = JOptionPane.showConfirmDialog(gui.getFrame(), dialogContent, dialogTitle, JOptionPane.YES_NO_OPTION);
		return flag == JOptionPane.NO_OPTION ? false : true;
	}


	@Override
	public void showChatMessage(String msg, String userName) throws RemoteException {
		this.gui.getChatArea().append(userName + ": " + msg + "\n");
	}

	@Override
	public void close(CloseType closeType) throws RemoteException {
		switch (closeType) {
		case MANAGER_LEAVE:
			new Thread(() -> {
				printMsg("The manager exits. The application will terminate.");
				System.exit(0);
			}).start();
			break;
		case KICKED_OUT:
			new Thread(() -> {
				printMsg("Sorry! The manager kicks you out!");
				System.exit(0);
			}).start();
			break;
		case SELF_CLOSE:
	        System.exit(0);
			break;
		case SERVER_CRASH:
			break;
		default:
			break;
			
		}
		
	}

	@Override
	public void loadImage(byte[] b) throws RemoteException {
		
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		BufferedImage wbImage;
		try {
			wbImage = ImageIO.read(in);
			this.gui.getDa().load(wbImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
