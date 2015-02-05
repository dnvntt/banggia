package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vn.com.vndirect.datafeed.util.MarketStatisMessage;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Transaction;

public class StatisticMemoryHandlerTest {
	
	private StatisticMemoryHandler memoryHandler;
	private InMemory memory;

	@Before
	public void setUp() {
		memory = new InMemory();
		memoryHandler = new StatisticMemoryHandler(memory);
	}

	@Test
	public void testStatisticMemoryHandler() {
		MarketStatisMessage statistic1 = new MarketStatisMessage();
		statistic1.setFloor("02");
		statistic1.setTradingDate("20150131");
		statistic1.setType("FLOOR");
		statistic1.setCount("5");
		 

		MarketStatisMessage statistic2 = new MarketStatisMessage();
		statistic2.setFloor("02");
		statistic2.setTradingDate("20150131");
		statistic2.setType("CEILING");
		statistic2.setCount("4");

		List<MarketStatisMessage> listOfStatistic = new ArrayList<MarketStatisMessage>();

		listOfStatistic.add(statistic1);
		listOfStatistic.add(statistic2);

		memoryHandler.handle(statistic1);
		memoryHandler.handle(statistic2);

		List<Transaction> statistics = (List<Transaction>)  memory.get("CeilingFloor", "ALL");

		Assert.assertEquals(listOfStatistic, statistics);
	}
}
