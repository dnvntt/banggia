package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.pusher.StockPusher;
import priceboard.room.ClientRoomManager;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class StockPushHandlerTest {
	private ClientRoomManager clientRoomManager;
	private StockRoomManager stockRoomManager;
	@Before
	public void setUp() {
		clientRoomManager = new ClientRoomManager();
		stockRoomManager = new StockRoomManager();
	}
	
	@Test
	public void testStockPushHandlerPushData() {
		List<String> sentData = new ArrayList<String>();
		StockPusher broadCastPusher = new StockPusher(clientRoomManager, stockRoomManager, new JsonParser(), new InMemory());
		for(int i = 0; i < 10; i++) {
			clientRoomManager.addClientToRoom("VND", new ClientConnection() {
				
				@Override
				public void send(String data) {
					sentData.add(data);
				}
			});
		}
		StockPushHandler stockPushHandler = new StockPushHandler(broadCastPusher);
		
		SecInfo stock = new SecInfo();
		stock.setBasicPrice(12.1);
		stock.setCode("VND");
		
		SecInfo stock1 = new SecInfo();
		stock1.setBasicPrice(14.1);
		stock1.setCode("SAM");
		
		stockPushHandler.handle(stock);
		stockPushHandler.handle(stock1);
		
		Assert.assertEquals(10, sentData.size());
	}
}
