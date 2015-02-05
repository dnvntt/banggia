package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

public class PtOrderMemoryHandlerTest {
private PtOrderMemoryHandler memoryHandler;
	
	private InMemory memory;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		memoryHandler = new PtOrderMemoryHandler(memory);
	}
	
 
	@Test
	public void testPtOrderMemoryHandler() {
		PutThroughTransaction pushThroughTransactionInfo = new PutThroughTransaction();
		pushThroughTransactionInfo.setSymbol("SAM");
		pushThroughTransactionInfo.setVolume(4120.0);
		pushThroughTransactionInfo.setPrice(14000.0);
		pushThroughTransactionInfo.setFloorCode("10");
		pushThroughTransactionInfo.setTradingDate("2015019");
		
		
		PutThroughTransaction pushThroughTransactionInfo1 = new PutThroughTransaction();
		pushThroughTransactionInfo1.setSymbol("SSI");
		pushThroughTransactionInfo1.setVolume(6120.0);
		pushThroughTransactionInfo1.setPrice(24000.0);
		pushThroughTransactionInfo1.setFloorCode("10");
		pushThroughTransactionInfo1.setTradingDate("2015019");
		
		List<PutThroughTransaction> listOfPt = new ArrayList<PutThroughTransaction>();
		
		listOfPt.add(pushThroughTransactionInfo);
		listOfPt.add(pushThroughTransactionInfo1);
		
		
		memoryHandler.handle(pushThroughTransactionInfo);
		memoryHandler.handle(pushThroughTransactionInfo1);
		
		List<PutThroughTransaction> putThroughTransactions = (List<PutThroughTransaction>) memory.get("PutThroughTransaction", "10");
		Assert.assertEquals(listOfPt, putThroughTransactions);
	}
}
