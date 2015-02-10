package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThrough;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class AdOrderControllerTest {
	private InMemory memory;
	private JsonParser jsonParser;
	private AdOrderController adOrdercontroller;
	@Before
	public void setUp() {
		memory = new InMemory();
		jsonParser =new JsonParser();
		adOrdercontroller = new AdOrderController(memory, jsonParser);		 
	}
	
	@Test
	public void testGetGDTTByFloorCodeWithoutDataInMemory() {
		Map<String, List<Map<String, Object>>>  adOrderList = adOrdercontroller.getPtOrder();		
		Assert.assertEquals(0, adOrderList.size());
	}
	
	@Test
	public void testGetGDTTWithDataInMemory() {
		PutThrough tran1= new PutThrough();
		tran1.setFloorCode("02");
		tran1.setStockSymbol("VND");
		tran1.setTime("20141225");
		
		
		PutThrough tran2= new PutThrough();
		tran2.setFloorCode("02");
		tran2.setStockSymbol("SHB");
		tran2.setTime("20141225");
		
		PutThrough tran3= new PutThrough();
		tran3.setFloorCode("10");
		tran3.setStockSymbol("HAG");
		tran3.setTime("20141225");
		
		List<PutThrough> t_hnxransactions = new ArrayList<>();
		t_hnxransactions.add(tran1);	
		t_hnxransactions.add(tran2);
		
		List<PutThrough> transactions_hose = new ArrayList<>();
		transactions_hose.add(tran3);
		
		memory.put("PutThrough","02", t_hnxransactions);
		memory.put("PutThrough","10", transactions_hose);
		
		Map<String, List<Map<String, Object>>>  adOrderList = adOrdercontroller.getPtOrder();		
		Assert.assertEquals(2, adOrderList.size());
		Assert.assertTrue(adOrderList.containsKey("02"));
	}
	
	@Test
	public void testGetGDTTByFloorCodeWithDataInMemory() {
		PutThrough tran1= new PutThrough();
		tran1.setFloorCode("02");
		tran1.setStockSymbol("HAG");
		tran1.setTime("20141225");
		
		List<PutThrough> transactions = new ArrayList<>();
		transactions.add(tran1);		
		
		memory.put("PutThrough","02", transactions);
		
		SecInfo stock1 = new SecInfo();
		stock1.setCode("HAG");
		stock1.setBasicPrice(21.0);
		stock1.setCeilingPrice(21.9);
		stock1.setFloorPrice(19.5);
		
		memory.put("STOCK", "HAG", stock1);
		
		Map<String, List<Map<String, Object>>>  adOrderList = adOrdercontroller.getPtOrder();		
		Assert.assertEquals(1, adOrderList.size());
		
	}
}
