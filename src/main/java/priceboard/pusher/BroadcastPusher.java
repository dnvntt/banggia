package priceboard.pusher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;
import priceboard.util.InstanceChecker;
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
		String data = getCompressionData(source);  
		clients.forEach((client) -> client.send(data));
	}

	private String getCompressionData(Object source) {
		if (InstanceChecker.isMarket(source)) {
			String floorCode = ((Market) source).getFloorCode();
			String data = (String) memory.get("MARKET_COMPRESSION", floorCode);
			return parser.buildReturnJsonStockAsString("MARKETINFO", data);
		}
		
		if (InstanceChecker.isPutThroughTransaction(source)) {
			return parser.buildReturnJsonStockAsString("PT_ORDER", source);
		} 
		
		if (InstanceChecker.isPutThrough(source)) {
			return parser.buildReturnJsonStockAsString("AD_ORDER", source);
		}

		return "";

	}

	@Override
	public void push(ClientConnection client, Object source) {
	}

}
