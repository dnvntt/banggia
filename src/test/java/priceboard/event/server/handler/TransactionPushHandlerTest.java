package priceboard.event.server.handler;

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

public class TransactionPushHandlerTest {
	private ClientRoomManager clientRoomManager;
	
	@Before
	public void setUp() {
		clientRoomManager = new ClientRoomManager();
	}
	
	@Test
	public void testTransactionPushHandler() {
		List<String> sentData = new ArrayList<String>();
		TransactionPusher transactionPusher = new TransactionPusher(clientRoomManager, new JsonParser(), new Mashaller());
		for(int i = 0; i < 10; i++) {
			clientRoomManager.addClientToTransaction("VND", new ClientConnection() {
				
				@Override
				public void send(String data) {
					sentData.add(data);
				}
			});
		}
		TransactionPushHandler transactionPushHandler = new TransactionPushHandler(transactionPusher);
		Transaction trans1 = new Transaction();
		trans1.setSymbol("VND");
		trans1.setLast(12.6);
		trans1.setLastVol(1230.0);
		
		Transaction trans2 = new Transaction();
		trans2.setSymbol("SAM");
		trans2.setLast(15.6);
		trans2.setLastVol(3230.0);
		
		transactionPushHandler.handle(trans1);
		transactionPushHandler.handle(trans2);
		
		Assert.assertEquals(10, sentData.size());
	}
}
