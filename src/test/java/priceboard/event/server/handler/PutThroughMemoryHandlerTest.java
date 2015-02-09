package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThrough;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class PutThroughMemoryHandlerTest {
	private InMemory memory;
	private PutThroughMemoryHandler memoryHandler;

	@Before
	public void setUp() {
		memory = new InMemory();
		memoryHandler = new PutThroughMemoryHandler(memory);
	}

	@Test
	public void testPutThroughMemoryHandler() {
		PutThrough pushThroughTransactionInfo = new PutThrough();
		pushThroughTransactionInfo.setStockSymbol("SAM");
		pushThroughTransactionInfo.setVol(5120.0);
		pushThroughTransactionInfo.setPrice(14000.0);
		pushThroughTransactionInfo.setFloorCode("10");
		pushThroughTransactionInfo.setTradingDate("2015019");

		PutThrough pushThroughTransactionInfo1 = new PutThrough();
		pushThroughTransactionInfo1.setStockSymbol("SSI");
		pushThroughTransactionInfo1.setVol(6120.0);
		pushThroughTransactionInfo1.setPrice(24000.0);
		pushThroughTransactionInfo1.setFloorCode("10");
		pushThroughTransactionInfo1.setTradingDate("2015019");

		List<PutThrough> listOfPutThrough = new ArrayList<PutThrough>();

		listOfPutThrough.add(pushThroughTransactionInfo);
		listOfPutThrough.add(pushThroughTransactionInfo1);

		SecInfo stock1 = new SecInfo();
		stock1.setCode("SAM");
		stock1.setBasicPrice(8.0);
		stock1.setCeilingPrice(8.8);
		stock1.setFloorPrice(7.5);
		 
		SecInfo stock2 = new SecInfo();
		stock2.setCode("SSI");
		stock2.setBasicPrice(21.0);
		stock2.setCeilingPrice(21.9);
		stock2.setFloorPrice(19.5);
		
		memory.put("STOCK", "SAM", stock1);
		memory.put("STOCK", "SSI", stock2);
		
		memoryHandler.handle(pushThroughTransactionInfo);
		memoryHandler.handle(pushThroughTransactionInfo1);

		List<PutThrough> putThrough = (List<PutThrough>) memory.get("PutThrough", "10");
		Assert.assertEquals(listOfPutThrough, putThrough);
	}
}
