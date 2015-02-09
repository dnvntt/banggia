package priceboard.event.client.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;

import com.fasterxml.jackson.databind.JsonNode;

@Component
@EventHandlerApplyFor(priority = 2, values = { "stopConsume" })
public class StopConsumeHandler implements EventHandler {

	private JsonParser parser;
	private ClientRoomManager clientRoomManager;

	@Autowired
	public StopConsumeHandler(ClientRoomManager roomManager, JsonParser parser) {
		this.clientRoomManager = roomManager;
		this.parser = parser;
	}

	@Override
	public void handle(Object source) {
		Map<String, Object> map = (Map<String, Object>) source;
		ClientConnection client = (ClientConnection) map.get("CLIENT");

		JsonNode dataNode = (JsonNode) map.get("data");
		List<String> codes = parser.parseDataCodes(dataNode);
		if (codes == null || codes.isEmpty()) {
			return;
		}
		codes.forEach((code) -> {
			clientRoomManager.removeClientFromRoom(code, client);
		});

	}

}