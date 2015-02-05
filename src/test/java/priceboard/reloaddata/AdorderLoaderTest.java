package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThrough;

public class AdorderLoaderTest {
	private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	private InMemory memory;
	
	@Before
	public void setup() {
		memory = new InMemory();
	}
	
	@Test
	public void testAdorderLoaderIntraday() throws Exception {
		List<Object> expectedPutThroughList = new ArrayList<Object>();
		
		PutThrough putThrough1 = new PutThrough();
		putThrough1.setFloorCode("02");
		putThrough1.setStockSymbol("VND");
		putThrough1.setVol(2300); 
		
		expectedPutThroughList.add(putThrough1);
		
		PutThrough putThrough2 = new PutThrough();
		putThrough2.setFloorCode("02");
		putThrough2.setStockSymbol("SAM");
		putThrough2.setVol(3300); 
		
		expectedPutThroughList.add(putThrough2);
		
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(expectedPutThroughList);
		AdorderLoader loader = new AdorderLoader(elasticSearchClient, memory);
		
		loader.load();
		
		List<PutThrough> putThroughList = (List<PutThrough>) memory.get("PutThrough", "02");
		
		Assert.assertEquals(expectedPutThroughList, putThroughList);
		
	}
	
}
