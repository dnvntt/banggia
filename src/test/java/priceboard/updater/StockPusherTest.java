package priceboard.updater;

import java.util.ArrayList;
import java.util.Calendar;
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

public class StockPusherTest {

	private ClientRoomManager clientRoomManager;
	private StockRoomManager stockRoomManager ;
	private InMemory memory;
	private JsonParser parser;
	
	private StockPusher stockPusher;

	final List<String> sentData = new ArrayList<String>();
	
	@Before
	public void setUp() {
		sentData.clear();
		clientRoomManager = new ClientRoomManager();
		stockRoomManager = new StockRoomManager();
		memory = new InMemory();
		parser = new JsonParser();
		stockPusher = new StockPusher(clientRoomManager, stockRoomManager, parser, memory);
	}
	
	@Test
	public void testStockPushererPushDataForAllClient() {
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
		
		stockRoomManager.addStockToRoom("10", "HAG");
		clientRoomManager.addClientToRoom("10", client1);
		clientRoomManager.addClientToRoom("HAG", client2);
		
		SecInfo secInfo = new SecInfo();
		secInfo.setCode("HAG");
		secInfo.setBasicPrice(10.5);
		secInfo.setFloorPrice(10);;
		secInfo.setCeilingPrice(12);
		secInfo.setFloorCode("10");
		secInfo.setAccumulatedVal(12000);
		secInfo.setAccumulatedVal(120);
		secInfo.setMatchPrice(10);
		secInfo.setMatchQtty(4000);
		secInfo.setBidPrice01(12);
		secInfo.setBidPrice01(11);
		secInfo.setBidQtty01(200);
		secInfo.setBidPrice01(10);
		secInfo.setBidQtty01(200);
		secInfo.setOfferPrice01(10);
		secInfo.setOfferQtty01(3000);
		secInfo.setOfferPrice02(11);
		secInfo.setOfferQtty02(3000);
		secInfo.setOfferPrice03(11.5);
		secInfo.setOfferQtty03(3000);
		secInfo.setCompanyName("Hoang Anh Gia Lai");
		secInfo.setCurrentRoom(3000);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		secInfo.setTradingDate(cal.getTime());
		
		memory.put("STOCK_COMPRESSION", "HAG", "10|20/11/2014||HAG|Hoang Anh Gia Lai||0.0|3000.0|10.5|0.0|0.0|0.0|0.0|0.0|0.0|12.0|10.0|0.0|0.0|10.0|4000.0|0.0|0.0|10.0|200.0|0.0|0.0|0.0|0.0|10.0|3000.0|11.0|3000.0|11.5|3000.0|120.0|0.0|0.0|0.0|0.0||");

		stockPusher.push(secInfo);
		
		String expected = "{\"type\":\"STOCK\",\"data\":\"10|20/11/2014||HAG|Hoang Anh Gia Lai||0.0|3000.0|10.5|0.0|0.0|0.0|0.0|0.0|0.0|12.0|10.0|0.0|0.0|10.0|4000.0|0.0|0.0|10.0|200.0|0.0|0.0|0.0|0.0|10.0|3000.0|11.0|3000.0|11.5|3000.0|120.0|0.0|0.0|0.0|0.0||\"}";
		Assert.assertEquals(2, sentData.size());
		Assert.assertEquals(expected, sentData.get(0));
		Assert.assertEquals(expected, sentData.get(1));
		
	}

	@Test
	public void testStockPushererPushDataForOneClient() {
		ClientConnection client = new ClientConnection() {
			public void send(String data) {
				sentData.add(data);
			};
		};
		memory.put("STOCK_COMPRESSION", "HAG", "10|20/11/2014||HAG|Hoang Anh Gia Lai||0.0|3000.0|10.5|0.0|0.0|0.0|0.0|0.0|0.0|12.0|10.0|0.0|0.0|10.0|4000.0|0.0|0.0|10.0|200.0|0.0|0.0|0.0|0.0|10.0|3000.0|11.0|3000.0|11.5|3000.0|120.0|0.0|0.0|0.0|0.0||");
		stockPusher.push(client, new SecInfo() {{setCode("HAG");}});
		String expected = "{\"type\":\"STOCK\",\"data\":\"10|20/11/2014||HAG|Hoang Anh Gia Lai||0.0|3000.0|10.5|0.0|0.0|0.0|0.0|0.0|0.0|12.0|10.0|0.0|0.0|10.0|4000.0|0.0|0.0|10.0|200.0|0.0|0.0|0.0|0.0|10.0|3000.0|11.0|3000.0|11.5|3000.0|120.0|0.0|0.0|0.0|0.0||\"}";
		Assert.assertEquals(1, sentData.size());
		Assert.assertEquals(expected, sentData.get(0));
	}
	
	@Test
	public void testStockPusherPushDataForOneClientWhenDataNotExist() {
		ClientConnection client = new ClientConnection() {
			public void send(String data) {
				sentData.add(data);
			};
		};
		stockPusher.push(client, new SecInfo() {{setCode("HAG");}});
		String expected = "{\"type\":\"STOCK\",\"data\":\"\"}";
		Assert.assertEquals(1, sentData.size());
		Assert.assertEquals(expected, sentData.get(0));
	}
}
