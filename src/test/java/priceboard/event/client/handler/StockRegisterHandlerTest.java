package priceboard.event.client.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.pusher.StockPusher;
import priceboard.room.ClientRoomManager;

import com.fasterxml.jackson.databind.JsonNode;

public class StockRegisterHandlerTest {

	private JsonParser parser;

	private ClientRoomManager clientRoomManager;
	
	private StockRegisterHandler stockRegister;
	
	@Before
	public void setUp() {
		parser = new JsonParser();
		
		clientRoomManager = new ClientRoomManager();
		
		StockPusher pusher = Mockito.mock(StockPusher.class);
		stockRegister = new StockRegisterHandler(clientRoomManager, pusher, null, parser);
	}
	
	@Test
	public void testClientRegistListCodes() {
		JsonNode node = parser.parse("{\"type\":\"registConsumer\",\"data\":{\"sequence\":\"179325\",\"params\":{\"name\":\"STOCK\",\"codes\":[\"BVS\",\"VND\",\"HAR\",\"FPT\"]},\"isIntervalRegist\":false}}");
		Map<String, Object> map = new HashMap<String, Object>();
		ClientConnection client = Mockito.mock(ClientConnection.class);
		map.put("CLIENT", client);
		map.put("data", node);
		stockRegister.handle(map);
		
		String[] codes = new String[]{"VND", "BVS", "HAR", "FPT"};
		for(String code : codes) {
			List<ClientConnection> clients = clientRoomManager.getClientInRoom(code);
			Assert.assertEquals(1, clients.size());
			Assert.assertEquals(client, clients.get(0));
			
		}
		List<String> rooms = clientRoomManager.getCurrentRoomsOfClient(client);
		Assert.assertEquals(4, rooms.size());
		for(String code : codes) {
			Assert.assertTrue(code + " is not in room list of client", rooms.contains(code));
		}
		
	}
	
	@Test
	public void test2ClientRegistListCodes() {
		JsonNode node = parser.parse("{\"type\":\"registConsumer\",\"data\":{\"sequence\":\"179325\",\"params\":{\"name\":\"STOCK\",\"codes\":[\"BVS\",\"VND\",\"HAR\",\"FPT\"]},\"isIntervalRegist\":false}}");
		Map<String, Object> map = new HashMap<String, Object>();
		ClientConnection client = Mockito.mock(ClientConnection.class);
		map.put("CLIENT", client);
		map.put("data", node);
		stockRegister.handle(map);
		
		JsonNode node2 = parser.parse("{\"type\":\"registConsumer\",\"data\":{\"sequence\":\"179325\",\"params\":{\"name\":\"STOCK\",\"codes\":[\"BVS\",\"VND\",\"HAG\",\"FPT\"]},\"isIntervalRegist\":false}}");
		Map<String, Object> map2 = new HashMap<String, Object>();
		ClientConnection client2 = Mockito.mock(ClientConnection.class);
		map2.put("CLIENT", client2);
		map2.put("data", node2);
		stockRegister.handle(map2);
		
		String[] codes = new String[]{"VND", "BVS", "FPT"};
		for(String code : codes) {
			List<ClientConnection> clients = clientRoomManager.getClientInRoom(code);
			Assert.assertEquals(2, clients.size());
			Assert.assertEquals(client, clients.get(0));
			Assert.assertEquals(client2, clients.get(1));
			
		}
		
		codes = new String[]{"HAG", "HAR"};
		List<ClientConnection> clients = clientRoomManager.getClientInRoom("HAR");
		Assert.assertEquals(1, clients.size());
		Assert.assertEquals(client, clients.get(0));
		
		clients = clientRoomManager.getClientInRoom("HAG");
		Assert.assertEquals(1, clients.size());
		Assert.assertEquals(client2, clients.get(0));

		
		String[] roomsOfClient1 =  new String[]{"VND", "BVS", "FPT", "HAR"};
		List<String> rooms = clientRoomManager.getCurrentRoomsOfClient(client);
		Assert.assertEquals(4, rooms.size());
		for(String room : roomsOfClient1) {
			Assert.assertTrue(room + " is not in room list of client", rooms.contains(room));
		}
		
		String[] roomsOfClient2 =  new String[]{"VND", "BVS", "FPT", "HAG"};
		List<String> rooms2 = clientRoomManager.getCurrentRoomsOfClient(client2);
		Assert.assertEquals(4, rooms2.size());
		for(String room : roomsOfClient2) {
			Assert.assertTrue(room + " is not in room list of client", rooms2.contains(room));
		}
		
	}
}
