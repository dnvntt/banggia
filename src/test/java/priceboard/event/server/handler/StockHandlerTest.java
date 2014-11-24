package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.client.ClientConnection;
import priceboard.pusher.Pusher;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class StockHandlerTest {

	private List<Object> objectList;
	 
	private StockHandler stockHandler;
	
	private StockRoomManager roomManager;
	
	@Before
	public void setUp() {
		objectList = new ArrayList<Object>();
		Pusher stockUpdater = new Pusher() {
			
			@Override
			public void push(ClientConnection client, Object source) {
				
			}
			
			@Override
			public void push(Object source) {
				objectList.add(source);				
			}
		};
		roomManager = new StockRoomManager();
		stockHandler = new StockHandler(stockUpdater, roomManager);
	}
	
	@Test
	public void testStockHandler() {
		SecInfo secInfo = new SecInfo();
		stockHandler.handle(secInfo);
		Assert.assertEquals(1, objectList.size());
		Assert.assertEquals(secInfo, objectList.get(0));
	}
	
	@Test
	public void testStockHandlerUpdateRoom() {
		SecInfo secInfo = new SecInfo();
		secInfo.setCode("VND");
		secInfo.setFloorCode("10");
		stockHandler.handle(secInfo);
		Assert.assertEquals(1, objectList.size());
		Assert.assertEquals(secInfo, objectList.get(0));
		
		List<String> roomOfStocks = roomManager.getRoomOfStock("VND");
		Assert.assertEquals(1, roomOfStocks.size());
		Assert.assertEquals("10", roomOfStocks.get(0));
	}
}
