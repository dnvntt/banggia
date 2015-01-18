package priceboard.event.server.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

public class MarketMemoryHandlerTest {
	
	private MarketMemoryHandler memoryHandler;
	
	private InMemory memory;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		memoryHandler = new MarketMemoryHandler(memory);
	}
	
 
	@Test
	public void testMemoryHandlerWithMarket() {
		Market market = new Market();
		market.setFloorCode("02");
		market.setAdvance(0);
		market.setControlCode("13");
		market.setStatus("10");
		market.setMarketIndex(123.5);
		memoryHandler.handle(market);
		Market marketInMemory = (Market)memory.get("MARKET", "02");
		Assert.assertEquals(market, marketInMemory);
	}
}
