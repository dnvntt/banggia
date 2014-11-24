package priceboard;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.Repeat;

import priceboard.client.ClientConnection;
import priceboard.room.ClientRoomManager;

public class ClientRoomManagerTest {
	
	private ClientRoomManager roomManager;

	@Before
	public void setUp() {
		roomManager = new ClientRoomManager();
	}
	
	@Test
	public void testAddRoom() {
		ClientConnection client1 = Mockito.mock(ClientConnection.class);
		
		roomManager.addClientToRoom("VND", client1);
		List<ClientConnection> actualClients = roomManager.getClientInRoom("VND");
		Assert.assertEquals(client1, actualClients.get(0));
	}
	
	@Test
	public void testAddRoomWithManyClient() {
		roomManager.addClientToRoom("VND", Mockito.mock(ClientConnection.class));
		roomManager.addClientToRoom("VND", Mockito.mock(ClientConnection.class));
		List<ClientConnection> actualClients = roomManager.getClientInRoom("VND");
		Assert.assertEquals(2, actualClients.size());
	}
	
	@Test
	public void testAddRoomWithPublicRoom() {
		roomManager.addClientToRoom("02", Mockito.mock(ClientConnection.class));
		roomManager.addClientToRoom("10", Mockito.mock(ClientConnection.class));
		Assert.assertEquals(1, roomManager.getClientInRoom("02").size());
		Assert.assertEquals(1, roomManager.getClientInRoom("10").size());
	}
	
	@Test
	public void testAddMultipleRoomAsync() throws InterruptedException {
		final int numberRoomPerThread = 5;
		
		Thread t1 = new Thread() {
			public void run() {
				for(int i = 0; i < numberRoomPerThread; i++) {
					roomManager.addClientToRoom("VND", Mockito.mock(ClientConnection.class));
				}
			};
		};
		
		Thread t2 = new Thread() {
			public void run() {
				for(int i = 0; i < numberRoomPerThread; i++) {
					roomManager.addClientToRoom("VND", Mockito.mock(ClientConnection.class));
				}
			};
		};
		
		
		Thread t3 = new Thread() {
			public void run() {
				for(int i = 0; i < numberRoomPerThread; i++) {
					roomManager.addClientToRoom("SSI", Mockito.mock(ClientConnection.class));
				}
			};
		};
		
		Thread t4 = new Thread() {
			public void run() {
				for(int i = 0; i < numberRoomPerThread; i++) {
					roomManager.addClientToRoom("SSI", Mockito.mock(ClientConnection.class));
				}
			};
		};
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		Assert.assertEquals(2 * numberRoomPerThread, roomManager.getClientInRoom("VND").size());
		Assert.assertEquals(2 * numberRoomPerThread, roomManager.getClientInRoom("SSI").size());
	}
	
	@Test
	public void testRemoveClientInRoom() {
		ClientConnection client =  Mockito.mock(ClientConnection.class);
		roomManager.addClientToRoom("VND", client);
		Assert.assertEquals(1, roomManager.getClientInRoom("VND").size());
		roomManager.removeClientFromRoom("VND", client);
		Assert.assertEquals(0, roomManager.getClientInRoom("VND").size());
		roomManager.removeClientFromRoom("VND", client);
		Assert.assertEquals(0, roomManager.getClientInRoom("VND").size());
	}
	
	@Test
	public void testRemoveClientInRoomButKeepItInOtherRoom() {
		ClientConnection client = Mockito.mock(ClientConnection.class);
		checkAddAndRemoveRoom(client);
		
	}
	
	private void checkAddAndRemoveRoom(ClientConnection client) {
		roomManager.addClientToRoom("VND", client);
		roomManager.addClientToRoom("SSI", client);
		roomManager.addClientToRoom("02", client);
		roomManager.removeClientFromRoom("VND", client);
		Assert.assertEquals(0, roomManager.getClientInRoom("VND").size());
		Assert.assertEquals(1, roomManager.getClientInRoom("SSI").size());
		Assert.assertEquals(1, roomManager.getClientInRoom("02").size());
	}
	
	@Test
	public void testGetCurrentRoomsOfClient() {
		ClientConnection client = Mockito.mock(ClientConnection.class);
		List<String> currentRoomOfClients = roomManager.getCurrentRoomsOfClient(client);
		Assert.assertTrue("Current room is empty", currentRoomOfClients == null || currentRoomOfClients.size() == 0);
		checkAddAndRemoveRoom(client);
		currentRoomOfClients = roomManager.getCurrentRoomsOfClient(client);
		Assert.assertEquals(2, currentRoomOfClients.size());
		Assert.assertTrue("current room does not contains SSI", currentRoomOfClients.contains("SSI"));
		Assert.assertTrue("current room does not contains 02", currentRoomOfClients.contains("02"));
	}
	
	@Test
	public void testRemoveClientInAllRoom() {
		ClientConnection client = Mockito.mock(ClientConnection.class);
		roomManager.addClientToRoom("VND", client);
		roomManager.addClientToRoom("SSI", client);
		roomManager.addClientToRoom("02", client);
		roomManager.removeClientFromAllRoom(client);
		Assert.assertEquals(0, roomManager.getClientInRoom("VND").size());
		Assert.assertEquals(0, roomManager.getClientInRoom("SSI").size());
		Assert.assertEquals(0, roomManager.getClientInRoom("02").size());
	}
	
	
	@Test
	@Repeat(10)
	public void testAddAndRemoveAndLoopOverClientConnnectionsWithoutConcurrentModifiedException() throws InterruptedException {
		final List<String> exceptions = new ArrayList<String>();
		Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
		    public void uncaughtException(Thread th, Throwable ex) {
		    	exceptions.add(ex.getMessage());
		    	ex.printStackTrace();
		    }
		};
		
		final ClientConnection client1 = Mockito.mock(ClientConnection.class);
		final ClientConnection client2 = Mockito.mock(ClientConnection.class);
		final ClientConnection client3 = Mockito.mock(ClientConnection.class);
		final String[] rooms = new String[]{"VND", "SSI", "02"};
		
		Thread addThread = new Thread() {
			public void run() {
				for(int i = 0; i < 10; i++) {
					roomManager.addClientToRoom(rooms[i%3], client1);
					roomManager.addClientToRoom(rooms[i%3], client2);
					roomManager.addClientToRoom(rooms[i%3], client3);
				}
			};
		};
		
		Thread removeThread1 = new Thread() {
			public void run() {
				roomManager.removeClientFromAllRoom(client1);
				roomManager.removeClientFromAllRoom(client2);
				roomManager.removeClientFromAllRoom(client3);
			}
		};
		
		
		Thread removeThread2 = new Thread() {
			public void run() {
				roomManager.removeClientFromRoom("VND", client1);
				roomManager.removeClientFromRoom("SSI", client2);
				roomManager.removeClientFromRoom("02", client3);
			}
		};
		
		Thread getThread1 = new Thread() {
			public void run() {
				List<ClientConnection> clients = roomManager.getClientInRoom("VND");
				if (clients == null) return;
				for (ClientConnection client : clients) {
					System.out.println(client);
				}
			}
		};

		Thread getThread2 = new Thread() {
			public void run() {
				List<ClientConnection> clients = roomManager.getClientInRoom("SSI");
				if (clients == null) return;
				for (ClientConnection client : clients) {
					System.out.println(client);
				}
			}
		};
		
		
		Thread getThread3 = new Thread() {
			public void run() {
				List<ClientConnection> clients = roomManager.getClientInRoom("02");
				if (clients == null) return;
				for (ClientConnection client : clients) {
					System.out.println(client);
				}
			}
		};
		
		
		
		addThread.setUncaughtExceptionHandler(exceptionHandler);
		removeThread1.setUncaughtExceptionHandler(exceptionHandler);
		removeThread2.setUncaughtExceptionHandler(exceptionHandler);
		getThread1.setUncaughtExceptionHandler(exceptionHandler);
		getThread2.setUncaughtExceptionHandler(exceptionHandler);
		getThread3.setUncaughtExceptionHandler(exceptionHandler);
		
		addThread.start();
		removeThread1.start();
		removeThread2.start();
		getThread1.start();
		getThread2.start();
		getThread3.start();
		
		addThread.join();
		removeThread1.join();
		removeThread2.join();
		getThread1.join();
		getThread2.join();
		getThread3.join();
		System.out.println(exceptions);
		Assert.assertEquals("Exception: ", 0, exceptions.size());
	}

}