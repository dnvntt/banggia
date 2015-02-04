package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.event.server.handler.StockMemoryHandler;
import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import priceboard.stock.compress.Mashaller;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class StockLoaderTest {

	private StockLoader stockLoader;
	
	private InMemory memory;
	
	private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	@Before
	public void setUp() {
		memory = new InMemory();
		stockLoader = new StockLoader(elasticSearchClient, memory);
		stockLoader.setHandlers(new ArrayList(){{
			add(new StockMemoryHandler(memory, new Mashaller()));
		}});
	}
	
	
	@Test
	public void testStockLoader() throws Exception {
		List<Object> listSecInfo = new ArrayList<Object>();
		SecInfo expectedSecInfo1 = new SecInfo();
		expectedSecInfo1.setCode("VND");
		SecInfo expectedSecInfo2 = new SecInfo();
		expectedSecInfo2.setCode("SSI");
		listSecInfo.add(expectedSecInfo1);
		listSecInfo.add(expectedSecInfo2);
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(listSecInfo);
		stockLoader.load();
		Object secInfo1 = memory.get("STOCK", "VND");
		Object secInfo2 = memory.get("STOCK", "SSI");
		
		Assert.assertEquals(expectedSecInfo1, secInfo1);
		Assert.assertEquals(expectedSecInfo2, secInfo2);
		
		Object compressionSecInfo1 = memory.get("STOCK_COMPRESSION", "VND");
		Object compressionSecInfo2 = memory.get("STOCK_COMPRESSION", "SSI");
		
		Assert.assertNotNull(compressionSecInfo1);
		Assert.assertNotNull(compressionSecInfo2);
	}
}
