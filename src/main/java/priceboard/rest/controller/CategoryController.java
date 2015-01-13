package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import priceboard.json.JsonParser;
import priceboard.reloaddata.Category;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@RestController
@RequestMapping("/priceservice/category")
public class CategoryController {

	@Autowired
	private InMemory memory;

	@Autowired
	private JsonParser jsonParser;

	@Autowired
	public CategoryController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}

	@RequestMapping(value = "/snapshot/", method = RequestMethod.GET)
	public @ResponseBody List<CustomCategory> getCategoryForOldVersion(
			ModelMap model) {
		Object categoryList = memory.get("CATEGORY_LIST", "CATEGORY_LIST");
		List<CustomCategory> customCategoryList = new ArrayList<CustomCategory>();
		for (Category category : (List<Category>) categoryList) {
			CustomCategory customCategory = new CustomCategory();
			customCategory.setCateName(category.getCategoryName());
			Collections.sort(category.getCodeList());
			customCategory.setStockCodes(category.getCodeList());
			customCategoryList.add(customCategory);

		}
		return customCategoryList;
	}

	@RequestMapping(value = "/snapshot/new/", method = RequestMethod.GET)
	public @ResponseBody Object getCategory(
			@RequestParam("jsonp") String jsonp, ModelMap model) {
		Object categoryList = memory.get("CATEGORY_LIST", "CATEGORY_LIST");
		return categoryList;
	}

}

class CustomCategory {
	private String cateName;
	private List<String> stockCodes;

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public List<String> getStockCodes() {
		return stockCodes;
	}

	public void setStockCodes(List<String> stockCodes) {
		this.stockCodes = stockCodes;
	}
}
