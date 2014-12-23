package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import priceboard.json.JsonParser;
import priceboard.reloaddata.Category;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@Controller
@RequestMapping("/category")
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
	public @ResponseBody String getCategoryForOldVersion(@RequestParam("jsonp") String jsonp, ModelMap model) {
		Object categoryList = memory.get("CATEGORY_LIST", "CATEGORY_LIST");
		if (categoryList == null) {
			return jsonp + "()";
		}
		
		List<CustomCategory> customCategoryList = new ArrayList<CustomCategory>();
		for(Category category : (List<Category>) categoryList) {
			CustomCategory customCategory = new CustomCategory();
			customCategory.setCateName(category.getCategoryName());
			customCategory.setStockCodes(category.getCodeList());
			customCategoryList.add(customCategory);
			
		}
		return jsonp + "(" + jsonParser.objectToString(customCategoryList) + ")";
	}
	
	@RequestMapping(value = "/snapshot/new/", method = RequestMethod.GET)
	public @ResponseBody String getCategory(@RequestParam("jsonp") String jsonp, ModelMap model) {
		Object categoryList = memory.get("CATEGORY_LIST", "CATEGORY_LIST");
		if (categoryList == null) {
			return jsonp + "()";
		}
		
		return jsonp + "(" + jsonParser.objectToString(categoryList) + ")";
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
