package priceboard.rest.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

public class StockControllerTest {

	@Test
	public void testStockController() {
		InMemory memory = new InMemory();
		memory.put("STOCK_COMPRESSION", "VND", "data for VND");
		memory.put("STOCK_COMPRESSION", "SSI", "data for SSI");
		JsonParser parser = new JsonParser();
		StockController controller = new StockController(memory, parser);
		String json = controller.getStock("jsonp", "VND,SSI", new ModelMap());
		String expected = "jsonp([\"data for VND\",\"data for SSI\"])";
		Assert.assertEquals(expected, json);
	}
	
	@Test
	public void testStockControllerWithEmptyCodes() {
		InMemory memory = new InMemory();
		memory.put("STOCK_COMPRESSION", "VND", "data for VND");
		memory.put("STOCK_COMPRESSION", "SSI", "data for SSI");
		JsonParser parser = new JsonParser();
		StockController controller = new StockController(memory, parser);
		String json = controller.getStock("jsonp", "", new ModelMap());
		String expected = "";
		Assert.assertEquals(expected, json);
	}
}
