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
		tran1.setTime("20141225");
		
		
		PutThrough tran2= new PutThrough();
		tran2.setFloorCode("02");
		tran2.setTime("20141225");
		
		PutThrough tran3= new PutThrough();
		tran3.setFloorCode("03");
		tran3.setTime("20141225");
		
		List<PutThrough> transactions = new ArrayList<>();
		transactions.add(tran1);	
		transactions.add(tran2);
		transactions.add(tran3);
		
		memory.put("PutThrough","", transactions);
		
		Map<String, List<Map<String, Object>>>  adOrderList = adOrdercontroller.getPtOrder();		
		Assert.assertEquals(2, adOrderList.size());
		Assert.assertTrue(adOrderList.containsKey("02"));
	}
	@Test
	public void testGetGDTTByFloorCodeWithDataInMemory() {
		PutThrough tran1= new PutThrough();
		tran1.setFloorCode("02");
		tran1.setTime("20141225");
		
		List<PutThrough> transactions = new ArrayList<>();
		transactions.add(tran1);		
		
		memory.put("PutThrough","", transactions);
		
		Map<String, List<Map<String, Object>>>  adOrderList = adOrdercontroller.getPtOrder();		
		Assert.assertEquals(1, adOrderList.size());
		
	}
}
