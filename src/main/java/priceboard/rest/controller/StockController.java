package priceboard.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import priceboard.json.JsonParser;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@RestController
@RequestMapping("/secinfo")
public class StockController {

	@Autowired
	private InMemory memory;


	@Autowired
	private StockRoomManager stockRoomManager;
	
	@Autowired
	private JsonParser jsonParser;
	
	@Autowired
	public StockController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}
	
	@RequestMapping(value = "/snapshot/q=codes:{codes}", method = RequestMethod.GET)
	public @ResponseBody String[] getStock(@PathVariable String codes, ModelMap model) {
		if (isEmpty(codes)) return new String[]{};
		String[] arrCodes = codes.split(",");
		String[] secInfos = new String[arrCodes.length];
		int i = 0;
		for (String code : arrCodes) {
			if (code.trim().length() == 0) continue;
			Object secInfo = memory.get("STOCK_COMPRESSION", code);
			if (secInfo == null) continue;
			secInfos[i++] = (String) secInfo;
		}
		return secInfos;
	}

	@RequestMapping(value = "/snapshot/q=floorCode:{floorCode}", method = RequestMethod.GET)
	public @ResponseBody String[] getStockByFloorCode(@PathVariable String floorCode, ModelMap model) {
		if (isEmpty(floorCode)) return new String[]{};
		
		List<String> codes = stockRoomManager.getStocksByRoom(floorCode);
		
		String[] secInfos = new String[codes.size()];
		int i = 0;
		for (String code : codes) {
			if (code.trim().length() == 0) continue;
			Object secInfo = memory.get("STOCK_COMPRESSION", code);
			if (secInfo == null) continue;
			secInfos[i++] = (String) secInfo;
		}
		
		
		 return new String[]{};
	}
	
	private boolean isEmpty(String codes) {
		return codes == null || codes.trim().length() == 0;
	}
}


