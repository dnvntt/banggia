package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.stock.compress.Mashaller;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.FloorCode;
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
	
	@Test
	public void testSaveMarketHistory() {
		
		memory.remove("MARKET", FloorCode.HNX30.getCode());
		memory.remove("MARKET_COMPRESSION", FloorCode.HNX30.getCode());
		memory.remove("ALL_MARKET", FloorCode.HNX30.getCode());
		
		Market market = new Market();
		market.setFloorCode("12");
		market.setPriorMarketIndex(524.6);
		market.setBidVol(34321.7);
		market.setTradingTime("093005");
		
		Market market2 = new Market();
		market2.setFloorCode("12");
		market2.setPriorMarketIndex(524.6);
		market2.setBidVol(34321.7);
		market2.setTradingTime("093005");
		
		Market market3 = new Market();
		market3.setFloorCode("12");
		market3.setPriorMarketIndex(524.6);
		market3.setBidVol(34321.7);
		market3.setTradingTime("093005");
		
		Market market4 = new Market();
		market4.setFloorCode("12");
		market4.setPriorMarketIndex(524.6);
		market4.setBidVol(34321.7);
		market4.setTradingTime("093005");
		
		memoryHandler.handle(market);
		memoryHandler.handle(market2);
		memoryHandler.handle(market3);
		memoryHandler.handle(market4);
		
		String code = "12";
		List<Market> marketList = (ArrayList<Market>) memory.get("ALL_MARKET", code);
		if (marketList == null || code.trim().length() == 0) {
			return;
		}
		Collections.sort(marketList, new Comparator<Market>() {
			@Override
			public int compare(Market o1, Market o2) {
				return o1.getTradingTime().compareTo(o2.getTradingTime());
			}
		});
		
		Assert.assertEquals(market, marketList.get(1));
	}
}
