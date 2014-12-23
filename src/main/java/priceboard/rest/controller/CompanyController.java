package priceboard.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;


@Controller
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private InMemory memory;
	
	@Autowired
	private JsonParser jsonParser;
	
	@Autowired
	public CompanyController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}
		
	@RequestMapping(value = "/snapshot/", method = RequestMethod.GET)
	public @ResponseBody String getCompany(@RequestParam("jsonp") String jsonp, ModelMap model) {
		Object companyList = memory.get("COMPANY_LIST", "COMPANY_LIST");
		if (companyList == null) {
			return jsonp + "()";
		}
		
		return jsonp + "(" + jsonParser.objectToString(companyList) + ")";
	}

}
