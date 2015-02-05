package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.json.JsonParser;
import vn.com.vndirect.datafeed.util.MarketStatisMessage;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

public class CeilingFloorCountControllerTest {
	private CeilingFloorCountController ceilingFloorCountController;
	private InMemory memory;
	private JsonParser jsonParser;
	@Before
	public void setUp() {
		memory = new InMemory();
		jsonParser =new JsonParser();
		ceilingFloorCountController = new CeilingFloorCountController(memory, jsonParser);
	}
	
	@Test
	public void testGetCeilingFloorCountByFloorCodeWithoutDataInMemory() {
		Map<String, Map<String,String>> results = ceilingFloorCountController.getCeilingFloor();
		Assert.assertNull(results);
	}

	@Test
	public void testGetCeilingFloorCountByFloorCode() {
		String floorCode = "02";
		List<Object> expectedMarketList = new ArrayList<Object>();
		MarketStatisMessage market = new MarketStatisMessage();
		market.setFloor(floorCode);market.setType("CEILING");
		
		expectedMarketList.add(market);
		
		MarketStatisMessage market2 = new MarketStatisMessage();
		market2.setFloor(floorCode);market2.setType("FLOOR");
		expectedMarketList.add(market2);
		
	 
		memory.put("CeilingFloor", "ALL", expectedMarketList);
		
		Map<String, Map<String, String>>  results = ceilingFloorCountController.getCeilingFloor();
		Assert.assertEquals(2, results.size());
		Assert.assertTrue(results.containsKey("ceiling"));
		Assert.assertTrue(results.containsKey("floor"));
	}
}
