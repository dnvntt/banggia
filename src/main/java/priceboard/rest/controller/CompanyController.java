package priceboard.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@RestController
@RequestMapping("/priceservice/company")
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
	public @ResponseBody Object getCompany(ModelMap model) {
		return memory.get("COMPANY_LIST", "COMPANY_LIST");
	}

}
