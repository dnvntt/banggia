package priceboard.pusher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;

@Component
public class CeilingFloorPusher  implements Pusher{
	private JsonParser parser;
	private ClientRoomManager roomManager;
	@Autowired
	public CeilingFloorPusher(ClientRoomManager roomManager, JsonParser parser) {
		this.parser = parser;
		this.roomManager = roomManager;
	}
	@Override
	public void push(Object source) {
		List<ClientConnection> clients = roomManager.getAllClient();
		String data = getStatisticData(source);  	
		clients.forEach((client) -> client.send(data));
	}
	private String getStatisticData(Object source) {	 
	   return parser.buildReturnJsonStockAsString("CEILING_FLOOR_COUNT", source);
	}
	
	@Override
	public void push(ClientConnection client, Object source) {
	}

}
