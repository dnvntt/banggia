package priceboard.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import priceboard.json.JsonParser;
import vn.com.vndirect.datafeed.util.MarketStatisMessage;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@Controller
@RequestMapping("/priceservice/ceilingfloorcount")
public class CeilingFloorCountController {

	@Autowired
	private InMemory memory;

	@Autowired
	private JsonParser jsonParser;

	@Autowired
	public CeilingFloorCountController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}

	@RequestMapping(value = "/snapshot/", method = RequestMethod.GET)
	public @ResponseBody Map<String, Map<String, String>> getCeilingFloor() {
		Map<String, Map<String, String>> marketCeilingFloor = new HashMap<String, Map<String, String>>();
		List<MarketStatisMessage> marketList = (List<MarketStatisMessage>) memory
				.get("CeilingFloor", "ALL");

		Map<String, String> marketCeilingInfos = new HashMap<String, String>();
		Map<String, String> marketfloorInfos = new HashMap<String, String>();
		for (MarketStatisMessage market : marketList) {

			if (market.getType().equals("CEILING")) {
				marketCeilingInfos.put(market.getFloor(), market.getCount());
			} else {
				marketfloorInfos.put(market.getFloor(), market.getCount());
			}
		}
		marketCeilingFloor.put("ceiling", marketCeilingInfos);
		marketCeilingFloor.put("floor", marketfloorInfos);

		return marketCeilingFloor;
	}

	private boolean isEmpty(String codes) {
		return codes == null || codes.trim().length() == 0;
	}

}