package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import priceboard.json.JsonParser;
import priceboard.reloaddata.Category;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

public class CategoryControllerTest {

	private InMemory memory;

	@Before
	public void setUp() {
		memory = new InMemory();
	}
	
	@Test
	public void testCategoryControlerReturnCategoryListInJsonp() {
		List<Object> listCategory = new ArrayList<Object>();
		Category category1 = new Category();
		category1.setCategoryName("Category 1");
		category1.setCodeList(new ArrayList<String>() {{add("VND"); add("SSI");}});
		Category category2 = new Category();
		category2.setCategoryName("Category 2");
		category2.setCodeList(new ArrayList<String>() {{add("HAG"); add("VCB");}});
		listCategory.add(category1);
		listCategory.add(category2);
		
		memory.put("CATEGORY_LIST", "CATEGORY_LIST", listCategory);
		
		CategoryController categoryController = new CategoryController(memory, new JsonParser());
		
		Object returnJsonp = categoryController.getCategory("jsonp", new ModelMap());
		Assert.assertEquals(category1, ((List) returnJsonp).get(0));
		Assert.assertEquals(category2, ((List) returnJsonp).get(1));
	}
}
