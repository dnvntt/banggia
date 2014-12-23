package priceboard.rest.controller;

import java.util.HashMap;
import java.util.Map;

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

@Controller
@RequestMapping("/market")
public class MarketController {

	@Autowired
	private InMemory memory;
	
	@Autowired
	private JsonParser jsonParser;

	private Map<String, Map<String, Object>> marketMapByFloorCode = new HashMap<String, Map<String, Object>>();
	
	@Autowired
	public MarketController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}
	
	@RequestMapping(value = "/snapshot/q=codes:{codes}", method = RequestMethod.GET)
	public @ResponseBody String getMarket(@RequestParam("jsonp") String jsonp, @PathVariable String codes, ModelMap model) {
		if (isEmpty(codes)) return "";
		String[] arrCodes = codes.split(",");
		for (String code : arrCodes) {
			if (code.trim().length() == 0) continue;
			Object market = memory.get("MARKET", code);
			if (market == null) continue;
			putData((Market) market);
		}
		return jsonp + "(" + jsonParser.objectToString(marketMapByFloorCode) + ")";
	}

	private boolean isEmpty(String codes) {
		return codes == null || codes.trim().length() == 0;
	}
	
	public void putData(Market market) {
		marketMapByFloorCode.put(market.getFloorCode(), new HashMap<String, Object>() {{
			put("crrTime", market.getTradingTime());
			put("data", market);
		}});
	}
}