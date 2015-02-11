package priceboard.pusher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;
import priceboard.stock.compress.Mashaller;
import priceboard.util.InstanceChecker;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

@Component
public class BroadcastPusher implements Pusher {

	private ClientRoomManager roomManager;
	private JsonParser parser;
	private InMemory memory;
	private Mashaller mashaller;

	@Autowired
	public BroadcastPusher(ClientRoomManager roomManager, JsonParser parser,
			InMemory memory, Mashaller mashaller) {
		this.roomManager = roomManager;
		this.parser = parser;
		this.memory = memory;
		this.mashaller = mashaller;
	}

	@Override
	public void push(Object source) {
		String data = getCompressionData(source);
		if (data.equals(""))
			return;
		List<ClientConnection> clients = roomManager.getAllClient();
		clients.forEach((client) -> client.send(data));
	}

	private String getCompressionData(Object source) {
		if (InstanceChecker.isMarket(source)) {
			String floorCode = ((Market) source).getFloorCode();
			String data = (String) memory.get("MARKET_COMPRESSION", floorCode);
			return parser.buildReturnJsonStockAsString("MARKETINFO", data);
		}

		if (InstanceChecker.isPutThroughTransaction(source)) {
			String compression = mashaller.compress(source);
			return parser.buildReturnJsonStockAsString("PT_ORDER", compression);
		}

		if (InstanceChecker.isPutThrough(source)) {
			String compression = mashaller.compress(source);
			return parser.buildReturnJsonStockAsString("AD_ORDER", compression);
		}

		return "";

	}

	@Override
	public void push(ClientConnection client, Object source) {
	}

}
