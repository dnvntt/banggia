package priceboard.event.server.handler;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.room.StockRoomManager;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class StockUpdateRoomHandlerTest {

	private StockUpdateRoomHandler stockHandler;
	
	private StockRoomManager roomManager;
	private MarketSessionChecker marketSessionChecker;

	@Before
	public void setUp() {
		marketSessionChecker = Mockito.mock(MarketSessionChecker.class);
		roomManager = new StockRoomManager();
		stockHandler = new StockUpdateRoomHandler(roomManager, marketSessionChecker);
	}

	@Test
	public void testStockHandlerOnlyUpdateRoomOfStockWhenMarketClose() {
		Mockito.when(marketSessionChecker.isClosedSession("02")).thenReturn(true);
		SecInfo secInfo = new SecInfo();
		secInfo.setCode("VND");
		secInfo.setFloorCode("02");
		stockHandler.handle(secInfo);
		
		List<String> roomOfStocks = roomManager.getRoomOfStock("VND");
		Assert.assertEquals(1, roomOfStocks.size());
		Assert.assertEquals("02", roomOfStocks.get(0));
	}
	
	@Test
	public void testStockHandlerDontUpdateRoomOfStockWhenMarketOpen() {
		Mockito.when(marketSessionChecker.isClosedSession(Mockito.any())).thenReturn(false);
		SecInfo secInfo = new SecInfo();
		secInfo.setCode("VND");
		secInfo.setFloorCode("02");
		stockHandler.handle(secInfo);
		
		List<String> roomOfStocks = roomManager.getRoomOfStock("VND");
		Assert.assertEquals(0, roomOfStocks.size());
	}
	
}
