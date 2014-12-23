package priceboard.event.client.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.json.JsonParser;
import priceboard.pusher.StockPusher;
import priceboard.room.ClientRoomManager;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

import com.fasterxml.jackson.databind.JsonNode;

@Component
@EventHandlerApplyFor(values = {"registConsumer"})
public class StockRegisterHandler implements EventHandler {

	private ClientRoomManager clientRoomManager;
	
	private StockPusher pusher;
	
	private JsonParser parser;
	
	@Autowired
	public StockRegisterHandler(ClientRoomManager clientRoomManager, StockPusher pusher, JsonParser parser) {
		this.clientRoomManager = clientRoomManager;
		this.parser = parser;
		this.pusher = pusher;
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
