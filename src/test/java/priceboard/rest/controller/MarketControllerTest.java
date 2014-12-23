package priceboard.rest.controller;


import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

public class MarketControllerTest {

	@Test	
	public void testMarketControllerBuildData() {
		InMemory memory = new InMemory();
		JsonParser parser = new JsonParser();
		MarketController controller = new MarketController(memory, parser);
		Market market = new Market();
		market.setTradingTime("13:52:37");
		market.setFloorCode("10");
		market.setAdvance(100);
		memory.put("MARKET", "10", market);
		Map<String, Map<String, Object>>  marketData = controller.getMarket("10", new ModelMap());
		Assert.assertNotNull(marketData.get("10"));
	}
}
