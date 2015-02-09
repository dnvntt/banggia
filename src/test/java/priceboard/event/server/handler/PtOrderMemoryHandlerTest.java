package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

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
		
		SecInfo stock2 = new SecInfo();
		stock2.setCode("SAM");
		stock2.setBasicPrice(21.0);
		stock2.setCeilingPrice(21.9);
		stock2.setFloorPrice(19.5);
		
		SecInfo stock1 = new SecInfo();
		stock1.setCode("SSI");
		stock1.setBasicPrice(8.0);
		stock1.setCeilingPrice(8.8);
		stock1.setFloorPrice(7.5);
		 
		memory.put("STOCK", "SSI", stock1);
		memory.put("STOCK", "SAM", stock2);
		
		
		memoryHandler.handle(pushThroughTransactionInfo);
		memoryHandler.handle(pushThroughTransactionInfo1);
		
		List<PutThroughTransaction> putThroughTransactions = (List<PutThroughTransaction>) memory.get("PutThroughTransaction", "10");
		Assert.assertEquals(listOfPt, putThroughTransactions);
	}
}
