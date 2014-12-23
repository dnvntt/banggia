package priceboard.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;

@Component
public class ClientRoomManager {

	private Map<String, CopyOnWriteArrayList<ClientConnection>> roomStorage = new HashMap<String, CopyOnWriteArrayList<ClientConnection>>();
	private Map<ClientConnection, CopyOnWriteArrayList<String>> currentRoomsOfClient = new HashMap<ClientConnection, CopyOnWriteArrayList<String>>();

	private Lock lock = new ReentrantLock();

	public ClientRoomManager() {

	}

	public void addClientToRoom(String room, ClientConnection client) {
		lock.lock();
		try {
			updateRoomStorage(room, client);
			updateCurrentRoomOfClient(room, client);
		} finally {
			lock.unlock();
		}
	}

	private void updateRoomStorage(String room, ClientConnection client) {
		CopyOnWriteArrayList<ClientConnection> clients = roomStorage.get(room);
		if (clients == null) {
			clients = new CopyOnWriteArrayList<ClientConnection>();
		}
		clients.add(client);
		roomStorage.put(room, clients);		
	}
	
	private void updateCurrentRoomOfClient(String room, ClientConnection client) {
		CopyOnWriteArrayList<String> currentRooms = currentRoomsOfClient.get(client);
		if (currentRooms == null) {
			currentRooms = new CopyOnWriteArrayList<String>();
		}
		currentRooms.add(room);
		currentRoomsOfClient.put(client, currentRooms);
	}


	public List<ClientConnection> getClientInRoom(String room) {
		List<ClientConnection> clients = roomStorage.get(room);
		if (clients == null) {
			return new ArrayList<ClientConnection>();
		}
		return roomStorage.get(room);
	}

	public void removeClientFromAllRoom(ClientConnection client) {
		List<String> currentRooms = currentRoomsOfClient.get(client);
		if (currentRooms != null) {
			Iterator<String> rooms = currentRooms.iterator();
			while(rooms.hasNext()) {
				String room = rooms.next();
				removeClientFromStorage(room, client);
			}
		}
		currentRoomsOfClient.remove(client);
	}
	
	public void removeClientFromRoom(String room, ClientConnection client) {
		removeClientFromStorage(room, client);
		removeRoomOfClientFromCurrentRooms(room, client);
	}
	
	private void removeClientFromStorage(String room, ClientConnection client) {
		List<ClientConnection> clients = roomStorage.get(room);
		if (clients != null) {
			clients.remove(client);
		}
	}

	private void removeRoomOfClientFromCurrentRooms(String room, ClientConnection client) {
		List<String> currentRooms = currentRoomsOfClient.get(client);
		if (currentRooms != null) {
			currentRooms.remove(room);
		}		
	}
	

	public List<String> getCurrentRoomsOfClient(ClientConnection client) {
		List<String> rooms = currentRoomsOfClient.get(client);
		if (rooms == null) {
			return new ArrayList<String>();
		}
		return currentRoomsOfClient.get(client);
	}

	public List<ClientConnection> getAllClient() {
		List<ClientConnection> allClient = new ArrayList<ClientConnection>();
		allClient.addAll(currentRoomsOfClient.keySet());
		return allClient;
	}

}
