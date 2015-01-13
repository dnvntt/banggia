package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThrough;

@Controller
@RequestMapping("/priceservice/adorder")
public class AdOrderController {

	@Autowired
	private InMemory memory;

	@Autowired
	private JsonParser jsonParser;

	@Autowired
	public AdOrderController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}

	@RequestMapping(value = "/history/", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<Map<String, Object>>> getPtOrder() {
		Map<String, List<Map<String, Object>>> adOrderList = new HashMap<String, List<Map<String, Object>>>();
		List<PutThrough> putThroughList = (List<PutThrough>) memory.get(
				"PutThrough", "");
		if (putThroughList == null)
			return adOrderList;

		for (PutThrough adorder : putThroughList) {
			String floorCode = adorder.getFloorCode();
			List<Map<String, Object>> list = getAdOrderList(adOrderList,
					floorCode);
			list.add(createAdOrderInfo(adorder));
		}

		return adOrderList;
	}

	private List<Map<String, Object>> getAdOrderList(
			Map<String, List<Map<String, Object>>> adOrderList, String floorCode) {
		if (adOrderList.get(floorCode) == null) {
			adOrderList.put(floorCode, new ArrayList<Map<String, Object>>());
		}

		return adOrderList.get(floorCode);
	}

	private Map<String, Object> createAdOrderInfo(PutThrough adorder) {
		Map<String, Object> adOrderInfos = new HashMap<String, Object>();
		adOrderInfos.put("floorCode", adorder.getFloorCode());
		adOrderInfos.put("stockSymbol", adorder.getStockSymbol());
		adOrderInfos.put("price", adorder.getPrice());
		adOrderInfos.put("vol", adorder.getVol());
		adOrderInfos.put("type", adorder.getType());
		adOrderInfos.put("status", adorder.getStatus());
		adOrderInfos.put("time", adorder.getTime());
		adOrderInfos.put("tradeId", adorder.getTradeId());
		adOrderInfos.put("stockId", adorder.getStockId());
		adOrderInfos.put("tradingDate", adorder.getTradingDate());
		adOrderInfos.put("basicPrice", "");
		adOrderInfos.put("ceilingPrice", "");
		adOrderInfos.put("floorPrice", "");
		return adOrderInfos;
	}

}