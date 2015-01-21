package priceboard.event.server.handler;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class StockMemoryHandlerTest {
	
	private StockMemoryHandler memoryHandler;
	
	private InMemory memory;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		memoryHandler = new StockMemoryHandler(memory);
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
		memoryHandler.handle(secInfo);
		SecInfo secInfoInMemory = (SecInfo)memory.get("STOCK", "HAG");
		Assert.assertEquals(secInfo, secInfoInMemory);
	}
 
}
