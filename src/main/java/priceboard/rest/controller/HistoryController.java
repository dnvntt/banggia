package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

@Controller
@RequestMapping("/priceservice/market")
public class HistoryController {

	@Autowired
	private InMemory memory;

	@Autowired
	private JsonParser jsonParser;

	@Autowired
	public HistoryController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}

	@RequestMapping(value = "/history/q=codes:{codes}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Map<String, List<String>>> getMarketHistory(@PathVariable String codes, ModelMap modelMap) {
		Map<String, Map<String, List<String>>> marketHistoryByFloorCode = new HashMap<String, Map<String, List<String>>>();
		if (isEmpty(codes))
			return marketHistoryByFloorCode;

		String[] arrCodes = codes.split(",");
		for (String code : arrCodes) {
			List<Market> marketList = (ArrayList<Market>) memory.get("ALL_MARKET", code);
			if (marketList == null || code.trim().length() == 0) {
				continue;
			}

			HashMap<String, List<String>> marketInfo = createMarketInfo(marketList);
			marketHistoryByFloorCode.put(code, marketInfo);
		}
		return marketHistoryByFloorCode;
	}

	private HashMap<String, List<String>> createMarketInfo(List<Market> marketList) {
		HashMap<String, List<String>> marketInfo = new HashMap<String, List<String>>();
		List<String> tradingTimes = new ArrayList<String>();
		List<String> sequences = new ArrayList<String>();
		List<String> totalShareTradeds = new ArrayList<String>();
		List<String> priorMarketIndexs = new ArrayList<String>();
		List<String> marketIndexs = new ArrayList<String>();
		for (Market market : marketList) {
			tradingTimes.add(market.getTradingTime());
			sequences.add(Integer.toString(market.getSequence()));
			totalShareTradeds.add(Double.toString(market.getTotalShareTraded()));
			priorMarketIndexs.add(Double.toString(market.getPriorMarketIndex()));
			marketIndexs.add(Double.toString(market.getMarketIndex()));
		}

		marketInfo.put("tradingTime", tradingTimes);
		marketInfo.put("totalShareTraded", totalShareTradeds);
		marketInfo.put("priorMarketIndex", priorMarketIndexs);
		marketInfo.put("marketIndex", marketIndexs);
		marketInfo.put("sequence", sequences);
		return marketInfo;
	}

	private boolean isEmpty(String codes) {
		return codes == null || codes.trim().length() == 0;
	}

}
