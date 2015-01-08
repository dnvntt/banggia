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
import priceboard.room.StockRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

@Controller
@RequestMapping("/market1")
public class HistoryController_ {

	@Autowired
	private InMemory memory;
	
	@Autowired
	private JsonParser jsonParser;
	
	@Autowired
	public HistoryController_(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}
	
	@RequestMapping(value = "/history/q=codes:{codes}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Map<String, List<String>>> getMarketHistory(@PathVariable String codes, ModelMap model) {
		Map<String, Map<String, List<String>>> marketHistoryByFloorCode = new HashMap<String, Map<String, List<String>>>();
		
		if (isEmpty(codes)) return marketHistoryByFloorCode;
		
		String[] arrCodes = codes.split(",");
		
 
		for (String code : arrCodes) {
			if (code.trim().length() == 0) continue;
			List<Market> marketList =  (ArrayList<Market>) memory.get("ALL_MARKET", code);
			if (marketList == null) continue;
			Collections.sort(marketList,new Comparator<Market>(){

				@Override
				public int compare(Market o1, Market o2) {
					// TODO Auto-generated method stub
					return o1.getTradingTime().compareTo(o2.getTradingTime());
				}
			}
			);
			HashMap<String, List<String>> marketInfo = new HashMap<String, List<String>>();
			
			marketHistoryByFloorCode.put(code, marketInfo);
			List<String> tradingTime = new ArrayList<String>();
			List<String> sequence = new ArrayList<String>();
			List<String> totalShareTraded = new ArrayList<String>();
			List<String> priorMarketIndex = new ArrayList<String>();
			List<String> marketIndex = new ArrayList<String>();
			for(Market market : marketList)
			{
				tradingTime.add(market.getTradingTime());
				sequence.add(Integer.toString(market.getSequence()));
				totalShareTraded.add(Double.toString(market.getTotalShareTraded()));
				marketIndex.add(Double.toString(market.getMarketIndex()));
			}
			 
			marketInfo.put("tradingTime", tradingTime);
			marketInfo.put("totalShareTraded", totalShareTraded);
			marketInfo.put("priorMarketIndex", priorMarketIndex);
			marketInfo.put("marketIndex", marketIndex);
			marketInfo.put("sequence", sequence);
		}
		
		 return  marketHistoryByFloorCode;    
		 
	}	
	
	 
	private boolean isEmpty(String codes) {
		return codes == null || codes.trim().length() == 0;
	}
	
	
}