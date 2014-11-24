package priceboard.event.client.handler;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.client.ClientConnection;
import priceboard.room.ClientRoomManager;

public class ClientDisconnectHandlerTest {
	
	private ClientRoomManager roomManager;
	
	private ClientDisconnectHandler handler;
	
	
	@Before
	public void setUp() {
		roomManager = new ClientRoomManager();
		handler = new ClientDisconnectHandler(roomManager, null);
	}

	@Test
	public void testDisconnectClientShouldRemoveClientInAllRoom() {
		ClientConnection client = Mockito.mock(ClientConnection.class);
		roomManager.addClientToRoom("VND", client);
		roomManager.addClientToRoom("02", client);
		
		
		handler.handle(client);
		
		List<ClientConnection> clients = roomManager.getClientInRoom("VND");
		Assert.assertEquals(0, clients.size());
		
		clients = roomManager.getClientInRoom("02");
		Assert.assertEquals(0, clients.size());
	}
	
	@Test
	public void testDisconnectClientShouldRemoveThisClientInAllRoom() {
		ClientConnection client = Mockito.mock(ClientConnection.class);
		roomManager.addClientToRoom("VND", client);
		roomManager.addClientToRoom("02", client);
		
		ClientConnection client2 = Mockito.mock(ClientConnection.class);
		roomManager.addClientToRoom("VND", client2);
		roomManager.addClientToRoom("02", client2);
		
		handler.handle(client);
		
		List<ClientConnection> clients = roomManager.getClientInRoom("VND");
		Assert.assertEquals(1, clients.size());
		Assert.assertEquals(client2, clients.get(0));
		
		clients = roomManager.getClientInRoom("02");
		Assert.assertEquals(1, clients.size());
		Assert.assertEquals(client2, clients.get(0));
	}
}
