package priceboard.pusher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.client.model.DataReturn;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.MarketStatisMessage;

@Component
public class CeilingFloorPusher{
	private JsonParser parser;
	private InMemory memory;

	@Autowired
	public CeilingFloorPusher(ClientRoomManager roomManager, JsonParser parser,
			InMemory memory) {
		this.parser = parser;
		this.memory = memory;
	}


	public void push(ClientConnection client) {
		Map<String, Map<String, String>> marketCeilingFloor = new HashMap<String, Map<String, String>>();
		List<MarketStatisMessage> marketList = (List<MarketStatisMessage>) memory.get("CeilingFloor", "");

		Map<String, String> marketCeilingInfos = new HashMap<String, String>();
		Map<String, String> marketfloorInfos = new HashMap<String, String>();
		for (MarketStatisMessage market : marketList) {

			if (market.getType().equals("CEILING")) {
				marketCeilingInfos.put(market.getFloor(), market.getCount());
			} else {
				marketfloorInfos.put(market.getFloor(), market.getCount());
			}
		}
		marketCeilingFloor.put("ceiling", marketCeilingInfos);
		marketCeilingFloor.put("floor", marketfloorInfos);
		
		DataReturn data = new DataReturn();
		data.setName("CEILING_FLOOR_COUNT");
		data.setData(marketCeilingFloor);
		String msg_return = parser.buildReturnJsonStockAsString("returnData",data);
		
		//List<ClientConnection> clients = roomManager.getAllClient();
		//clients.forEach((client) -> client.send(msg_return));
		
		 client.send(msg_return);
	}
}
