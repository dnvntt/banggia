package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

public class CategoryLoaderTest {

	private ElasticSearchClient elasticSearchClient = Mockito.mock(ElasticSearchClient.class);
	
	private InMemory memory;

	private CategoryNameLoader loader;
	
	@Before
	public void setUp() {
		memory = new InMemory();
		loader = new CategoryNameLoader(elasticSearchClient, memory);
	}
	
	
	@Test
	public void testCategoryNameLoaderLoadData() throws Exception {
		List<Object> expectedListCategory = new ArrayList<Object>();
		Category Category1 = new Category();
		Category Category2 = new Category();
		expectedListCategory.add(Category1);
		expectedListCategory.add(Category2);
		
		Mockito.when(elasticSearchClient.getDataByIndex(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(expectedListCategory);
		loader.load();
		List<Object> listCategory = (List<Object>) memory.get("CATEGORY_LIST", "CATEGORY_LIST");
		Assert.assertEquals(expectedListCategory, listCategory);
		
	}
}
