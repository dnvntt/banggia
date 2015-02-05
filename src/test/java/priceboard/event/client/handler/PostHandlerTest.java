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
import vn.com.vndirect.lib.commonlib.memory.InMemory;

import com.fasterxml.jackson.databind.JsonNode;

public class PostHandlerTest {
	private JsonParser parser;
	private ClientRoomManager clientRoomManager;
	private InMemory memory;
	private PostHandler transactionRegister;

	@Before
	public void setUp() {
		parser = new JsonParser();
		memory = new InMemory();
		clientRoomManager = new ClientRoomManager();
		transactionRegister = new PostHandler(clientRoomManager, parser, memory);
	}

	@Test
	public void testClientRegistTransactionCode() {
		JsonNode node = parser
				.parse("{\"type\":\"post\",\"data\":{\"sequence\":0,\"params\":{\"name\":\"TRANSACTION\",\"symbol\":\"VND\"}}}");
		Map<String, Object> map = new HashMap<String, Object>();
		ClientConnection client = Mockito.mock(ClientConnection.class);
		map.put("CLIENT", client);
		map.put("data", node);
		transactionRegister.handle(map);

		String room = "VND";

		List<ClientConnection> clients = clientRoomManager.getClientInTransaction(room);
		Assert.assertEquals(1, clients.size());
		Assert.assertEquals(client, clients.get(0));

		JsonNode node1 = parser
				.parse("{\"type\":\"post\",\"data\":{\"sequence\":0,\"params\":{\"name\":\"TRANSACTION\",\"symbol\":\"SSI\"}}}");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("CLIENT", client);
		map1.put("data", node1);
		transactionRegister.handle(map1);
		List<ClientConnection> clients1 = clientRoomManager.getClientInTransaction(room);
		Assert.assertEquals(0, clients1.size());
		
	}
}
