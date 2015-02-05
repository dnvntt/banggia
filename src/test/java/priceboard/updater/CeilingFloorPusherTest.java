package priceboard.updater;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.pusher.CeilingFloorPusher;
import priceboard.room.ClientRoomManager;
import vn.com.vndirect.datafeed.util.MarketStatisMessage;

public class CeilingFloorPusherTest {
	
	private ClientRoomManager clientRoomManager;
	private JsonParser parser;
	private CeilingFloorPusher ceilingFloorPusher;
	final List<String> sentData = new ArrayList<String>();

	@Before
	public void setUp() {
		sentData.clear();
		clientRoomManager = new ClientRoomManager();
		parser = new JsonParser();
		ceilingFloorPusher = new CeilingFloorPusher(clientRoomManager, parser);
	}

	@Test
	public void testCeilingFloorPushDataToAllClient() {
		ClientConnection client1 = new ClientConnection() {
			public void send(String data) {
				sentData.add(data);
			};
		};

		ClientConnection client2 = new ClientConnection() {
			public void send(String data) {
				sentData.add(data);
			};
		};

		clientRoomManager.addClientToRoom("VND", client1);
		clientRoomManager.addClientToRoom("HAG", client2);

		MarketStatisMessage statistic1 = new MarketStatisMessage();
		statistic1.setFloor("02");
		statistic1.setTradingDate("20150131");
		statistic1.setType("FLOOR");
		statistic1.setCount("5");
		 

		ceilingFloorPusher.push(statistic1);
		
		String expected = "{\"type\":\"CEILING_FLOOR_COUNT\",\"data\":{\"count\":\"5\",\"type\":\"FLOOR\",\"floor\":\"02\",\"symbls\":null,\"tradingDate\":\"20150131\"}}";
		Assert.assertEquals(2, sentData.size());
		Assert.assertEquals(expected, sentData.get(0));
	}
}
