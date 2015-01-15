package priceboard.pusher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

@Component
public class BroadcastPusher implements Pusher {

	private ClientRoomManager roomManager;
	private JsonParser parser;
	private InMemory memory;

	@Autowired
	public BroadcastPusher(ClientRoomManager roomManager, JsonParser parser,
			InMemory memory) {
		this.roomManager = roomManager;
		this.parser = parser;
		this.memory = memory;
	}

	@Override
	public void push(Object source) {
		List<ClientConnection> clients = roomManager.getAllClient();
		String data = getCompressionData((Market) source);
		clients.forEach((client) -> client.send(data));
	}

	private String getCompressionData(Market source) {
		String floorCode = source.getFloorCode();
		String data = (String) memory.get("MARKET_COMPRESSION", floorCode);
		// return data;
		return parser.buildReturnJsonMarketAsString(data);
	}

	@Override
	public void push(ClientConnection client, Object source) {
	}

}
