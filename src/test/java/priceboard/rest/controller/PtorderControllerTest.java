package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

public class PtorderControllerTest {
	private InMemory memory;
	private JsonParser jsonParser;
	private PtOrderController ptOrdercontroller;
	@Before
	public void setUp() {
		memory = new InMemory();
		jsonParser =new JsonParser();
		ptOrdercontroller = new PtOrderController(memory, jsonParser);		 
	}
	
	@Test
	public void testGetGDTTByFloorCodeWithoutDataInMemory() {
		Map<String, List<Map<String, Object>>>  ptOrderList = ptOrdercontroller.getPtOrder();		
		Assert.assertEquals(0, ptOrderList.size());
	}
	
	@Test
	public void testGetGDTTWithDataInMemory() {
		PutThroughTransaction tran1= new PutThroughTransaction();
		tran1.setFloorCode("02");
		tran1.setTime("20141225");
		
		
		PutThroughTransaction tran2= new PutThroughTransaction();
		tran2.setFloorCode("02");
		tran2.setTime("20141225");
		
		PutThroughTransaction tran3= new PutThroughTransaction();
		tran3.setFloorCode("03");
		tran3.setTime("20141225");
		
		List<PutThroughTransaction> transactions = new ArrayList<>();
		transactions.add(tran1);	
		transactions.add(tran2);
		transactions.add(tran3);
		
		memory.put("PutThroughTransaction","", transactions);
		
		Map<String, List<Map<String, Object>>>  ptOrderList = ptOrdercontroller.getPtOrder();		
		Assert.assertEquals(2, ptOrderList.size());
		Assert.assertTrue(ptOrderList.containsKey("02"));
	}
	@Test
	public void testGetGDTTByFloorCodeWithDataInMemory() {
		PutThroughTransaction tran1= new PutThroughTransaction();
		tran1.setFloorCode("02");
		tran1.setTime("20141225");
		
		List<PutThroughTransaction> transactions = new ArrayList<>();
		transactions.add(tran1);		
		
		memory.put("PutThroughTransaction","", transactions);
		
		Map<String, List<Map<String, Object>>>  ptOrderList = ptOrdercontroller.getPtOrder();		
		Assert.assertEquals(1, ptOrderList.size());
		
	}
}
