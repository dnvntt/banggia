package priceboard.updater;

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
import vn.com.vndirect.priceservice.datamodel.PutThrough;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

public class BroadcastPusherTest {
private ClientRoomManager clientRoomManager;
	
	private JsonParser parser;

	private BroadcastPusher broadcastPusher;
	private InMemory memory;
	final List<String> sentData = new ArrayList<String>();

	@Before
	public void setUp() {
		sentData.clear();
		clientRoomManager = new ClientRoomManager();
		parser = new JsonParser();
		memory = new InMemory();
		broadcastPusher = new BroadcastPusher(clientRoomManager, parser, memory,new Mashaller());
	}

	@Test
	public void testPutThroughPushDataForAllClient() {
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
		
		
		clientRoomManager.addClientToRoom("10", client1);
		clientRoomManager.addClientToRoom("HAG", client2);
		
		PutThrough pushThroughInfo = new PutThrough();
		pushThroughInfo.setStockSymbol("HAG");
		pushThroughInfo.setType("");
		pushThroughInfo.setVol(3120);
		pushThroughInfo.setPrice(24000);
		pushThroughInfo.setFloorCode("02");
		pushThroughInfo.setTradingDate("2015019");
		
			
		broadcastPusher.push(pushThroughInfo);
		String expected = "{\"type\":\"AD_ORDER\",\"data\":\"02|HAG|24000.0|3120.0||0||||||\"}";
		Assert.assertEquals(2, sentData.size());
		Assert.assertEquals(expected, sentData.get(0));
		
		
	 
	}
	@Test
	public void testPutThroughTransactionPushDataForAllClient() {
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
		
		
		clientRoomManager.addClientToRoom("02", client1);
		clientRoomManager.addClientToRoom("SSI", client2);
		
		 
		PutThroughTransaction pushThroughTransactionInfo = new PutThroughTransaction();
		pushThroughTransactionInfo.setSymbol("SAM");
		pushThroughTransactionInfo.setVolume(4120.0);
		pushThroughTransactionInfo.setPrice(14000.0);
		pushThroughTransactionInfo.setFloorCode("10");
		pushThroughTransactionInfo.setTradingDate("2015019");
		broadcastPusher.push(pushThroughTransactionInfo);
		System.out.println("Gt real: "+sentData.get(0));
		String expected = "{\"type\":\"PT_ORDER\",\"data\":\"10|SAM|14000.0|4120.0||||||\"}";
		Assert.assertEquals(2, sentData.size());
		Assert.assertEquals(expected, sentData.get(0));
	}

}




