package priceboard.event.client.handler;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.json.JsonParser;
import priceboard.pusher.Pusher;
import priceboard.room.ClientRoomManager;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class StockRegisterHandler implements EventHandler {

	private ClientRoomManager clientRoomManager;
	
	private Pusher pusher;
	
	private JsonParser parser;
	
	private ClientEventTypeMapping clientEventTypeMapping;
	
	@Autowired
	public StockRegisterHandler(ClientRoomManager clientRoomManager, Pusher pusher, JsonParser parser, ClientEventTypeMapping clientEventTypeMapping) {
		this.clientRoomManager = clientRoomManager;
		this.parser = parser;
		this.pusher = pusher;
		this.clientEventTypeMapping = clientEventTypeMapping;
	}
	
	@PostConstruct
	public void registerHander() {
		clientEventTypeMapping.registerHandler(this, "regisConsumer");
	}

	@Override
	public void handle(Object source) {
		Map<String, Object> map = (Map<String, Object>) source;
		ClientConnection client = (ClientConnection) map.get("CLIENT");
		JsonNode dataNode = (JsonNode) map.get("data");
		List<String> codes = parser.parseDataCodes(dataNode);
		addClientToRoom(codes, client);
		pushDataToClient(codes, client);
	}

	private void addClientToRoom(List<String> codes, ClientConnection client) {
		codes.forEach((room) -> {
			clientRoomManager.addClientToRoom(room, client);
		});
	}
	
	private void pushDataToClient(List<String> codes, ClientConnection client) {
		codes.forEach((room) -> {
			pusher.push(client, new SecInfo(){{setCode(room);}});
		});
	}
}
