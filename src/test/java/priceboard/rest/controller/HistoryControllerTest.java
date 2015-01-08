package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

public class HistoryControllerTest {
	
	private HistoryController historyController;
	private InMemory memory;
	private JsonParser jsonParser;
	@Before
	public void setUp() {
		memory = new InMemory();
		jsonParser =new JsonParser();
		historyController = new HistoryController(memory, jsonParser);
	}
	
	@Test
	public void testGetHistoryOfMarketByFloorCodeWithoutDataInMemory() {
		String floorCode = "02";
		Map<String, Map<String, List<String>>> results = historyController.getMarketHistory(floorCode, new ModelMap());
		Assert.assertEquals(0, results.size());
	}

	@Test
	public void testGetHistoryOfMarketByFloorCode() {
		String floorCode = "02";
		Market market1 = new Market();
		market1.setFloorCode(floorCode);
		market1.setMarketIndex(454.5);
		market1.setTradingTime("09:00:11");
		
		Market market2 = new Market();
		market2.setFloorCode(floorCode);
		market2.setMarketIndex(234.5);
		market2.setTradingTime("09:10:11");
		
		List<Market> markets = new ArrayList<>();
		markets.add(market1);
		markets.add(market2);
	 
		memory.put("ALL_MARKET", "02", markets);
		
		Map<String, Map<String, List<String>>>  results = historyController.getMarketHistory(floorCode, new ModelMap());
		Assert.assertEquals(1, results.size());
		Assert.assertTrue(results.containsKey("02"));
	}
	
	
	@Test
	public void testGetHistoryOfMarketByNonExistFloorCode() {
		String floorCode = "02";
		Market market1 = new Market();
		market1.setFloorCode(floorCode);
		market1.setMarketIndex(444.5);
		
		Market market2 = new Market();
		market2.setFloorCode(floorCode);
		market2.setMarketIndex(234.5);
		List<Market> markets = new ArrayList<>();
		markets.add(market1);
		markets.add(market2);
		
		memory.put("ALL_MARKET", "02", markets);
		
		Map<String, Map<String, List<String>>> results = historyController.getMarketHistory("03", new ModelMap());
		Assert.assertEquals(0, results.size());
	}
	
	@Test
	public void testGetHistoryOfMarket() {
		Market market1 = new Market();
		market1.setFloorCode("02");
		market1.setMarketIndex(444.5);
		
		Market market2 = new Market();
		market2.setFloorCode("10");
		market2.setMarketIndex(234.5);
		List<Market> markets1 = new ArrayList<>();
		markets1.add(market1);
		List<Market> markets2 = new ArrayList<>();
		markets2.add(market2);
		
		memory.put("ALL_MARKET", "02", markets1);
		memory.put("ALL_MARKET", "10", markets2);
		
		Map<String, Map<String, List<String>>> results = historyController.getMarketHistory("10", new ModelMap());
		Assert.assertEquals(1, results.size());
		//Assert.assertEquals("10", results.get(0).getFloorCode());
	}
	
	@Test
	public void testGetHistoryOfMarkets() {
		Market market1 = new Market();
		market1.setFloorCode("02");
		market1.setMarketIndex(444.5);
		
		Market market2 = new Market();
		market2.setFloorCode("10");
		market2.setMarketIndex(234.5);
		List<Market> markets1 = new ArrayList<>();
		markets1.add(market1);
		List<Market> markets2 = new ArrayList<>();
		markets2.add(market2);
		
		memory.put("ALL_MARKET", "02", markets1);
		memory.put("ALL_MARKET", "10", markets2);
		
		Map<String, Map<String, List<String>>> results = historyController.getMarketHistory("02,10", new ModelMap());
		Assert.assertEquals(2, results.size());
	}
	
}
