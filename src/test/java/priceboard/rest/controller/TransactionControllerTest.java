package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;
import vn.com.vndirect.priceservice.datamodel.Transaction;

public class TransactionControllerTest {
	private InMemory memory;
	private JsonParser jsonParser;
	private TransactionController transactionController;
	@Before
	public void setUp() {
		memory = new InMemory();
		jsonParser =new JsonParser();
		transactionController = new TransactionController(memory, jsonParser);		 
	}
	

	@Test
	public void testTransaction()
	{
		String stockCode="HAG";
		List<Transaction>  result = transactionController.getTransactionHistory(stockCode, new ModelMap());
		Assert.assertEquals(0, result.size());
	}
	
	@Test
	public void testTransactionbyCode()
	{
		String stockCode="HAG";
		Transaction trans1= new Transaction();
		trans1.setFloorCode("02");
		trans1.setSymbol("HAG");
		trans1.setLast(23.4); 
		
		
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(trans1);	
		 
		memory.put("TRANSACTION",trans1.getSymbol(), transactions);
		
		List<Transaction>  result = transactionController.getTransactionHistory("HAG", new ModelMap());
		Assert.assertEquals(1, result.size());
	}
}
