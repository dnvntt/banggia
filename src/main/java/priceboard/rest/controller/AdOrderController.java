package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

@Controller
@RequestMapping("/adorder")
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
		Map<String, List<Map<String,Object>>> AdOrderList = new HashMap<String, List<Map<String,Object>>>();
		List<PutThrough>  PutThroughList =  (List<PutThrough>) memory.get("PutThrough", "");
		if(PutThroughList==null) return AdOrderList;

	
		for (PutThrough adorder : PutThroughList) {
			String floorCode = adorder.getFloorCode();
			List<Map<String, Object>> list = getAdOrderList(AdOrderList,floorCode);
			list.add(createAdOrderInfo(adorder));
		}

		 return  AdOrderList;    
	}
	 
	private List<Map<String,Object>> getAdOrderList(Map<String, List<Map<String,Object>>> AdOrderList, String floorCode){
		if (AdOrderList.get(floorCode) == null) {
			AdOrderList.put(floorCode, new ArrayList<Map<String,Object>>());
		}
		
		return AdOrderList.get(floorCode);
	}
	
	private Map<String,Object> createAdOrderInfo(PutThrough adorder) {
		Map<String,Object> AdOrderInfos = new HashMap<String,Object>();
		AdOrderInfos.put("floorCode", adorder.getFloorCode());
		AdOrderInfos.put("stockSymbol", adorder.getStockSymbol());
		AdOrderInfos.put("price", adorder.getPrice());
		AdOrderInfos.put("vol", adorder.getVol());
		AdOrderInfos.put("type", adorder.getType());
		AdOrderInfos.put("status",  adorder.getStatus());
		AdOrderInfos.put("time", adorder.getTime());
		AdOrderInfos.put("tradeId", adorder.getTradeId());
		AdOrderInfos.put("stockId", adorder.getStockId());
		AdOrderInfos.put("tradingDate",adorder.getTradingDate());
		AdOrderInfos.put("basicPrice", "");
		AdOrderInfos.put("ceilingPrice", "");
		AdOrderInfos.put("floorPrice", "");
		return AdOrderInfos;
	}
	
}