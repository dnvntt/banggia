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
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

@Controller
@RequestMapping("/ptorder")
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
		Map<String, List<Map<String, Object>>> PtOrderList = new HashMap<String, List<Map<String, Object>>>();
		List<PutThroughTransaction> PutThroughList = (List<PutThroughTransaction>) memory.get("PutThroughTransaction", "");
        if(PutThroughList==null) return PtOrderList; 
		for (PutThroughTransaction ptorder : PutThroughList) {
			String floorCode = ptorder.getFloorCode();
			List<Map<String, Object>> list = getPtOrderList(PtOrderList,floorCode);		
			list.add(createPtOrderInfo(ptorder));

		}

		return PtOrderList;
	}
	
	private List<Map<String,Object>> getPtOrderList(Map<String, List<Map<String,Object>>> PtOrderList, String floorCode){
		if (PtOrderList.get(floorCode) == null) {
			PtOrderList.put(floorCode, new ArrayList<Map<String,Object>>());
		}
		
		return PtOrderList.get(floorCode);
	}
	 
	private Map<String,Object> createPtOrderInfo(PutThroughTransaction ptorder) {
		Map<String,Object> PtOrderInfos = new HashMap<String,Object>();
		PtOrderInfos.put("floorCode", ptorder.getFloorCode());
		PtOrderInfos.put("symbol", ptorder.getSymbol());
		PtOrderInfos.put("price", ptorder.getPrice());
		PtOrderInfos.put("volume", ptorder.getVolume());
		PtOrderInfos.put("time", ptorder.getTime());
		PtOrderInfos.put("stockNo",  ptorder.getStockNo());
		PtOrderInfos.put("tradingDate", ptorder.getTradingDate());
		PtOrderInfos.put("basicPrice",""); 
		PtOrderInfos.put("ceilingPrice", "");
		PtOrderInfos.put("floorPrice", "");
		return PtOrderInfos;
	}
	 
	
}