package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.datafeed.util.MarketStatisMessage;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

public class CeilingFloorLoaderTest {
private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	private InMemory memory;
	
	@Before
	public void setup() {
		memory = new InMemory();
	}
	
	@Test
	public void testLoadCeilingFloorIntraday() throws Exception {
		List<Object> expectedMarketList = new ArrayList<Object>();
		
		MarketStatisMessage market = new MarketStatisMessage();
		market.setFloor("02");market.setType("CEILING");
		
		expectedMarketList.add(market);
		
		MarketStatisMessage market2 = new MarketStatisMessage();
		market2.setFloor("02");market2.setType("FLOOR");
		expectedMarketList.add(market2);
		
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(expectedMarketList);
		CeilingFloorLoader loader = new CeilingFloorLoader(elasticSearchClient, memory);
		
		loader.load();
		
		List<Object> marketList = (List<Object>) memory.get("CeilingFloor", "ALL");
		
		Assert.assertEquals(expectedMarketList, marketList);
		
		
	}
}
