package priceboard.updater;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.pusher.TransactionPusher;
import priceboard.room.ClientRoomManager;
import priceboard.stock.compress.Mashaller;
import vn.com.vndirect.priceservice.datamodel.Transaction;

public class TransactionPusherTest {

	private ClientRoomManager clientRoomManager;
	
	private JsonParser parser;

	private TransactionPusher transactionPusher;

	final List<String> sentData = new ArrayList<String>();

	@Before
	public void setUp() {
		sentData.clear();
		clientRoomManager = new ClientRoomManager();
		parser = new JsonParser();
		transactionPusher = new TransactionPusher(clientRoomManager, parser, new Mashaller());
	}

	@Test
	public void testTransactionPusherPushDataForAllClient() {
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
		
		
		clientRoomManager.addClientToTransaction("10", client1);
		clientRoomManager.addClientToTransaction("HAG", client2);
		
		Transaction transactionInfo = new Transaction();
		transactionInfo.setSymbol("HAG");
		transactionInfo.setHighest(24);
		transactionInfo.setAccumulatedVal(32432123);
		transactionInfo.setAccumulatedVol(3423);
		transactionInfo.setLast(23.3);
		transactionInfo.setFloorCode("02");
		transactionInfo.setTradingDate("2015019");
		
			
		transactionPusher.push(transactionInfo);
		System.out.println("Gt real: "+sentData.get(0));
		//String expected = "{\"type\":\"TRANSACTION\",\"data\":{\"highest\":24.0,\"last\":23.3,\"lastVol\":0.0,\"lowest\":0.0,\"matchType\":null,\"symbol\":\"HAG\",\"openPrice\":0.0,\"time\":null,\"floorCode\":\"02\",\"stockId\":0.0,\"accumulatedVal\":3.2432123E7,\"accumulatedVol\":3423.0,\"tradingDate\":\"2015019\"}}";
		String expected = "{\"type\":\"TRANSACTION\",\"data\":\"02|HAG|24.0|23.3|0.0|0.0||0.0||||\"}";
		Assert.assertEquals(1, sentData.size());
		Assert.assertEquals(expected, sentData.get(0));
		
	}

}
