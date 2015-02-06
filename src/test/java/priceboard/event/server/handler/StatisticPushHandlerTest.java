package priceboard.event.server.handler;

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

public class StatisticPushHandlerTest {
	private ClientRoomManager clientRoomManager;
	
	@Before
	public void setUp() {
		clientRoomManager = new ClientRoomManager();
	}
	
	@Test
	public void testStatisticPushHandler() {
		List<String> sentData = new ArrayList<String>();
		CeilingFloorPusher statisticPusher = new CeilingFloorPusher(clientRoomManager, new JsonParser());
		for(int i = 0; i < 10; i++) {
			clientRoomManager.addClientToRoom("VND" + i, new ClientConnection() {
				
				@Override
				public void send(String data) {
					sentData.add(data);
				}
			});
		}
		StatisticPushHandler statisticHandler = new StatisticPushHandler(statisticPusher);
		statisticHandler.handle(new MarketStatisMessage());
		Assert.assertEquals(10, sentData.size());
	}
}
