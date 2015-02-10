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
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

@Controller
@RequestMapping("/priceservice/ptorder")
public class PtOrderController {

	@Autowired
	private InMemory memory;

	@Autowired
	private JsonParser jsonParser;

	@Autowired
	public PtOrderController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}

	@RequestMapping(value = "/history/", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<Map<String, Object>>> getPtOrder() {
		Map<String, List<Map<String, Object>>> ptOrderList = new HashMap<String, List<Map<String, Object>>>();
		List<PutThroughTransaction> putThroughList_hose = (List<PutThroughTransaction>) memory
				.get("PutThroughTransaction", FloorCode.HOSE.getCode());
		List<PutThroughTransaction> putThroughList_hnx = (List<PutThroughTransaction>) memory
				.get("PutThroughTransaction", FloorCode.HNX.getCode());

		if (putThroughList_hose == null && putThroughList_hnx == null)
			return ptOrderList;

		if (putThroughList_hose != null) {

			List<Map<String, Object>> list_hose = getPtOrderList(ptOrderList,
					FloorCode.HOSE.getCode());
			for (PutThroughTransaction ptorder : putThroughList_hose) {
				list_hose.add(createPtOrderInfo(ptorder));
			}
		}

		if (putThroughList_hnx != null) {
			List<Map<String, Object>> list_hnx = getPtOrderList(ptOrderList,
					FloorCode.HNX.getCode());
			for (PutThroughTransaction ptorder : putThroughList_hnx) {
				list_hnx.add(createPtOrderInfo(ptorder));
			}
		}

		return ptOrderList;
	}

	private List<Map<String, Object>> getPtOrderList(
			Map<String, List<Map<String, Object>>> ptOrderList, String floorCode) {
		if (ptOrderList.get(floorCode) == null) {
			ptOrderList.put(floorCode, new ArrayList<Map<String, Object>>());
		}

		return ptOrderList.get(floorCode);
	}

	private Map<String, Object> createPtOrderInfo(PutThroughTransaction ptorder) {
		Map<String, Object> ptOrderInfos = new HashMap<String, Object>();
		ptOrderInfos.put("floorCode", ptorder.getFloorCode());
		String symbol= ptorder.getSymbol();
		ptOrderInfos.put("symbol",symbol);
		ptOrderInfos.put("price", ptorder.getPrice());
		ptOrderInfos.put("volume", ptorder.getVolume());
		ptOrderInfos.put("time", ptorder.getTime());
		ptOrderInfos.put("stockNo", ptorder.getStockNo());
		ptOrderInfos.put("tradingDate", ptorder.getTradingDate());
		
		ptOrderInfos.put("basicPrice", ptorder.getBasicPrice());
		ptOrderInfos.put("ceilingPrice", ptorder.getCeilingPrice());
		ptOrderInfos.put("floorPrice", ptorder.getFloorPrice());
		return ptOrderInfos;
	}

}