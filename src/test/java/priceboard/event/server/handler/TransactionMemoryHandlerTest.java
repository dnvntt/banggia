package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Transaction;

public class TransactionMemoryHandlerTest {

	private TransactionMemoryHandler memoryHandler;
	
	private InMemory memory;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		memoryHandler = new TransactionMemoryHandler(memory);
	}
	
 
	@Test
	public void testTransactionMemoryHandler() {
		Transaction trans1= new Transaction();
		trans1.setFloorCode("02");
		trans1.setTime("132500");
		trans1.setSymbol("HAG");
		trans1.setLast(23.4); 
		trans1.setLastVol(32440);
		
		Transaction trans2= new Transaction();
		trans2.setFloorCode("02");
		trans2.setTime("134500");
		trans2.setSymbol("HAG");
		trans2.setLast(24.4); 
		trans2.setLastVol(12440);
		
		List<Transaction>  listOfTransaction= new ArrayList<Transaction>();
		
		listOfTransaction.add(trans1);
		listOfTransaction.add(trans2);
		
		memoryHandler.handle(trans1);
		memoryHandler.handle(trans2);
		
		List<Transaction> transactions = (List<Transaction>)memory.get("TRANSACTION", "HAG");
		
		Assert.assertEquals(listOfTransaction, transactions);
	}
}
