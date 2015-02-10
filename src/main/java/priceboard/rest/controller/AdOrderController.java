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
import vn.com.vndirect.priceservice.datamodel.FloorCode;
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
		List<PutThrough> putThroughList_hose = (List<PutThrough>) memory.get(
				"PutThrough", FloorCode.HOSE.getCode());
		List<PutThrough> putThroughList_hnx = (List<PutThrough>) memory.get(
				"PutThrough", FloorCode.HNX.getCode());

		if (putThroughList_hose == null && putThroughList_hnx == null)
			return adOrderList;
		if (putThroughList_hose != null) {
			List<Map<String, Object>> list_hose = getAdOrderList(adOrderList,
					FloorCode.HOSE.getCode());

			for (PutThrough adorder : putThroughList_hose) {
				list_hose.add(createAdOrderInfo(adorder));
			}
		}
		if (putThroughList_hnx != null) {
			List<Map<String, Object>> list_hnx = getAdOrderList(adOrderList,
					FloorCode.HNX.getCode());
			for (PutThrough adorder : putThroughList_hnx) {
				list_hnx.add(createAdOrderInfo(adorder));
			}
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
		String symbol = adorder.getStockSymbol();
		adOrderInfos.put("stockSymbol", symbol );
		adOrderInfos.put("price", adorder.getPrice());
		adOrderInfos.put("vol", adorder.getVol());
		adOrderInfos.put("type", adorder.getType());
		adOrderInfos.put("status", adorder.getStatus());
		adOrderInfos.put("time", adorder.getTime());
		adOrderInfos.put("tradeId", adorder.getTradeId());
		adOrderInfos.put("stockId", adorder.getStockId());
		adOrderInfos.put("tradingDate", adorder.getTradingDate());
		 
		adOrderInfos.put("basicPrice", adorder.getBasicPrice());
		adOrderInfos.put("ceilingPrice", adorder.getCeilingPrice());
		adOrderInfos.put("floorPrice", adorder.getFloorPrice());
		
		return adOrderInfos;
	}

}