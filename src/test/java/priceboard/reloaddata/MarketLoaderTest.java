package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

public class MarketLoaderTest {

	private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	private InMemory memory;
	
	@Before
	public void setup() {
		memory = new InMemory();
	}
	
	@Test
	public void testLoadMarketDataIntraday() throws Exception {
		List<Object> expectedMarketList = new ArrayList<Object>();
		
		Market market = new Market();
		market.setFloorCode("02");
		market.setTradingTime("091909");
		
		expectedMarketList.add(market);
		
		market = new Market();
		market.setFloorCode("02");
		market.setTradingTime("090909");
		expectedMarketList.add(market);
		
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(expectedMarketList);
		MarketLoader loader = new MarketLoader(elasticSearchClient, memory);
		
		loader.load();
		
		List<Object> marketList = (List<Object>) memory.get("ALL_MARKET", "02");
		
		Assert.assertEquals(expectedMarketList, marketList);
		
	}
	
	
	@Test
	public void testLoadMarketDataSnapshot() throws Exception {
		List<Object> expectedMarketList = new ArrayList<Object>();
		
		Market expectedMarket = new Market();
		expectedMarket.setFloorCode("02");
		expectedMarket.setTradingTime("090909");
		
		expectedMarketList.add(expectedMarket);
		
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(expectedMarketList);
		MarketLoader loader = new MarketLoader(elasticSearchClient, memory);
		
		loader.load();
		
		Market market = (Market) memory.get("MARKET", "02");
		
		Assert.assertEquals(expectedMarket, market);
		
		
		
		
	}
}
