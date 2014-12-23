package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

public class CompanyNameLoaderTest {

	private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	private InMemory memory;

	private CompanyNameLoader loader;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		loader = new CompanyNameLoader(elasticSearchClient, memory);
	}
	
	
	@Test
	public void testCompanyNameLoaderLoadData() throws Exception {
		List<Object> expectedListCompany = new ArrayList<Object>();
		Company company1 = new Company();
		company1.setCode("VND");
		company1.setCompanyName("CT chung khoan VNDIRECT");
		Company company2 = new Company();
		company2.setCode("SSI");
		company2.setCompanyName("CT chung khoan SSI");
		expectedListCompany.add(company1);
		expectedListCompany.add(company2);
		
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(expectedListCompany);
		loader.load();
		List<Object> listCompany = (List<Object>) memory.get("COMPANY_LIST", "COMPANY_LIST");
		Assert.assertEquals(expectedListCompany, listCompany);
		
	}
}
