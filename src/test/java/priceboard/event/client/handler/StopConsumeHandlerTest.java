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
import priceboard.room.ClientRoomManager;

import com.fasterxml.jackson.databind.JsonNode;

public class StopConsumeHandlerTest {

	private JsonParser parser;
	private ClientRoomManager clientRoomManager;
	private StopConsumeHandler stopConsume;
	private StockRegisterHandler stockRegister;
	
	@Before
	public void setUp() {
		parser = new JsonParser();
		clientRoomManager = new ClientRoomManager();
		stockRegister = new StockRegisterHandler(clientRoomManager, parser);
		stopConsume = new StopConsumeHandler(clientRoomManager, parser);
	}
	
	@Test
	public void testClientStopConsume() {
		JsonNode node = parser.parse("{\"type\":\"registConsumer\",\"data\":{\"sequence\":\"179325\",\"params\":{\"name\":\"STOCK\",\"codes\":[\"BVS\",\"VND\",\"HAR\",\"FPT\"]},\"isIntervalRegist\":false}}");
		Map<String, Object> map = new HashMap<String, Object>();
		ClientConnection client = Mockito.mock(ClientConnection.class);
		map.put("CLIENT", client);
		map.put("data", node);
		stockRegister.handle(map);
		
				
		JsonNode nodeStop = parser.parse("{\"type\":\"stopConsume\",\"data\":{\"sequence\":\"0\",\"params\":{\"name\":\"STOCK\",\"codes\":[\"BVS\",\"VND\"]}}}");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("CLIENT", client);
		map1.put("data", nodeStop);
		stopConsume.handle(map1);
		
		List<String> rooms = clientRoomManager.getCurrentRoomsOfClient(client);
		Assert.assertEquals(2, rooms.size());
		Assert.assertTrue( rooms.contains("HAR"));
		
		Assert.assertTrue("FPT still inside room of client", rooms.contains("FPT"));
		
		
	 
		
	}
	
}
