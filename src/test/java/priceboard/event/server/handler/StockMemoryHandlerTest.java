package priceboard.event.server.handler;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.stock.compress.Mashaller;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class StockMemoryHandlerTest {
	
	private StockMemoryHandler stockMemoryHandler;
	
	private InMemory memory;
	private Mashaller  mashaller;
	@Before
	public void setUp() {
		memory = new InMemory();
		mashaller = new Mashaller();
		stockMemoryHandler = new StockMemoryHandler(memory,mashaller);
	}
	
	@Test
	public void testMemoryHandlerWithStock() {
		SecInfo secInfo = new SecInfo();
		secInfo.setCode("HAG");
		secInfo.setBasicPrice(10.5);
		secInfo.setFloorPrice(10);;
		secInfo.setCompanyName("Hoang Anh Gia Lai");
		secInfo.setCurrentRoom(3000);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		secInfo.setTradingDate(cal.getTime());
		stockMemoryHandler.handle(secInfo);
		SecInfo secInfoInMemory = (SecInfo)memory.get("STOCK", "HAG");
		Assert.assertEquals(secInfo, secInfoInMemory);
	}
	@Test
	public void testCompressSecInfo() {
		SecInfo secInfo = new SecInfo();
		secInfo.setCode("HAG");
		secInfo.setBasicPrice(10.5);
		secInfo.setFloorPrice(10);;
		secInfo.setCeilingPrice(12);
		secInfo.setFloorCode("10");
		secInfo.setAccumulatedVal(12000);
		secInfo.setAccumulatedVal(120);
		secInfo.setMatchPrice(10);
		secInfo.setMatchQtty(4000);
		secInfo.setBidPrice01(12);
		secInfo.setBidPrice01(11);
		secInfo.setBidQtty01(200);
		secInfo.setBidPrice01(10);
		secInfo.setBidQtty01(200);
		secInfo.setOfferPrice01(10);
		secInfo.setOfferQtty01(3000);
		secInfo.setOfferPrice02(11);
		secInfo.setOfferQtty02(3000);
		secInfo.setOfferPrice03(11.5);
		secInfo.setOfferQtty03(3000);
		secInfo.setCompanyName("Hoang Anh Gia Lai");
		secInfo.setCurrentRoom(3000);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		secInfo.setTradingDate(cal.getTime());
		
		stockMemoryHandler.handle(secInfo);
		
		String compressionSecInfo = (String) memory.get("STOCK_COMPRESSION", "HAG");
		
		Assert.assertEquals("10|20/11/2014||HAG|Hoang Anh Gia Lai||0.0|3000.0|10.5|0.0|0.0|0.0|0.0|0.0|0.0|12.0|10.0|0.0|0.0|10.0|4000.0|0.0|0.0|10.0|200.0|0.0|0.0|0.0|0.0|10.0|3000.0|11.0|3000.0|11.5|3000.0|120.0|0.0|0.0|0.0|0.0||", compressionSecInfo);
	}
	
 
}
