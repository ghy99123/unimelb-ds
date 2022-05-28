package server;

//import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import remote.IClient;
import remote.IRemoteWhiteBoard;
import util.CloseType;
import util.JoinMessage;


public class RemoteWhiteBoard extends UnicastRemoteObject implements IRemoteWhiteBoard{

	// Stores the client users.
	private ConcurrentHashMap<String, IClient> clients;
	
	private String manager;
	
	private byte[] imageInBytes;
	
	protected RemoteWhiteBoard() throws RemoteException {
		clients = new ConcurrentHashMap<>();
	}	
	
	
	private ArrayList<String> getUserNameList() {
		return new ArrayList<String>(clients.keySet());
	}
	
	private void updateUserListOnClientSide() {
		ArrayList<String> userList = getUserNameList();
		
		clients.forEach((key, value) -> {
			try {
				value.updateUserList(userList);
			} catch (RemoteException e) {
				System.out.println("Remote Error! " + key + " has some communication-related problems. Remote method call fails!");
			}
		});
	}
	
	@Override 
	public synchronized JoinMessage join(String userName, boolean isManager, IClient client) throws RemoteException {
		
		if (isManager) {
			if (manager != null) {
				return JoinMessage.MANAGER_EXIST;
			} else {
				manager = userName;
				clients.put(userName, client);
				client.initGUI();
				
				updateUserListOnClientSide();
				return JoinMessage.SUCCESS;
			}
			
		}
		
		if (manager == null) {
			return JoinMessage.NO_MANAGER;
		}
		
		
		if (clients.containsKey(userName)) {
			return JoinMessage.INVALID_NAME;
		}
		
		if (clients.get(manager).ApproveJoin(userName)) {
			
			client.initGUI();
			
			if (imageInBytes != null)
				client.loadImage(imageInBytes);
			
			clients.put(userName, client);
			
			updateUserListOnClientSide();
			broadcast(userName + " has joined the whiteboard!", manager + "(manager)");
			
			return JoinMessage.SUCCESS;
		} else {
			return JoinMessage.REFUSED;
		}
		
	}

	@Override
	public synchronized void synchronizeImage(byte[] b, String userName) throws RemoteException {
		imageInBytes = b;
		
		for (String client : clients.keySet()) {
            if (!client.equals(userName)) {
                clients.get(client).loadImage(imageInBytes);
            }
        }
		
	}


	@Override
	public void clearUser(String userName) throws RemoteException {
		// TODO Auto-generated method stub
		clients.remove(userName);
		
		updateUserListOnClientSide();
		broadcast(userName + " has left", manager + "(manager)");
		
	}


	@Override
	public void broadcast(String msg, String speaker) throws RemoteException {
		
		clients.forEach((key, value) -> {
			try {
				value.showChatMessage(msg, speaker);
			} catch (RemoteException e) {
				System.out.println("Remote Error! " + key + " has some communication-related problems. Remote method call fails!");
			}
		});
	}


	@Override
	public void terminate() throws RemoteException {
		
		clients.remove(manager);
		manager = null;
		
		clients.forEach((key, value) -> {
			try {
				value.close(CloseType.MANAGER_LEAVE);
			} catch (RemoteException e) {
				System.out.println("Remote Error! " + key + " has some communication-related problems. Remote method call fails!");
			}
		});
		clients.clear();
		imageInBytes = null;
	}


	@Override
	public void kickUser(String userName) throws RemoteException {
		
		clients.get(userName).close(CloseType.KICKED_OUT);
		clients.remove(userName);
		broadcast(userName + " has been kicked out!", manager + "(manager)");
	}


	@Override
	public boolean isUserExist(String userName) throws RemoteException {
		return clients.containsKey(userName);
	}

}
