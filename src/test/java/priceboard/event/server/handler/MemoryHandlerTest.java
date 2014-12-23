package priceboard.event.server.handler;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class MemoryHandlerTest {
	
	private MemoryHandler memoryHandler;
	
	private InMemory memory;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		memoryHandler = new MemoryHandler(memory);
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
	
	@Test
	public void testMemoryHandlerWithMarket() {
		Market market = new Market();
		market.setFloorCode("02");
		market.setAdvance(0);
		market.setControlCode("13");
		market.setStatus("10");
		market.setMarketIndex(123.5);
		memoryHandler.handle(market);
		Market marketInMemory = (Market)memory.get("MARKET", "02");
		Assert.assertEquals(market, marketInMemory);
	}
}
