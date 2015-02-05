package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

public class PtorderLoaderTest {
	private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	private InMemory memory;
	
	@Before
	public void setup() {
		memory = new InMemory();
	}
	
	@Test
	public void testPtorderLoaderIntraday() throws Exception {
		List<Object> expectedPutThroughTransactionList = new ArrayList<Object>();
		
		PutThroughTransaction putThrough1 = new PutThroughTransaction();
		putThrough1.setFloorCode("02");
		putThrough1.setSymbol("VND");
		putThrough1.setVolume(2300.0); 
		
		expectedPutThroughTransactionList.add(putThrough1);
		
		PutThroughTransaction putThrough2 = new PutThroughTransaction();
		putThrough2.setFloorCode("02");
		putThrough2.setSymbol("SAM");
		putThrough2.setVolume(3300.0); 
		
		expectedPutThroughTransactionList.add(putThrough2);
		
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(expectedPutThroughTransactionList);
		PtorderLoader loader = new PtorderLoader(elasticSearchClient, memory);
		
		loader.load();
		
		List<PutThroughTransaction> putThroughTransactionList = (List<PutThroughTransaction>) memory.get("PutThroughTransaction", "02");
		
		Assert.assertEquals(expectedPutThroughTransactionList, putThroughTransactionList);
		
	}
}
