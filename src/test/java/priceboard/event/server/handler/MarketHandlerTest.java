package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.pusher.BroadcastPusher;
import priceboard.room.ClientRoomManager;
import priceboard.stock.compress.Mashaller;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

public class MarketHandlerTest {

	private ClientRoomManager roomManager;
	
	@Before
	public void setUp() {
		roomManager = new ClientRoomManager();
	}
	
	@Test
	public void testMarketHandlerPushData() {
		List<String> sentData = new ArrayList<String>();
		BroadcastPusher broadCastPusher = new BroadcastPusher(roomManager, new JsonParser(), new InMemory(), new Mashaller());
		for(int i = 0; i < 10; i++) {
			roomManager.addClientToRoom("VND" + i, new ClientConnection() {
				
				@Override
				public void send(String data) {
					sentData.add(data);
				}
			});
		}
		BroadcastPushHandler marketHandler = new BroadcastPushHandler(broadCastPusher);
		marketHandler.handle(new Market());
		Assert.assertEquals(10, sentData.size());
	}
}
