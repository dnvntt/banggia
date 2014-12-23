package priceboard.reloaddata;

import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.BussinessDateLoader;
import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.web.commons.utility.DateUtils;

public class BusinessDateLoaderTest {

	private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testLoadBusinessDate() throws Exception {
		InMemory memory = new InMemory();
		BussinessDateLoader loader = new BussinessDateLoader(elasticSearchClient, memory);
		Date inputDate = new Date();
		BusinessDate expectedBusinessDate = new BusinessDate();
		expectedBusinessDate.setBusinessDate(DateUtils.dateToString(inputDate));
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(expectedBusinessDate));
		loader.load();
		String businessDate = (String) memory.get("businessdate", "businessdate");
		Assert.assertEquals(expectedBusinessDate.getBusinessDate(), businessDate);
	}
}

