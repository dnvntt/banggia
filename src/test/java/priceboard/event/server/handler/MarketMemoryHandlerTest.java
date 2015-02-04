package priceboard.event.server.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.stock.compress.Mashaller;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

public class MarketMemoryHandlerTest {
	
	private MarketMemoryHandler memoryHandler;
	
	private InMemory memory;
	private Mashaller  mashaller;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		mashaller = new Mashaller();
		memoryHandler = new MarketMemoryHandler(memory,mashaller);
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

	@Test
	public void testCompressMarketInfo() {
		Market market = new Market();
		market.setFloorCode("02");
		market.setMarketIndex(123.4);
		market.setAdvance(13);
		market.setControlCode("5");
		market.setNoChange(3);
		market.setStatus("5");
		memoryHandler.handle(market);
		String compressionMarket = (String) memory.get("MARKET_COMPRESSION", "02");
		Assert.assertEquals("|0.0|0.0|0.0|13|0|3||0.0|||02|123.4|0.0|0.0|0.0||5|0|", compressionMarket);
	}
}
