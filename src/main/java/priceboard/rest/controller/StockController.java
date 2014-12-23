package priceboard.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Controller
@RequestMapping("/secinfo")
public class StockController {

	@Autowired
	private InMemory memory;
	
	@Autowired
	private JsonParser jsonParser;
	
	@Autowired
	public StockController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}
	
	@RequestMapping(value = "/snapshot/q=codes:{codes}", method = RequestMethod.GET)
	public @ResponseBody String getStock(@RequestParam("jsonp") String jsonp, @PathVariable String codes, ModelMap model) {
		if (isEmpty(codes)) return "";
		String[] arrCodes = codes.split(",");
		SecInfo[] secInfos = new SecInfo[arrCodes.length];
		int i = 0;
		for (String code : arrCodes) {
			if (code.trim().length() == 0) continue;
			Object secInfo = memory.get("STOCK", code);
			if (secInfo == null) continue;
			secInfos[i++] = (SecInfo) secInfo;
		}
		return jsonp + "(" + jsonParser.objectToString(secInfos) + ")";
	}

	private boolean isEmpty(String codes) {
		return codes == null || codes.trim().length() == 0;
	}
}


