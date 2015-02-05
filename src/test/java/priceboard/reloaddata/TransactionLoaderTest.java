package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Transaction;

public class TransactionLoaderTest {
	private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	private InMemory memory;
	
	@Before
	public void setup() {
		memory = new InMemory();
	}
	
	@Test
	public void testLoadMarketDataIntraday() throws Exception {
		List<Object> expectedTransactionList = new ArrayList<Object>();
		
		Transaction trans1 = new Transaction();
		trans1.setFloorCode("02");
		trans1.setSymbol("VND");
		trans1.setLastVol(2300.0);
		trans1.setLast(12.0);
		
		expectedTransactionList.add(trans1);
		
		Transaction trans2 = new Transaction();
		trans2.setFloorCode("02");
		trans2.setSymbol("VND");
		trans2.setLastVol(1300.0);
		trans2.setLast(12.3);
		
		expectedTransactionList.add(trans2);
		
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(expectedTransactionList);
		TransactionLoader loader = new TransactionLoader(elasticSearchClient, memory);
		
		loader.load();
		
		List<Object> transactionList = (List<Object>) memory.get("TRANSACTION", "VND");
		
		Assert.assertEquals(expectedTransactionList, transactionList);
		
	}
}
